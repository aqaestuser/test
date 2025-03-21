package runner;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Tracing;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PlaywrightOptions {
    private static final VideoSettings VIDEO = new VideoSettings(1280, 720, Paths.get("target/video"));

    public static BrowserType.LaunchOptions browserOptions() {
        return new BrowserType.LaunchOptions()
                .setHeadless(ProjectProperties.isHeadlessMode())
                .setSlowMo(ProjectProperties.getSlowMoMode());
    }

    public static Browser.NewContextOptions contextOptions() {
        Browser.NewContextOptions options = new Browser.NewContextOptions()
                .setViewportSize(ProjectProperties.getViewportWidth(), ProjectProperties.getViewportHeight())
                .setBaseURL(ProjectProperties.getBaseUrl());

        if (ProjectProperties.isVideoMode()) {
            options.setRecordVideoDir(VIDEO.videoDirPath)
                    .setRecordVideoSize(VIDEO.width, VIDEO.height);
        }
        return options;
    }

    public static Tracing.StartOptions tracingStartOptions() {
        return new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true);
    }

    public static Tracing.StopOptions tracingStopOptions(Path tracePath) {
        return new Tracing.StopOptions().setPath(tracePath);
    }

    private record VideoSettings(int width, int height, Path videoDirPath) {
    }
}
