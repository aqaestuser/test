package xyz.npgw.test.common.base;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Route;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.Video;
import com.microsoft.playwright.options.Cookie;
import io.qameta.allure.Allure;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.client.Client;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.entity.Credentials;
import xyz.npgw.test.common.entity.Token;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.common.entity.UserRole;
import xyz.npgw.test.common.util.BrowserUtils;
import xyz.npgw.test.common.util.CleanupUtils;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Log4j2
public abstract class BaseTestForSingleLogin {

    protected static final String RUN_ID = TestUtils.now();
    private final HashMap<String, Response> requestMap = new HashMap<>();
    @Getter(AccessLevel.PROTECTED)
    private Playwright playwright;
    private Browser browser;
    private BrowserContext browserContext;
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

    @BeforeSuite
    protected void beforeSuite() {
        Playwright playwright = BrowserUtils.createPlaywright();
        Token token = getTokenFromApiResponse(playwright);

        if (token != null && !token.idToken().isEmpty()) {
            apiRequestContext = getApiRequestContext(playwright, token);
            CleanupUtils.clean(apiRequestContext);
            apiRequestContext.dispose();
        }
        playwright.close();
    }

    @BeforeClass
    protected void beforeClass() {
        playwright = BrowserUtils.createPlaywright();
        initApiRequestContext();

        uid = "%s.%s".formatted(RUN_ID, Thread.currentThread().getId());
        companyName = "%s test run company".formatted(uid);
        TestUtils.createCompany(apiRequestContext, companyName);

        browser = BrowserUtils.createBrowser(playwright);
        browserContext = BrowserUtils.createContext(browser);
        browserContext.route("**/*", route -> {
            String url = route.request().url();
            if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png")) {
                if (requestMap.get(url) == null) {
                    APIResponse apiResponse = route.fetch();
                    requestMap.put(url, new Response(apiResponse.status(), apiResponse.headers(), apiResponse.body()));
                }
                Response r = requestMap.get(url);
                route.fulfill(new Route.FulfillOptions()
                        .setStatus(r.status)
                        .setHeaders(r.headers)
                        .setBodyBytes(r.body));
            } else {
                route.fallback();
            }
        });

        createBlankPage();
//        openSiteAccordingRole();
    }

    @BeforeMethod
    protected void beforeMethod(Method method, ITestResult testResult, Object[] args) {
        initApiRequestContext();

        testId = "%s/%s/%s(%d)%s".formatted(
                ProjectProperties.getArtefactDir(),
                method.getDeclaringClass().getSimpleName(),
                method.getName(),
                testResult.getMethod().getCurrentInvocationCount(),
                new SimpleDateFormat("_MMdd_HHmmss").format(new Date()));

        if (page.isClosed()) {
            createBlankPage();
            new AboutBlankPage(page).navigate("/dashboard");
        }

        if (ProjectProperties.isTracingMode()) {
            BrowserUtils.startTracing(browserContext);
        }
    }

    @AfterMethod
    protected void afterMethod(Method method, ITestResult testResult) throws IOException {
        int resultStatus = testResult.getStatus();
        boolean isNeedArtefacts = Set.of(ITestResult.FAILURE, ITestResult.SKIP).contains(resultStatus);
        if (resultStatus == ITestResult.FAILURE) {
            long testDuration = (testResult.getEndMillis() - testResult.getStartMillis()) / 1000;
            log.info("{} <<< {} in {} s", status(resultStatus), testId, testDuration);
        }
        if (!page.isClosed()) {
            page.close();
        }
        if (ProjectProperties.isVideoMode()) {
            attachVideoIfNeeded(page, testId, isNeedArtefacts);
        }

        if (ProjectProperties.isTracingMode()) {
            if (isNeedArtefacts) {
                Path traceFilePath = Paths.get(testId + ".zip");
                browserContext.tracing().stop(new Tracing.StopOptions().setPath(traceFilePath));
                Allure.getLifecycle().addAttachment(
                        "tracing", "archive/zip", "zip", Files.readAllBytes(traceFilePath));
            } else {
                browserContext.tracing().stop();
            }
        }
    }

    @AfterClass(alwaysRun = true)
    protected void afterClass() {
        if (browserContext != null) {
            try {
                browserContext.close();
            } catch (Exception ignored) {
                log.info("Attempt to close the browserContext that is already closed.");
            }
        }
        if (browser != null) {
            try {
                browser.close();
            } catch (Exception ignored) {
                log.info("Attempt to close the browser that is already closed.");
            }
        }
        if (apiRequestContext != null) {
            try {
                User.delete(apiRequestContext, "%s.super@email.com".formatted(uid));
                TestUtils.deleteCompany(apiRequestContext, companyName);
                apiRequestContext.dispose();
            } catch (Exception ignored) {
                log.info("Attempt to dispose the apiRequestContext that is already disposed.");
            }
        }
        if (playwright != null) {
            try {
                playwright.close();
            } catch (Exception ignored) {
                log.info("Attempt to close the playwright that is already closed.");
            }
        }
    }

    private void openSite(String role) {
        UserRole userRole = UserRole.valueOf(role);

        if (userRole == UserRole.USER && businessUnit == null) {
            businessUnit = TestUtils.createBusinessUnit(apiRequestContext, companyName, "default");
        }

        String email = "%s.%s@email.com".formatted(uid, userRole.toString().toLowerCase());
        if (!User.exists(apiRequestContext, email)) {
            User user = User.builder()
                    .companyName((userRole == UserRole.SUPER ? "super" : companyName))
                    .userRole(userRole)
                    .merchantIds(userRole == UserRole.USER ? new String[]{businessUnit.merchantId()} : new String[]{})
                    .email(email)
                    .build();
            User.create(apiRequestContext, user);
            User.passChallenge(apiRequestContext, user.getEmail(), user.getPassword());
        }

        new AboutBlankPage(page).navigate("/").loginAs(email, ProjectProperties.getPassword(), userRole.getName());
    }

    private void initApiRequestContext() {
        if (LocalTime.now().isBefore(bestBefore)) {
            return;
        }
        Token token = getTokenFromApiResponse(playwright);

        if (token != null && !token.idToken().isEmpty()) {
            apiRequestContext = getApiRequestContext(playwright, token);
            bestBefore = LocalTime.now().plusSeconds(token.expiresIn()).minusMinutes(3);
        }
    }

    protected void initPageRequestContext() {
        StorageState storageState = new Gson().fromJson(browserContext.storageState(), StorageState.class);
        LocalStorage[] localStorage = storageState.origins()[0].localStorage();
        String tokenData = Arrays.stream(localStorage)
                .filter(item -> item.name().equals("token_data"))
                .findAny()
                .map(LocalStorage::value)
                .orElse("");
        if (!tokenData.isEmpty()) {
            Token token = new Gson().fromJson(tokenData, Token.class);
            browserContext.setExtraHTTPHeaders(Map.of("Authorization", "Bearer %s".formatted(token.idToken())));
        }
    }

    private Token getTokenFromApiResponse(Playwright playwright) {
        APIRequestContext context = playwright.request()
                .newContext(new APIRequest.NewContextOptions().setBaseURL(ProjectProperties.getBaseURL()));
        Credentials credentials = new Credentials(ProjectProperties.getEmail(), ProjectProperties.getPassword());
        Token token = User.getTokenResponse(context, credentials).token();
        context.dispose();

        return token;
    }

    private Token getTokenFromApiResponse(Playwright playwright, Credentials credentials) {
        APIRequestContext context = playwright.request()
                .newContext(new APIRequest.NewContextOptions().setBaseURL(ProjectProperties.getBaseURL()));
        Token token = User.getTokenResponse(context, credentials).token();
        context.dispose();

        return token;
    }

    private Token getTokenFromApiResponse(Playwright playwright, String apiKey) {
        APIRequestContext context = playwright.request()
                .newContext(new APIRequest.NewContextOptions().setBaseURL(ProjectProperties.getBaseURL()));
        Token token = Client.getToken(context, apiKey);
        context.dispose();

        return token;
    }

    private APIRequestContext getApiRequestContext(Playwright playwright, Token token) {
        return playwright.request()
                .newContext(new APIRequest.NewContextOptions()
                        .setBaseURL(ProjectProperties.getBaseURL())
                        .setExtraHTTPHeaders(Map.of("Authorization", "Bearer %s".formatted(token.idToken()))));
    }

    protected APIRequestContext getApiRequestContext(Playwright playwright, Credentials credentials) {
        Token token = getTokenFromApiResponse(playwright, credentials);
        return playwright.request()
                .newContext(new APIRequest.NewContextOptions()
                        .setBaseURL(ProjectProperties.getBaseURL())
                        .setExtraHTTPHeaders(Map.of("Authorization", "Bearer %s".formatted(token.idToken()))));
    }


    protected APIRequestContext getApiRequestContext(Playwright playwright, String apiKey) {
        Token token = getTokenFromApiResponse(playwright, apiKey);
        return playwright.request()
                .newContext(new APIRequest.NewContextOptions()
                        .setBaseURL(ProjectProperties.getBaseURL())
                        .setExtraHTTPHeaders(Map.of("Authorization", "Bearer %s".formatted(token.idToken()))));
    }

    private void attachVideoIfNeeded(Page page, String testId, boolean isNeedArtefacts) throws IOException {
        Video video = page.video();
        if (video != null) {
            if (isNeedArtefacts) {
                Path videoFilePath = Paths.get(testId + ".webm");
                video.saveAs(videoFilePath);
                Allure.getLifecycle().addAttachment(
                        "video", "video/webm", "webm", Files.readAllBytes(videoFilePath));
            }
            video.delete();
        }
    }

    private void createBlankPage() {
        page = browserContext.newPage();
        page.setDefaultTimeout(ProjectProperties.getDefaultTimeout());
        page.addLocatorHandler(page.getByText("Loading..."), locator -> {
        });
    }

    protected void openSiteAccordingRole() {
        String testClassName = this.getClass().getSimpleName();
        String prefix = Stream.of("Unauthenticated", "TestUser", "TestAdmin", "User", "Admin")
                .filter(testClassName::startsWith)
                .findFirst()
                .orElse("");

        switch (prefix) {
            case "Unauthenticated":
                return;
            case "TestUser":
                new AboutBlankPage(page)
                        .navigate("/")
                        .loginAsUser("testUser@email.com", ProjectProperties.getPassword());
                return;
            case "TestAdmin":
                new AboutBlankPage(page)
                        .navigate("/")
                        .loginAsAdmin("testAdmin@email.com", ProjectProperties.getPassword());
                return;
            case "User":
                openSite("USER");
                return;
            case "Admin":
                openSite("ADMIN");
                return;
            default:
                openSite("SUPER");
        }
    }

    private boolean isTestIndependent(Method method) {
        Test testAnnotation = method.getAnnotation(Test.class);
        if (testAnnotation == null) {
            return true;
        }
        return testAnnotation.dependsOnMethods().length == 0 && testAnnotation.dependsOnGroups().length == 0;
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

    private record Response(int status, Map<String, String> headers, byte[] body) {

    }
}
