package runner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class ProjectProperties {
    private static final Logger LOGGER = LogManager.getLogger(ProjectProperties.class.getName());
    private static final String RESOURCE_PATH = "./src/test/resources/";
    private static final String ENV_ACCESS_OPTIONS = "ACCESS_OPTIONS";
    private static final String ENV_BROWSER_OPTIONS = "BROWSER_OPTIONS";
    private static Properties properties;

    static {
        initProperties();
    }

    private static void loadPropertiesFromEnv(String envKey) {
        String envValue = System.getenv(envKey);
        if (envValue == null || envValue.isEmpty()) return;

        Arrays.stream(envValue.split(";"))
                .map(option -> option.split("=", 2))
                .filter(pair -> pair.length == 2 && !pair[0].isBlank())
                .forEach(pair -> properties.setProperty(pair[0].trim(), pair[1].trim()));
    }

    private static void loadPropertiesFromFile(String fileName) {
        try (FileInputStream fileInputStream = new FileInputStream(RESOURCE_PATH + fileName)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            LOGGER.error("The \u001B[31m{}\u001B[0m file not found.", fileName);
            LOGGER.error("You need to create it from {}.TEMPLATE file.", fileName);
            System.exit(3);
        }
    }

    private static void initProperties() {
        properties = new Properties();
        if (isServerRun()) {
            loadPropertiesFromEnv(ENV_ACCESS_OPTIONS);
            loadPropertiesFromEnv(ENV_BROWSER_OPTIONS);
        } else {
            loadPropertiesFromFile("access.properties");
            loadPropertiesFromFile("browser.properties");
        }
    }

    public static String getPropertyValue(String propertyName) {
        if (!properties.containsKey(propertyName) || properties.getProperty(propertyName) == null
                || properties.getProperty(propertyName).trim().isEmpty()) {
            LOGGER.error("Property '{}' does not exist or it's value is invalid.", propertyName);
        }
        return properties.getProperty(propertyName).trim();
    }

    public static boolean getPropertyValueAsBoolean(String propertyName) {
        if (getPropertyValue(propertyName).equalsIgnoreCase("true")
                || getPropertyValue(propertyName).equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(getPropertyValue(propertyName));
        } else {
            LOGGER.error("Property '{}' doesn't have [true, false] value.", propertyName);
            LOGGER.error("The default value 'true' for '{}' hase been set.", propertyName);
            return true;
        }
    }

    static boolean isServerRun() {
        return System.getenv("CI_RUN") != null;
    }

    static boolean isHeadlessMode() {
        return getPropertyValueAsBoolean("headlessMode");
    }

    static Double getSlowMoMode() {
        return Double.valueOf(getPropertyValue("slowMoMode"));
    }

    static int getViewportWidth() {
        return Integer.parseInt(getPropertyValue("viewportWidth"));
    }

    static int getViewportHeight() {
        return Integer.parseInt(getPropertyValue("viewportHeight"));
    }

    static boolean isTracingMode() {
        return getPropertyValueAsBoolean("tracingMode");
    }

    static boolean isVideoMode() {
        return getPropertyValueAsBoolean("videoMode");
    }

    public static String getBaseUrl() {
        return getPropertyValue("baseURL");
    }

    public static String getUserEmail() {
        return getPropertyValue("userEmail");
    }

    public static String getUserPassword() {
        return getPropertyValue("userPassword");
    }
}
