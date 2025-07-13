package xyz.npgw.test.common.logging;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.PlaywrightException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.npgw.test.common.entity.ApiError;

public class ApiLogger {

    private final Logger log;

    public ApiLogger(String className) {
        this.log = LogManager.getLogger(className);
    }

    public void response(APIResponse response, String message) {
        switch (response.status() / 100) {
            case 2:
                log.debug("{} {}", message, response.status());
                break;
            case 4:
                String errorMessage = response.text();
                try {
                    errorMessage = new Gson().fromJson(response.text(), ApiError.class).message();
                } catch (JsonSyntaxException ignored) {
                    // no handling
                }
                log.warn("{} {} - {}", message, response.status(), errorMessage);
                break;
            case 5:
                log.error("{} {} - {}", message, response.status(), response.text());
                throw new PlaywrightException(response.text());
            default:
                log.debug("{} {} - {}", message, response.status(), response.text());
        }
    }
}
