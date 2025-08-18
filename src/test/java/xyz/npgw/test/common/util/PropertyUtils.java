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

    private static final String CI_RUN = "CI_RUN";
    private static final String DOCKER_RUN = "DOCKER_RUN";

    static {
        properties = new Properties();
        if ((System.getenv(CI_RUN) == null) && (System.getenv(DOCKER_RUN) == null)) {
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

    public static String getValue(String value) {
        return properties.getProperty(value, System.getenv(value.replace('.', '_').toUpperCase()));
    }
}
