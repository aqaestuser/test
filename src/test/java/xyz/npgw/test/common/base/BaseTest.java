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
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.RequestOptions;
import io.qameta.allure.Allure;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import xyz.npgw.test.common.BrowserFactory;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.common.entity.UserRole;
import xyz.npgw.test.common.util.TestUtils;
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

@Log4j2
public abstract class BaseTest {

    protected static final String RUN_ID = new SimpleDateFormat("MMdd.HHmmss").format(new Date());

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    @Getter(AccessLevel.PROTECTED)
    private Page page;
    @Getter(AccessLevel.PROTECTED)
    private APIRequestContext apiRequestContext;
    private LocalTime bestBefore = LocalTime.now();
    private String testId;

    @Getter(AccessLevel.PROTECTED)
    private String uid;
    @Getter(AccessLevel.PROTECTED)
    private String companyName;
    private BusinessUnit businessUnit;

    @BeforeClass
    protected void beforeClass() {
        playwright = Playwright.create(new Playwright.CreateOptions().setEnv(ProjectProperties.getEnv()));
        initApiRequestContext();
        browser = BrowserFactory.getBrowser(playwright);

        uid = "%s.%s".formatted(RUN_ID, Thread.currentThread().getId());
        companyName = "%s test run company".formatted(uid);
        TestUtils.createCompany(apiRequestContext, companyName);
    }

    @BeforeMethod
    protected void beforeMethod(Method method, ITestResult testResult, Object[] args) {
        testId = "%s/%s/%s/%s(%d)%s".formatted(
                ProjectProperties.getArtefactDir(),
                ProjectProperties.getBrowserType(),
                method.getDeclaringClass().getSimpleName(),
                method.getName(),
                testResult.getMethod().getCurrentInvocationCount(),
                new SimpleDateFormat("_MMdd_HHmmss").format(new Date()));
        log.info(">>> {}", testId);

        if (ProjectProperties.isSkipMode() && ProjectProperties.isFailFast()) {
            throw new SkipException("Test skipped due to failFast option being true");
        }

        Browser.NewContextOptions options = new Browser
                .NewContextOptions()
                .setLocale("en-GB")
                .setTimezoneId("Europe/Paris")
                .setColorScheme(ProjectProperties.getColorScheme())
                .setViewportSize(ProjectProperties.getViewportWidth(), ProjectProperties.getViewportHeight())
                .setBaseURL(ProjectProperties.getBaseUrl());

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
        openSite(args);
    }

    private void openSite(Object[] args) {
        UserRole userRole = UserRole.SUPER;
        if (args.length != 0 && (args[0] instanceof String)) {
            try {
                userRole = UserRole.valueOf((String) args[0]);
            } catch (IllegalArgumentException e) {
                if (args[0].equals("UNAUTHORISED")) {
                    new AboutBlankPage(page).navigate("/");
                    return;
                }
            }
        }

        if (userRole == UserRole.USER && businessUnit == null) {
            businessUnit = TestUtils.createBusinessUnit(apiRequestContext, companyName, "default");
        }

        String email = "%s.%s@email.com".formatted(uid, userRole.toString().toLowerCase());
        if (!User.exists(apiRequestContext, email)) {
            User user = new User(
                    (userRole == UserRole.SUPER) ? "super" : companyName,
                    true,
                    userRole,
                    (userRole == UserRole.USER) ? new String[]{businessUnit.merchantId()} : new String[]{},
                    email,
                    ProjectProperties.getUserPassword());
            User.create(apiRequestContext, user);
            User.passChallenge(apiRequestContext, user.email(), user.password());
        }

        new AboutBlankPage(page).navigate("/").loginAs(email, ProjectProperties.getUserPassword());
//        initPageRequestContext();
    }

    @AfterMethod
    protected void afterMethod(Method method, ITestResult testResult) throws IOException {
        if (!testResult.isSuccess() && !ProjectProperties.closeBrowserIfError()) {
            page.pause();
        }

        long testDuration = (testResult.getEndMillis() - testResult.getStartMillis()) / 1000;
        log.info("{} <<< {} in {} s", status(testResult.getStatus()), testId, testDuration);

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
    protected void afterClass() {
        if (browser != null) {
            browser.close();
        }
        if (apiRequestContext != null) {
            User.delete(apiRequestContext, "%s.super@email.com".formatted(uid));
            TestUtils.deleteCompany(apiRequestContext, companyName);
            log.info("                 --- Class finished ---                 ");
            apiRequestContext.dispose();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    private void initApiRequestContext() {
        if (LocalTime.now().isBefore(bestBefore)) {
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
            bestBefore = LocalTime.now().plusSeconds(token.expiresIn).minusMinutes(1);
        } else {
            String message = "Retrieve API idToken failed: %s".formatted(tokenResponse.statusText());
            log.error(message);
            throw new SkipException(message);
        }
    }

    private void initPageRequestContext() {
        StorageState storageState = new Gson().fromJson(context.storageState(), StorageState.class);
        LocalStorage[] localStorage = storageState.origins()[0].localStorage();
        String tokenData = Arrays.stream(localStorage)
                .filter(item -> item.name().equals("token_data"))
                .findAny()
                .map(LocalStorage::value)
                .orElse("");
        if (!tokenData.isEmpty()) {
            Token token = new Gson().fromJson(tokenData, Token.class);
            context.setExtraHTTPHeaders(Map.of("Authorization", "Bearer %s".formatted(token.idToken)));
        }
    }

    private String status(int code) {
        return switch (code) {
            case -1 -> "CREATED";
            case 1 -> "SUCCESS";
            case 2 -> "FAILURE";
            case 3 -> "SKIP";
            case 4 -> "SUCCESS_PERCENTAGE_FAILURE";
            case 16 -> "STARTED";
            default -> throw new IllegalStateException("Unexpected value: " + code);
        };
    }

    private record StorageState(Cookie[] cookies, Origin[] origins) {
    }

    private record Origin(String origin, LocalStorage[] localStorage) {
    }

    private record LocalStorage(String name, String value) {
    }

    private record Token(String accessToken, int expiresIn, String idToken, String refreshToken, String tokenType) {
    }

    private record TokenResponse(String userChallengeType, Token token, String sessionId) {
    }
}
