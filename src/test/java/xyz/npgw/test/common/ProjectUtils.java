package xyz.npgw.test.common;

import io.qameta.allure.Allure;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Log4j2
public final class ProjectUtils {

    private static final String PREFIX_PROP = "local.";
    private static final String EMAIL = ".email";
    private static final String PASSWORD = ".password";

    private static final String BASE_URL = PREFIX_PROP + "baseURL";
    private static final String BROWSER_TYPE = PREFIX_PROP + "browserType";
    private static final String HEADLESS_MODE = PREFIX_PROP + "headlessMode";
    private static final String VIEWPORT_WIDTH = PREFIX_PROP + "viewportWidth";
    private static final String VIEWPORT_HEIGHT = PREFIX_PROP + "viewportHeight";
    private static final String SLOW_MO_MODE = PREFIX_PROP + "slowMoMode";
    private static final String IS_TRACING_MODE = PREFIX_PROP + "isTracingMode";

    private static final String IS_VIDEO_MODE = PREFIX_PROP + "isVideoMode";
    private static final String VIDEO_WIDTH = PREFIX_PROP + "videoHeight";
    private static final String VIDEO_HEIGHT = PREFIX_PROP + "videoWidth";

    private static final String CLOSE_BROWSER_IF_ERROR = PREFIX_PROP + "closeBrowserIfError";
    private static final String ARTEFACT_DIR = PREFIX_PROP + "artefactDir";

    private static final String ENV_APP_OPTIONS = "APP_OPTIONS";

    private static final Properties properties;
    static {
        properties = new Properties();
        if (isServerRun()) {
            if (System.getenv(ENV_APP_OPTIONS) != null) {
                for (String option : System.getenv(ENV_APP_OPTIONS).split(";")) {
                    String[] optionArr = option.split("=");
                    properties.setProperty(PREFIX_PROP + optionArr[0], optionArr[1]);
                }
            }
        } else {
            try (InputStream inputStream = Files.newInputStream(Paths.get("./config/.env"))) {
                properties.load(inputStream);
            } catch (IOException e) {
                log.error("The '.env' file not found in ./config/ directory.");
                log.error("You need to create it from ./config/.env.TEMPLATE file.");
                throw new RuntimeException(e);
            }
        }
    }

    @Getter(AccessLevel.PRIVATE)
    private static String testName;

    private ProjectUtils() {
        throw new UnsupportedOperationException();
    }

    private static boolean isServerRun() {
        return System.getenv("CI_RUN") != null;
    }

    public static int getViewportWidth() {
        return Integer.parseInt(properties.getProperty(VIEWPORT_WIDTH, "1920"));
    }

    public static int getViewportHeight() {
        return Integer.parseInt(properties.getProperty(VIEWPORT_HEIGHT, "1080"));
    }

    public static int getVideoWidth() {
        return Integer.parseInt(properties.getProperty(VIDEO_WIDTH, "1280"));
    }

    public static int getVideoHeight() {
        return Integer.parseInt(properties.getProperty(VIDEO_HEIGHT, "720"));
    }

    public static String getBaseUrl() {
        return properties.getProperty(BASE_URL, "");
    }

    public static String getBrowserType() {
        return properties.getProperty(BROWSER_TYPE.toUpperCase(), "CHROMIUM");
    }

    public static String getSuperEmail() {
        return properties.getProperty(PREFIX_PROP + UserRole.SUPER + EMAIL, "");
    }

    public static String getSuperPassword() {
        return properties.getProperty(PREFIX_PROP + UserRole.SUPER + PASSWORD, "");
    }

    public static String getAdminEmail() {
        return properties.getProperty(PREFIX_PROP + UserRole.ADMIN + EMAIL, "");
    }

    public static String getAdminPassword() {
        return properties.getProperty(PREFIX_PROP + UserRole.ADMIN + PASSWORD, "");
    }

    public static String getUserEmail() {
        return properties.getProperty(PREFIX_PROP + UserRole.USER + EMAIL, "");
    }

    public static String getUserPassword() {
        return properties.getProperty(PREFIX_PROP + UserRole.USER + PASSWORD, "");
    }

    public static boolean isHeadlessMode() {
        return Boolean.parseBoolean(properties.getProperty(HEADLESS_MODE, "true"));
    }

    public static Double getSlowMoMode() {
        return Double.valueOf(properties.getProperty(SLOW_MO_MODE, "0"));
    }

    public static boolean isTracingMode() {
        return Boolean.parseBoolean(properties.getProperty(IS_TRACING_MODE, "true"));
    }

    public static boolean isVideoMode() {
        return Boolean.parseBoolean(properties.getProperty(IS_VIDEO_MODE, "true"));
    }

    public static boolean closeBrowserIfError() {
        return Boolean.parseBoolean(properties.getProperty(CLOSE_BROWSER_IF_ERROR, "true"));
    }

    public static String getArtefactDir() {
        return properties.getProperty(ARTEFACT_DIR, "target/artefact");
    }

    public static void saveVideo() {
        try {
            Allure.getLifecycle().addAttachment(
                    "video",
                    "video/webm",
                    "webm",
                    Files.readAllBytes(getVideoFilePath()));
        } catch (IOException e) {
            log.error("Add video to allure failed: {}", e.getMessage());
        }
    }

    public static void saveTraces() {
        try {
            Allure.getLifecycle().addAttachment(
                    "tracing",
                    "archive/zip",
                    "zip",
                    Files.readAllBytes(getTraceFilePath()));
        } catch (IOException e) {
            log.error("Add traces to allure failed: {}", e.getMessage());
        }
    }

    public static Path getVideoFilePath() {
        return  Paths.get(getArtefactDir(), getBrowserType(), getTestName() + ".webm");
    }

    public static Path getTraceFilePath() {
        return Paths.get(getArtefactDir(), getBrowserType(), getTestName() + ".zip");
    }

    public static void setTestName(Method method, ITestResult testResult) {
        String testMethodName = method.getDeclaringClass().getSimpleName() + "/" + method.getName();
        if (!method.getAnnotation(Test.class).dataProvider().isEmpty()) {
            testMethodName = "%s(%d)".formatted(testMethodName, testResult.getMethod().getCurrentInvocationCount());
        }
        testName = testMethodName + new SimpleDateFormat("_MMdd_HHmmss").format(new Date());
    }
}
