package xyz.npgw.test.common.base;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.options.RequestOptions;
import io.qameta.allure.Allure;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import xyz.npgw.test.common.BrowserFactory;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.UserRole;
import xyz.npgw.test.page.AboutBlankPage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static xyz.npgw.test.common.base.StateManager.getState;
import static xyz.npgw.test.common.base.StateManager.isOk;
import static xyz.npgw.test.common.base.StateManager.setState;

@Log4j2
@Listeners(TestListener.class)
public abstract class BaseTest {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    @Getter(AccessLevel.PROTECTED)
    private Page page;
    @Getter(AccessLevel.PROTECTED)
    private APIRequestContext apiRequestContext;
    private LocalTime apiTokenBestBefore = LocalTime.now();
    private String testId;
    private LocalTime superTokenBestBefore = LocalTime.now();
    private LocalTime adminTokenBestBefore = LocalTime.now();
    private LocalTime userTokenBestBefore = LocalTime.now();
    private LocalTime noneTokenBestBefore = LocalTime.now();

    @BeforeClass
    protected void beforeClass(ITestContext testContext) {
        playwright = Playwright.create(new Playwright.CreateOptions().setEnv(ProjectProperties.getEnv()));
        browser = BrowserFactory.getBrowser(playwright);
        log.info(">>> >>> >>> CLASS {}", testContext.getAttribute("testRunId"));
    }

    @BeforeMethod
    protected void beforeMethod(ITestContext testContext, Method method, ITestResult testResult, Object[] args) throws IOException {
        log.info(">>> thread {} is entering before method", Thread.currentThread().getName());
        log.info("- super {} admin {} user {}-", getState().getSuperExpiration(),
                getState().getAdminExpiration(), getState().getUserExpiration());

        testId = "%s/%s/%s/%s(%d)%s".formatted(
                ProjectProperties.getArtefactDir(),
                ProjectProperties.getBrowserType(),
                method.getDeclaringClass().getSimpleName(),
                method.getName(),
                testResult.getMethod().getCurrentInvocationCount(),
                new SimpleDateFormat("_MMdd_HHmmss").format(new Date()));
        Thread.currentThread().setName("th%s%s".formatted(
                Thread.currentThread().getId(),
                testId.substring(testId.length() - 12)));
        log.info(">>> {}", testId);

        if (ProjectProperties.isSkipMode() && ProjectProperties.isFailFast()) {
            log.info("Test {} skipped isSkipMode && isFailFast - true th {}", testId, Thread.currentThread().getId());
            throw new SkipException("Test skipped due to failFast option being true");
        }

        Browser.NewContextOptions options = new Browser
                .NewContextOptions()
                .setLocale("en-GB")
                .setTimezoneId("Europe/Paris")
                .setColorScheme(ProjectProperties.getColorScheme())
                .setViewportSize(ProjectProperties.getViewportWidth(), ProjectProperties.getViewportHeight())
                .setBaseURL(ProjectProperties.getBaseUrl());

        RunAs runAs = RunAs.SUPER;
        if (args.length != 0 && Arrays.stream(RunAs.values()).anyMatch(e -> e.name().equals(args[0]))) {
            runAs = RunAs.valueOf((String) args[0]);
        }
        log.info("current test will runAs {}", runAs);

//        if (runAs == RunAs.UNAUTHORISED) {
//            log.info("write NONE state file to target, overwrite if present");
//            Files.write(
//                    (new File("target/NONE-%s-state.json".formatted(Thread.currentThread().getId()))).toPath(),
//                    Collections.singleton("{\"cookies\":[],\"origins\":[]}"),
//                    StandardCharsets.UTF_8);
//        }

        if (runAs != RunAs.UNAUTHORISED && isOk(runAs)) {
            options.setStorageStatePath(
                    Paths.get("target/%s-%s-state.json".formatted(runAs, Thread.currentThread().getId())));
            log.info("set for current runAs {} option path {}", runAs, options.storageStatePath);
        }

//        UserRole userRole = UserRole.SUPER;
//        boolean isUnathorised = false;
//        if (args.length != 0 && (args[0] instanceof String)) {
//            try {
//                userRole = UserRole.valueOf((String) args[0]);
//            } catch (IllegalArgumentException ignored) {
//                if (!args[0].equals("UNAUTHORISED")) {
//                    log.info("value {} not recognized as user role, using SUPER instead", args[0]);
//                } else {
//                    isUnathorised = true;
//                    log.info("write NONE state file to target, overwrite if present");
//                    Files.write(
//                            (new File("target/NONE-%s-state.json".formatted(Thread.currentThread().getId()))).toPath(),
//                            Collections.singleton("{\"cookies\":[],\"origins\":[]}"),
//                            StandardCharsets.UTF_8);
//                    if (LocalTime.now().isBefore(noneTokenBestBefore)) {
//                        log.info("set NONE state {} th {}", testId, Thread.currentThread().getId());
//                        options.setStorageStatePath(
//                                Paths.get("target/NONE-%s-state.json".formatted(Thread.currentThread().getId())));
//                    }
//                }
//            }
//        }
//
//        if (!isUnathorised) {
//            if (userRole == UserRole.SUPER && LocalTime.now().isBefore(superTokenBestBefore)
//                    || userRole == UserRole.ADMIN && LocalTime.now().isBefore(adminTokenBestBefore)
//                    || userRole == UserRole.USER && LocalTime.now().isBefore(userTokenBestBefore)) {
//                log.info("set {} state", userRole);
//                options.setStorageStatePath(
//                        Paths.get("target/%s-%s-state.json".formatted(userRole, Thread.currentThread().getId())));
//            }
//        }
//        log.info("current userRole -> {} isUnauthorised -> {}", userRole, isUnathorised);

        if (ProjectProperties.isVideoMode()) {
            options.setRecordVideoDir(Paths.get(ProjectProperties.getArtefactDir()))
                    .setRecordVideoSize(ProjectProperties.getVideoWidth(), ProjectProperties.getVideoHeight());
        }

        context = browser.newContext(options);

        if (ProjectProperties.isTracingMode()) {
            context.tracing().start(new Tracing
                    .StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));
        }

        page = context.newPage();
        page.setDefaultTimeout(ProjectProperties.getDefaultTimeout());

        log.info("current opts are -> {} runAs {}", options.storageStatePath, runAs);
        initApiRequestContext();
        openSite(runAs, args);
    }

    @AfterMethod
    protected void afterMethod(ITestContext testContext, Method method, ITestResult testResult) throws IOException {
        if (!testResult.isSuccess() && !ProjectProperties.closeBrowserIfError()) {
            page.pause();
        }

        long testDuration = (testResult.getEndMillis() - testResult.getStartMillis()) / 1000;
        log.info("{}_{} <<< {} in {} s th {}",
                testResult.isSuccess() ? "OK" : "FAILURE", testResult.getStatus(),
                testId,
                testDuration,
                Thread.currentThread().getId());

        if (page != null) {
            page.close();
            if (ProjectProperties.isVideoMode() && page.video() != null) {
                if (testResult.getStatus() == ITestResult.FAILURE) {
                    Path videoFilePath = Paths.get(testId + ".webm");
                    page.video().saveAs(videoFilePath);
                    Allure.getLifecycle().addAttachment(
                            "video", "video/webm", "webm", Files.readAllBytes(videoFilePath));
                }
                page.video().delete();
            }
        }

        if (context != null) {
            if (ProjectProperties.isTracingMode()) {
                if (testResult.getStatus() == ITestResult.FAILURE) {
                    Path traceFilePath = Paths.get(testId + ".zip");
                    context.tracing().stop(new Tracing.StopOptions().setPath(traceFilePath));
                    Allure.getLifecycle().addAttachment(
                            "tracing", "archive/zip", "zip", Files.readAllBytes(traceFilePath));
                } else {
                    context.tracing().stop();
                }
            }
            context.close();
        }

        if (testResult.getStatus() == ITestResult.FAILURE) {
            ProjectProperties.setTracingMode(false);
            ProjectProperties.setVideoMode(false);

            if (ProjectProperties.isFailFast()) {
                ProjectProperties.setSkipMode(true);
            }
        }
    }

    @AfterClass(alwaysRun = true)
    protected void afterClass(ITestContext testContext) {
        if (browser != null) {
            browser.close();
        }
        if (apiRequestContext != null) {
            apiRequestContext.dispose();
        }
        if (playwright != null) {
            playwright.close();
        }
        log.info("<<< <<< <<< CLASS {}", testContext.getAttribute("testRunId"));
    }

    private void openSite(RunAs runAs, Object[] args) {
        if (runAs == RunAs.UNAUTHORISED || isOk(runAs)) {
            log.info("navigate('/') as {}", runAs);
            new AboutBlankPage(page).navigate("/");
            return;
        }
        log.info("login as {} setState and store {}",
                UserRole.valueOf(runAs.name()), "target/%s-%s-state.json".formatted(runAs, Thread.currentThread().getId()));
        new AboutBlankPage(page).navigate("/").loginAs(UserRole.valueOf(runAs.name()));
//            superTokenBestBefore = LocalTime.now().plusMinutes(14);
        setState(runAs);
        context.storageState(new BrowserContext
                .StorageStateOptions()
                .setPath(Paths.get("target/%s-%s-state.json".formatted(runAs, Thread.currentThread().getId()))));
    }

    private void openXSite(RunAs runAs, Object[] args) {
        UserRole userRole = UserRole.SUPER;
        if (args.length != 0 && (args[0] instanceof String)) {
            try {
                userRole = UserRole.valueOf((String) args[0]);
            } catch (IllegalArgumentException ignored) {
                if (args[0].equals("UNAUTHORISED")) {
                    log.info("is it ok?? to run as {} -> {}", runAs, isOk(runAs));
                    if (LocalTime.now().isBefore(noneTokenBestBefore)) {
                        log.info("navigate('/') as NONE {} th {} {}", noneTokenBestBefore, Thread.currentThread().getId(), runAs);
                        new AboutBlankPage(page).navigate("/");
                        return;
                    }

                    log.info("navigate('/') as NONE and store state th {} {}", Thread.currentThread().getId(), runAs);
                    new AboutBlankPage(page).navigate("/");
                    noneTokenBestBefore = LocalTime.now().plusMinutes(14);
                    setState(runAs);
                    context.storageState(new BrowserContext
                            .StorageStateOptions()
                            .setPath(Paths.get("target/NONE-%s-state.json".formatted(Thread.currentThread().getId()))));
                    return;
                }
            }
        }

        if (userRole == UserRole.SUPER) {
            log.info("is it ok?? to run as {} -> {}", runAs, isOk(runAs));
            if (LocalTime.now().isBefore(superTokenBestBefore)) {
                log.info("navigate('/') as SUPER {} th {} {}", superTokenBestBefore, Thread.currentThread().getId(), runAs);
                new AboutBlankPage(page).navigate("/");
                return;
            }
            log.info("login as {} and store state th {} {}", userRole, Thread.currentThread().getId(), runAs);
            new AboutBlankPage(page).navigate("/").loginAs(userRole);
            superTokenBestBefore = LocalTime.now().plusMinutes(14);
            setState(runAs);
            context.storageState(new BrowserContext
                    .StorageStateOptions()
                    .setPath(Paths.get("target/%s-%s-state.json".formatted(userRole, Thread.currentThread().getId()))));
        }
        if (userRole == UserRole.ADMIN) {
            log.info("is it ok?? to run as {} -> {}", runAs, isOk(runAs));
            if (LocalTime.now().isBefore(adminTokenBestBefore)) {
                log.info("navigate('/') as ADMIN {} th {} {}", adminTokenBestBefore, Thread.currentThread().getId(), runAs);
                new AboutBlankPage(page).navigate("/");
                return;
            }
            log.info("login as {} and store state th {} {}", userRole, Thread.currentThread().getId(), runAs);
            new AboutBlankPage(page).navigate("/").loginAs(userRole);
            adminTokenBestBefore = LocalTime.now().plusMinutes(14);
            setState(runAs);
            context.storageState(new BrowserContext
                    .StorageStateOptions()
                    .setPath(Paths.get("target/%s-%s-state.json".formatted(userRole, Thread.currentThread().getId()))));
        }
        if (userRole == UserRole.USER) {
            log.info("is it ok?? to run as {} -> {}", runAs, isOk(runAs));
            if (LocalTime.now().isBefore(userTokenBestBefore)) {
                log.info("navigate('/') as USER {} th {} {}", userTokenBestBefore, Thread.currentThread().getId(), runAs);
                new AboutBlankPage(page).navigate("/");
                return;
            }
            log.info("login as {} and store state th {} {}", userRole, Thread.currentThread().getId(), runAs);
            new AboutBlankPage(page).navigate("/").loginAs(userRole);
            userTokenBestBefore = LocalTime.now().plusMinutes(14);
            setState(runAs);
            context.storageState(new BrowserContext
                    .StorageStateOptions()
                    .setPath(Paths.get("target/%s-%s-state.json".formatted(userRole, Thread.currentThread().getId()))));
        }
    }

    private void initApiRequestContext() {
        if (LocalTime.now().isBefore(apiTokenBestBefore)) {
            return;
        }

        APIResponse tokenResponse = playwright
                .request()
                .newContext()
                .post(ProjectProperties.getBaseUrl() + "/portal-v1/user/token",
                        RequestOptions.create().setData(Map.of(
                                "email", ProjectProperties.getSuperEmail(),
                                "password", ProjectProperties.getSuperPassword())));

        if (tokenResponse.ok()) {
            Token token = new Gson().fromJson(tokenResponse.text(), TokenResponse.class).token();
            apiRequestContext = playwright
                    .request()
                    .newContext(new APIRequest
                            .NewContextOptions()
                            .setBaseURL(ProjectProperties.getBaseUrl())
                            .setExtraHTTPHeaders(Map.of("Authorization", "Bearer %s".formatted(token.idToken))));
            apiTokenBestBefore = LocalTime.now().plusSeconds(token.expiresIn).minusMinutes(1);
        } else {
            String message = "Retrieve API idToken failed: %s".formatted(tokenResponse.statusText());
            log.error(message);
            throw new SkipException(message);
        }
    }

    private record Token(String accessToken, int expiresIn, String idToken, String refreshToken, String tokenType) {
    }

    private record TokenResponse(String userChallengeType, Token token, String sessionId) {
    }
}
