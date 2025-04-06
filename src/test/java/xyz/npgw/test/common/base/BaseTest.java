package xyz.npgw.test.common.base;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import xyz.npgw.test.common.ApiContextUtils;
import xyz.npgw.test.common.BrowserFactory;
import xyz.npgw.test.common.ProjectUtils;
import xyz.npgw.test.common.UserRole;
import xyz.npgw.test.page.AboutBlankPage;

import java.lang.reflect.Method;
import java.nio.file.Paths;

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
//        apiRequestContext = ApiContextUtils.getApiRequestContext(playwright);
        browser = BrowserFactory.getBrowser(playwright);
    }

    @BeforeMethod
    protected void beforeMethod(Method method, ITestResult testResult, Object[] args) {
        Browser.NewContextOptions options = new Browser
                .NewContextOptions()
                .setViewportSize(ProjectUtils.getViewportWidth(), ProjectUtils.getViewportHeight())
                .setBaseURL(ProjectUtils.getBaseUrl());

        if (ProjectUtils.isVideoMode()) {
            options.setRecordVideoDir(Paths.get(ProjectUtils.getArtefactDir()))
                    .setRecordVideoSize(ProjectUtils.getVideoWidth(), ProjectUtils.getVideoHeight());
        }

        context = browser.newContext(options);


        if (ProjectUtils.isTracingMode()) {
            context.tracing().start(new Tracing
                    .StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));
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
        if (userRole == UserRole.GUEST) {
            new AboutBlankPage(page).navigate("/");
        } else {
            new AboutBlankPage(page).navigate("/").loginAsUser(userRole);
        }
    }

    @AfterMethod(alwaysRun = true)
    protected void afterMethod(Method method, ITestResult testResult) {
        ProjectUtils.setTestName(method, testResult);

        if (!testResult.isSuccess() && !ProjectUtils.closeBrowserIfError()) {
            page.pause();
        }

        if (page != null) {
            page.close();
            if (ProjectUtils.isVideoMode() && page.video() != null) {
                page.video().saveAs(ProjectUtils.getVideoFilePath());
                page.video().delete();

                if (!testResult.isSuccess()) {
                    ProjectUtils.saveVideo();
                }
            }
        }

        if (context != null) {
            if (ProjectUtils.isTracingMode()) {
                context.tracing().stop(new Tracing.StopOptions().setPath(ProjectUtils.getTraceFilePath()));

                if (!testResult.isSuccess()) {
                    ProjectUtils.saveTraces();
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
}
