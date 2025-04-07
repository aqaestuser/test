package xyz.npgw.test.common.base;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import io.qameta.allure.Allure;
import lombok.extern.log4j.Log4j2;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import xyz.npgw.test.common.BrowserFactory;
import xyz.npgw.test.common.PlaywrightOptions;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.ProjectUtils;
import xyz.npgw.test.common.UserRole;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Log4j2
public abstract class BaseTest {

    private static final String ARTEFACT_DIR = "target/artefact";

    private Playwright playwright;
    private Browser browser;
    private String browserType;
    private BrowserContext context;
    private Page page;
    private APIRequestContext request;

    @Parameters("browserType")
    @BeforeClass
    protected void beforeClass(@Optional("Chromium") String browserType) {
        this.browserType = browserType.toUpperCase();
        try {
            playwright = Playwright.create();
            browser = BrowserFactory.getBrowser(playwright, this.browserType);
        } catch (IllegalArgumentException e) {
            if (playwright != null) {
                playwright.close();
            }
            log.error("Unsupported browser: {}", browserType);
            System.exit(1);
        } catch (RuntimeException e) {
            log.error("Playwright.create() failed: {}", e.getMessage());
            System.exit(2);
        }
    }

    @BeforeMethod
    protected void beforeMethod(Method method, ITestResult testResult, Object[] args) {
        context = browser.newContext(PlaywrightOptions.contextOptions());

        if (ProjectProperties.isTracingMode()) {
            context.tracing().start(PlaywrightOptions.tracingStartOptions());
        }

        APIResponse tokenResponse = playwright.request().newContext().post(
                ProjectProperties.getBaseUrl() + "/portal-v1/user/token",
                RequestOptions.create().setData(
                        Map.of("email", ProjectProperties.getUserEmail(),
                                "password", ProjectProperties.getUserPassword())));
        if (tokenResponse.ok()) {
            log.debug(tokenResponse.text());
            String idToken = new Gson().fromJson(tokenResponse.text(), TokenResponse.class).token().idToken;
            request = playwright.request().newContext(PlaywrightOptions.apiContextOptions(idToken));
        } else {
            log.error("Retrieve API idToken failed: {}", tokenResponse.statusText());
            System.exit(5);
        }

        page = context.newPage();

        UserRole userRole = UserRole.SUPER;
        if (args.length != 0 && (args[0] instanceof String)) {
            try {
                userRole = UserRole.valueOf((String) args[0]);
            } catch (IllegalArgumentException e) {
                log.debug("Unknown UserRole, using 'SUPER' as default. {}", e.getMessage());
            }
        }
        if (userRole != UserRole.GUEST) {
            Allure.step("Login to the site as %s".formatted(userRole));
            ProjectUtils.loginAsRole(page, userRole);
        }
    }

    @AfterMethod(alwaysRun = true)
    protected void afterMethod(Method method, ITestResult testResult) {
        String testName = ProjectUtils.getTestClassCompleteMethodName(method, testResult);
        Path traceFilePath = Paths.get(ARTEFACT_DIR, browserType, testName + ".zip");
        Path videoFilePath = Paths.get(ARTEFACT_DIR, browserType, testName + ".webm");

        if (page != null) {
            page.close();

            if (ProjectProperties.isVideoMode()) {
                page.video().saveAs(videoFilePath);
                page.video().delete();
            }
        }

        if (context != null) {
            if (ProjectProperties.isTracingMode()) {
                context.tracing().stop(PlaywrightOptions.tracingStopOptions(traceFilePath));
            }
            context.close();
        }

        if (!testResult.isSuccess()) { // && ProjectProperties.isServerRun()) {
            if (ProjectProperties.isVideoMode()) {
                try {
                    Allure.getLifecycle().addAttachment(
                            "video", "video/webm", "webm", Files.readAllBytes(videoFilePath));
                } catch (IOException e) {
                    log.error("Add video to allure failed: {}", e.getMessage());
                }
            }
            if (ProjectProperties.isTracingMode()) {
                try {
                    Allure.getLifecycle().addAttachment(
                            "tracing", "archive/zip", "zip", Files.readAllBytes(traceFilePath));
                } catch (IOException e) {
                    log.error("Add traces to allure failed: {}", e.getMessage());
                }
            }
        }
    }

    @AfterClass(alwaysRun = true)
    protected void afterClass() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    protected Page getPage() {
        return page;
    }

    private record Token(String accessToken, int expiresIn, String idToken, String refreshToken, String tokenType) {
    }

    private record TokenResponse(String userChallengeType, Token token) {
    }
}
