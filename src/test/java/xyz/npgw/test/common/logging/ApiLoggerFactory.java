package xyz.npgw.test.common.logging;

public class ApiLoggerFactory {

    public static ApiLogger getLogger(String className) {
        return new ApiLogger(className);
    }
}
