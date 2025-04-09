package xyz.npgw.test.common;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@Log4j2
public class ApiContextUtils {

    public static APIRequestContext getApiRequestContext(Playwright playwright) {
        APIResponse tokenResponse = playwright
                .request()
                .newContext()
                .post(ProjectProperties.getBaseUrl() + "/portal-v1/user/token",
                        RequestOptions.create().setData(Map.of(
                                "email", ProjectProperties.getSuperEmail(),
                                "password", ProjectProperties.getSuperPassword())));

        if (tokenResponse.ok()) {
            return playwright
                    .request()
                    .newContext(new APIRequest
                            .NewContextOptions()
                            .setBaseURL(ProjectProperties.getBaseUrl())
                            .setExtraHTTPHeaders(Map.of("Authorization", "Bearer %s".formatted(new Gson()
                                    .fromJson(tokenResponse.text(), TokenResponse.class)
                                    .token()
                                    .idToken))));
        } else {
            String message = "Retrieve API idToken failed: %s".formatted(tokenResponse.text());
            log.error(message);
            throw new RuntimeException(message);
        }
    }

    private record Token(String accessToken, int expiresIn, String idToken, String refreshToken, String tokenType) {
    }

    private record TokenResponse(String userChallengeType, Token token) {
    }
}
