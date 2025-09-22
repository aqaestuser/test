package xyz.npgw.test.common.base;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Video;
import com.microsoft.playwright.options.Cookie;
import io.qameta.allure.Allure;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.client.Client;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.entity.Credentials;
import xyz.npgw.test.common.entity.Token;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.common.util.BrowserUtils;
import xyz.npgw.test.common.util.CleanupUtils;
import xyz.npgw.test.common.util.TestUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public abstract class BaseTest {

    protected static final String RUN_ID = TestUtils.now();
    protected final HashMap<String, BaseTestForSingleLogin.Response> requestMap = new HashMap<>();
    @Getter(AccessLevel.PROTECTED)
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext browserContext;
    @Getter(AccessLevel.PROTECTED)
    protected Page page;
    @Getter(AccessLevel.PROTECTED)
    protected APIRequestContext apiRequestContext;
    protected LocalTime bestBefore = LocalTime.now();
    protected String testId;
    @Getter(AccessLevel.PROTECTED)
    protected String uid;
    @Getter(AccessLevel.PROTECTED)
    protected String companyName;
    protected BusinessUnit businessUnit;

    protected static final Map<String, Long> classDurations = new ConcurrentHashMap<>();
    protected long startTime;

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

        startTime = System.currentTimeMillis();
    }

    @AfterClass(alwaysRun = true)
    protected void afterClass() {
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
        classDurations.put(getClass().getSimpleName(), (System.currentTimeMillis() - startTime) / 1000);
    }

    @AfterSuite
    protected void afterSuite() throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("Class,Duration(s)");
        lines.addAll(
                classDurations.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .map(e -> e.getKey() + "," + e.getValue())
                        .toList()
        );

        Files.write(Paths.get("durations.csv"), lines);
    }

    protected void createBlankPage() {
        page = browserContext.newPage();
        page.setDefaultTimeout(ProjectProperties.getDefaultTimeout());
        page.addLocatorHandler(page.getByText("Loading..."), locator -> {
        });
    }

    private boolean isTestIndependent(Method method) {
        Test testAnnotation = method.getAnnotation(Test.class);
        if (testAnnotation == null) {
            return true;
        }
        return testAnnotation.dependsOnMethods().length == 0 && testAnnotation.dependsOnGroups().length == 0;
    }

    protected void initApiRequestContext() {
        if (LocalTime.now().isBefore(bestBefore)) {
            return;
        }
        Token token = getTokenFromApiResponse(playwright);

        if (token != null && !token.idToken().isEmpty()) {
            apiRequestContext = getApiRequestContext(playwright, token);
            bestBefore = LocalTime.now().plusSeconds(token.expiresIn()).minusMinutes(3);
        }
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

    private void initPageRequestContext() {
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

    protected void attachVideoIfNeeded(Page page, String testId, boolean isNeedArtefacts) throws IOException {
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

    protected void attachVideoIfNeeded(Page page, String testId, int resultStatus) throws IOException {
        Video video = page.video();
        if (video != null) {
            if (Set.of(ITestResult.FAILURE, ITestResult.SKIP).contains(resultStatus)) {
                Path videoFilePath = Paths.get(testId + ".webm");
                video.saveAs(videoFilePath);
                Allure.getLifecycle().addAttachment(
                        "video", "video/webm", "webm", Files.readAllBytes(videoFilePath));
            }
            video.delete();
        }
    }

    protected String status(int code) {
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

    protected record StorageState(Cookie[] cookies, Origin[] origins) {
    }

    protected record Origin(String origin, LocalStorage[] localStorage) {
    }

    protected record LocalStorage(String name, String value) {
    }

    protected record Response(int status, Map<String, String> headers, byte[] body) {

    }
}
