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
import xyz.npgw.test.common.entity.UserRole;
import xyz.npgw.test.page.AboutBlankPage;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static xyz.npgw.test.common.state.StateManager.isOk;
import static xyz.npgw.test.common.state.StateManager.setState;

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

    @BeforeClass
    protected void beforeClass(ITestContext testContext) {
        playwright = Playwright.create(new Playwright.CreateOptions().setEnv(ProjectProperties.getEnv()));
        browser = BrowserFactory.getBrowser(playwright);
        log.debug(">>> >>> >>> CLASS {}", testContext.getAttribute("testRunId"));
    }

    @BeforeMethod
    protected void beforeMethod(ITestContext testContext, Method method, ITestResult testResult, Object[] args) {
        log.debug(">>> thread {} is entering before method", Thread.currentThread().getName());

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
            log.info("Test {} skipped isSkipMode && isFailFast - true", testId);
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

        if (runAs != RunAs.UNAUTHORISED && isOk(runAs)) {
            options.setStorageStatePath(
                    Paths.get("target/%s-%s-state.json".formatted(runAs, Thread.currentThread().getId())));
            log.debug("set for current runAs {} option path {}", runAs, options.storageStatePath);
        }

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

        initApiRequestContext();
        openSite(runAs);
    }

    @AfterMethod
    protected void afterMethod(ITestContext testContext, Method method, ITestResult testResult) throws IOException {
        if (!testResult.isSuccess() && !ProjectProperties.closeBrowserIfError()) {
            page.pause();
        }

        long testDuration = (testResult.getEndMillis() - testResult.getStartMillis()) / 1000;
        log.info("{}_{} <<< {} in {} s",
                testResult.isSuccess() ? "OK" : "FAILURE", testResult.getStatus(), testId, testDuration);

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
        log.debug("<<< <<< <<< CLASS {}", testContext.getAttribute("testRunId"));
    }

    private void openSite(RunAs runAs) {
        if (runAs == RunAs.UNAUTHORISED || isOk(runAs)) {
            log.info("navigate('/') as {}", runAs);
            new AboutBlankPage(page).navigate("/");
            return;
        }
        log.info("login as {} setState and store {}",
                UserRole.valueOf(runAs.name()),
                "target/%s-%s-state.json".formatted(runAs, Thread.currentThread().getId()));
        new AboutBlankPage(page).navigate("/").loginAs(UserRole.valueOf(runAs.name()));
        setState(runAs);
        context.storageState(new BrowserContext
                .StorageStateOptions()
                .setPath(Paths.get("target/%s-%s-state.json".formatted(runAs, Thread.currentThread().getId()))));
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
