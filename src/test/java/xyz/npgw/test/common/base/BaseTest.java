package xyz.npgw.test.common.base;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import io.qameta.allure.Allure;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import xyz.npgw.test.common.ApiContextUtils;
import xyz.npgw.test.common.BrowserFactory;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.UserRole;
import xyz.npgw.test.page.AboutBlankPage;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
public abstract class BaseTest {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    @Getter(AccessLevel.PROTECTED)
    private Page page;
    @Getter(AccessLevel.PROTECTED)
    private APIRequestContext apiRequestContext;

    @BeforeClass
    protected void beforeClass() {
        playwright = Playwright.create();
        browser = BrowserFactory.getBrowser(playwright);
    }

    @BeforeMethod
    protected void beforeMethod(Method method, ITestResult testResult, Object[] args) {
        Browser.NewContextOptions options = new Browser
                .NewContextOptions()
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

        if (apiRequestContext == null) {
            apiRequestContext = ApiContextUtils.getApiRequestContext(playwright);
        }

        page = context.newPage();

        UserRole userRole = UserRole.SUPER;
        if (args.length != 0 && (args[0] instanceof String)) {
            try {
                userRole = UserRole.valueOf((String) args[0]);
            } catch (IllegalArgumentException e) {
                log.debug("Unknown user role, using SUPER");
            }
        }
        if (userRole == UserRole.UNAUTHORISED) {
            new AboutBlankPage(page).navigate("/");
        } else {
            new AboutBlankPage(page).navigate("/").loginAs(userRole);
        }
    }

    @AfterMethod(alwaysRun = true)
    protected void afterMethod(Method method, ITestResult testResult) {
        String testName = getTestName(method, testResult);

        if (!testResult.isSuccess() && !ProjectProperties.closeBrowserIfError()) {
            page.pause();
        }

        long testDuration = (testResult.getEndMillis() - testResult.getStartMillis()) / 1000;
        log.info("{} {} in {} s", testName, testResult.isSuccess(), testDuration);

        Path videoFilePath = Paths
                .get(ProjectProperties.getArtefactDir(), ProjectProperties.getBrowserType(), testName + ".webm");
        if (page != null) {
            page.close();
            if (ProjectProperties.isVideoMode() && page.video() != null) {
                if (!testResult.isSuccess()) {
                    page.video().saveAs(videoFilePath);
                    addVideoToAllure(videoFilePath);
                }
                page.video().delete();
            }
        }

        Path traceFilePath = Paths
                .get(ProjectProperties.getArtefactDir(), ProjectProperties.getBrowserType(), testName + ".zip");
        if (context != null) {
            if (ProjectProperties.isTracingMode()) {
                if (testResult.isSuccess()) {
                    context.tracing().stop();
                } else {
                    context.tracing().stop(new Tracing.StopOptions().setPath(traceFilePath));
                    addTracesToAllure(traceFilePath);
                }
            }
            context.close();
        }
    }

    @AfterClass(alwaysRun = true)
    protected void afterClass() {
        if (browser != null) {
            browser.close();
        }
        if (apiRequestContext != null) {
            apiRequestContext.dispose();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    private String getTestName(Method method, ITestResult testResult) {
        String testName = method.getDeclaringClass().getSimpleName() + "/" + method.getName();
        if (!method.getAnnotation(Test.class).dataProvider().isEmpty()) {
            testName = "%s(%d)".formatted(testName, testResult.getMethod().getCurrentInvocationCount() - 1);
        }
        return testName + new SimpleDateFormat("_MMdd_HHmmss").format(new Date());
    }

    private void addVideoToAllure(Path path) {
        try {
            Allure.getLifecycle()
                    .addAttachment("video", "video/webm", "webm", Files.readAllBytes(path));
        } catch (IOException e) {
            log.error("Add video to allure failed: {}", e.getMessage());
        }
    }

    private void addTracesToAllure(Path path) {
        try {
            Allure.getLifecycle()
                    .addAttachment("tracing", "archive/zip", "zip", Files.readAllBytes(path));
        } catch (IOException e) {
            log.error("Add traces to allure failed: {}", e.getMessage());
        }
    }
}
