package xyz.npgw.test.common.util;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Log4j2
public class PropertyUtils {

    private static final Properties properties;

    private static final String PREFIX_PROP = "local.";

    private static final String CI_RUN = "CI_RUN";
    private static final String DOCKER_RUN = "DOCKER_RUN";
    private static final String ENV_APP_OPTIONS = "APP_OPTIONS";

    static {
        properties = new Properties();
        if ((System.getenv(CI_RUN) != null) || (System.getenv(DOCKER_RUN) != null)) {
            if (System.getenv(ENV_APP_OPTIONS) != null) {
                for (String option : System.getenv(ENV_APP_OPTIONS).split(";")) {
                    String[] optionArr = option.trim().split("=");
                    properties.setProperty(PREFIX_PROP + optionArr[0], optionArr[1]);
                }
            }
        } else {
            String configPath = System.getProperty("configPath", ".properties");
            try (InputStream inputStream = Files.newInputStream(Paths.get(configPath))) {
                properties.load(inputStream);
            } catch (IOException e) {
                log.error("The '.properties' file not found in project directory.");
                log.error("You need to create it from .properties.TEMPLATE file.");
                throw new RuntimeException(e);
            }
        }
    }

    static String getValue(String propName) {
//        return System.getenv(propName.replace('.', '_').toUpperCase());
        return properties.getProperty(propName);
    }
}
