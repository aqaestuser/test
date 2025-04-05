package xyz.npgw.test.common;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

@Log4j2
public class ProjectProperties {

    private static final String ENV_ACCESS_OPTIONS = "ACCESS_OPTIONS";
    private static final String ENV_BROWSER_OPTIONS = "BROWSER_OPTIONS";

    private static final Properties properties;
    static {
        properties = new Properties();
        if (isServerRun()) {
            loadPropertiesFromEnv(ENV_ACCESS_OPTIONS);
            loadPropertiesFromEnv(ENV_BROWSER_OPTIONS);
        } else {
            loadPropertiesFromFile("access.properties");
            loadPropertiesFromFile("browser.properties");
        }
    }

    private static void loadPropertiesFromEnv(String envKey) {
        String envValue = System.getenv(envKey);
        if (envValue == null || envValue.isEmpty()) {
            log.error("The environment key {} not found.", envKey);
            System.exit(3);
        }

        Arrays.stream(envValue.split(";"))
                .map(option -> option.split("=", 2))
                .filter(pair -> pair.length == 2 && !pair[0].isBlank())
                .forEach(pair -> properties.setProperty(pair[0].trim(), pair[1].trim()));
    }

    private static void loadPropertiesFromFile(String fileName) {
        try (InputStream inputStream = ProjectProperties.class.getClassLoader().getResourceAsStream(fileName)) {
            properties.load(inputStream);
        } catch (NullPointerException | IOException e) {
            log.error("The file {} not found.", fileName);
            log.error("You need to create it from {}.TEMPLATE file.", fileName);
            System.exit(3);
        }
    }

    public static String getPropertyValue(String propertyName) {
        if (!properties.containsKey(propertyName) || properties.getProperty(propertyName) == null
                || properties.getProperty(propertyName).trim().isEmpty()) {
            log.error("Property '{}' does not exist or it's value is invalid.", propertyName);
        }
        return properties.getProperty(propertyName).trim();
    }

    public static boolean getPropertyValueAsBoolean(String propertyName) {
        if (getPropertyValue(propertyName).equalsIgnoreCase("true")
                || getPropertyValue(propertyName).equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(getPropertyValue(propertyName));
        } else {
            log.error("Property '{}' doesn't have [true, false] value.", propertyName);
            log.error("The default value 'true' for '{}' has been set.", propertyName);
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

    public static boolean isTracingMode() {
        return getPropertyValueAsBoolean("tracingMode");
    }

    public static boolean isVideoMode() {
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
