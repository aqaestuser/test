package runner;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public abstract class BaseTest {

    private Playwright playwright;
    private Browser browser;
    private String browserType;
    private BrowserContext context;
    private Page page;

    @Parameters("browserType")
    @BeforeClass
    protected void launchBrowser(@Optional("CHROMIUM") String browserType) {
        this.browserType = browserType;
        try {
            playwright = Playwright.create();
            browser = BrowserManager.getBrowser(playwright, browserType);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    @BeforeMethod
    protected void createContextAndPage(Method method, ITestResult testResult) {
        try {
            context = browser.newContext(PlaywrightOptions.contextOptions());
            context.tracing().start(PlaywrightOptions.tracingStartOptions());
            page = context.newPage();
            BaseUtils.navigateToBaseURL(page);
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    @AfterMethod(alwaysRun = true)
    protected void closeContext(Method method, ITestResult testResult) {
        page.close();
        context.tracing().stop(PlaywrightOptions.tracingStopOptions(page, browserType, method, testResult));
        context.close();
    }

    @AfterClass(alwaysRun = true)
    protected void closeBrowser() {
        browser.close();
        playwright.close();
    }

    protected Page getPage() {
        return page;
    }
}
