package xyz.npgw.test.common.logging;

import com.microsoft.playwright.APIResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.SkipException;

public class ApiLogger {

    private final Logger log;

    public ApiLogger(String className) {
        this.log = LogManager.getLogger(className);
    }

    public void response(APIResponse response, String message) {
        switch (response.status() / 100) {
            case 2:
                log.info("{} {}", message, response.status());
                break;
            case 4:
                log.warn("{} {} - {}", message, response.status(), response.text());
                break;
            case 5:
                log.error("{} {} - {}", message, response.status(), response.text());
                throw new SkipException(response.text());
            default:
                log.debug("{} {} - {}", message, response.status(), response.text());
        }
    }
}
