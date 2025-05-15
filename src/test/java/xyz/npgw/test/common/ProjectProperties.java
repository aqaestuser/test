package xyz.npgw.test.common;

import com.microsoft.playwright.options.ColorScheme;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.common.entity.UserRole;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

@Log4j2
public final class ProjectProperties {

    private static final String PREFIX_PROP = "local.";
    private static final String EMAIL = ".email";
    private static final String PASSWORD = ".password";

    private static final String CI_RUN = "CI_RUN";

    private static final String BASE_URL = PREFIX_PROP + "baseURL";
    private static final String BROWSER_TYPE = PREFIX_PROP + "browserType";
    private static final String HEADLESS_MODE = PREFIX_PROP + "headlessMode";
    private static final String SLOW_MO_MODE = PREFIX_PROP + "slowMoMode";
    private static final String VIEWPORT_WIDTH = PREFIX_PROP + "viewportWidth";
    private static final String VIEWPORT_HEIGHT = PREFIX_PROP + "viewportHeight";
    private static final String TRACING_MODE = PREFIX_PROP + "tracingMode";

    private static final String VIDEO_MODE = PREFIX_PROP + "videoMode";
    private static final String VIDEO_WIDTH = PREFIX_PROP + "videoHeight";
    private static final String VIDEO_HEIGHT = PREFIX_PROP + "videoWidth";

    private static final String CLOSE_BROWSER_IF_ERROR = PREFIX_PROP + "closeBrowserIfError";
    private static final String ARTEFACT_DIR = PREFIX_PROP + "artefactDir";
    private static final String DEFAULT_TIMEOUT = PREFIX_PROP + "defaultTimeout";
    private static final String FAIL_FAST = PREFIX_PROP + "failFast";
    private static final String SKIP_MODE = PREFIX_PROP + "skipMode";
    private static final String ADDITIONAL_RETRIES = PREFIX_PROP + "additionalRetries";
    private static final String COLOR_SCHEME = PREFIX_PROP + "colorScheme";
    private static final String DEBUG = PREFIX_PROP + "DEBUG";

    private static final String ENV_APP_OPTIONS = "APP_OPTIONS";

    private static final Properties properties;

    static {
        properties = new Properties();
        if (System.getenv(CI_RUN) != null) {
            if (System.getenv(ENV_APP_OPTIONS) != null) {
                for (String option : System.getenv(ENV_APP_OPTIONS).split(";")) {
                    String[] optionArr = option.trim().split("=");
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

    private ProjectProperties() {
        throw new UnsupportedOperationException();
    }

    public static String getBaseUrl() {
        return properties.getProperty(BASE_URL, "");
    }

    public static String getBrowserType() {
        return properties.getProperty(BROWSER_TYPE.toUpperCase(), "CHROMIUM");
    }

    public static boolean isHeadlessMode() {
        return Boolean.parseBoolean(properties.getProperty(HEADLESS_MODE, "true"));
    }

    public static Double getSlowMoMode() {
        return Double.valueOf(properties.getProperty(SLOW_MO_MODE, "0"));
    }

    public static int getViewportWidth() {
        return Integer.parseInt(properties.getProperty(VIEWPORT_WIDTH, "1920"));
    }

    public static int getViewportHeight() {
        return Integer.parseInt(properties.getProperty(VIEWPORT_HEIGHT, "953"));
    }

    public static boolean isTracingMode() {
        return Boolean.parseBoolean(properties.getProperty(TRACING_MODE, "true"));
    }

    public static void setTracingMode(boolean mode) {
        properties.setProperty(TRACING_MODE, String.valueOf(mode));
    }

    public static boolean isVideoMode() {
        return Boolean.parseBoolean(properties.getProperty(VIDEO_MODE, "true"));
    }

    public static void setVideoMode(boolean mode) {
        properties.setProperty(VIDEO_MODE, String.valueOf(mode));
    }

    public static int getVideoWidth() {
        return Integer.parseInt(properties.getProperty(VIDEO_WIDTH, "1280"));
    }

    public static int getVideoHeight() {
        return Integer.parseInt(properties.getProperty(VIDEO_HEIGHT, "720"));
    }

    public static boolean closeBrowserIfError() {
        return Boolean.parseBoolean(properties.getProperty(CLOSE_BROWSER_IF_ERROR, "true"));
    }

    public static String getArtefactDir() {
        return properties.getProperty(ARTEFACT_DIR, "target/artefact");
    }

    public static double getDefaultTimeout() {
        return Double.parseDouble(properties.getProperty(DEFAULT_TIMEOUT, "5000"));
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

    public static boolean isFailFast() {
        return Boolean.parseBoolean(properties.getProperty(FAIL_FAST, "false"));
    }

    public static boolean isSkipMode() {
        return Boolean.parseBoolean(properties.getProperty(SKIP_MODE, "false"));
    }

    public static void setSkipMode(boolean mode) {
        properties.setProperty(SKIP_MODE, String.valueOf(mode));
    }

    public static int getAdditionalRetries() {
        return Integer.parseInt(properties.getProperty(ADDITIONAL_RETRIES, "0"));
    }

    public static ColorScheme getColorScheme() {
        return ColorScheme.valueOf(properties.getProperty(COLOR_SCHEME, "DARK").toUpperCase());
    }

    public static Map<String, String> getEnv() {
        String debug = properties.getProperty(DEBUG, "");
        return debug.isEmpty() ? Map.of() : Map.of("DEBUG", debug);
    }
}
