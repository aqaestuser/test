package runner;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import org.testng.ITestResult;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PlaywrightOptions {

    private static final String RUN_DIR = ProjectUtils.setNameFromDateAndTime();
    private static final String TRACE_DIR = "target/testTracing/";
    public static final String TRACE_RUN_DIR = TRACE_DIR + RUN_DIR + "/";
    private static final String VIDEO_DIR = "target/video/";
    public static final String VIDEO_RUN_DIR = VIDEO_DIR + RUN_DIR + "/";

    public static BrowserType.LaunchOptions browserOptions() {
        return new BrowserType.LaunchOptions()
                .setHeadless(ProjectProperties.isHeadlessMode())
                .setSlowMo(ProjectProperties.getSlowMoMode());
    }

    public static Browser.NewContextOptions contextOptions() {
        return new Browser.NewContextOptions()
                .setViewportSize(ProjectProperties.isViewportWidth(), ProjectProperties.getViewportHeight())
                .setBaseURL(ProjectProperties.getBaseUrl())
                .setRecordVideoDir(Paths.get(VIDEO_RUN_DIR))
                .setRecordVideoSize(1280, 720);
    }

    public static Tracing.StartOptions tracingStartOptions() {
        return new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true);
    }

    public static Tracing.StopOptions tracingStopOptions(Page page, String browserType, Method method, ITestResult testResult) {
        String testName = ProjectUtils.getTestClassMethodNameWithInvocationCount(method, testResult);

        Tracing.StopOptions tracingStopOptions = null;
        if (!testResult.isSuccess()) {
            if (ProjectProperties.isTracingMode()) {
                tracingStopOptions = new Tracing.StopOptions()
                        .setPath(Paths.get(TRACE_RUN_DIR + browserType + "/" + testName + ".zip"));
            }
            if (ProjectProperties.isVideoMode()) {
                page.video().saveAs(Paths.get(VIDEO_RUN_DIR + browserType + "/" + testName + ".webm"));
            }
        }
        page.video().delete();
        try {
            Files.deleteIfExists(Paths.get(VIDEO_RUN_DIR));
            Files.deleteIfExists(Paths.get(VIDEO_DIR));
        } catch (IOException ignored) {
        }
        return tracingStopOptions;
    }
}
