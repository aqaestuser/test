package xyz.npgw.test.common.entity;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.testng.SkipException;

import java.util.concurrent.TimeUnit;

import static xyz.npgw.test.common.util.TestUtils.encode;

@Log4j2
public record User(
        String companyName,
        boolean enabled,
        UserRole userRole,
        String[] merchantIds,
        String email,
        String password) {

    public static void create(APIRequestContext request, User user) {
        APIResponse response = request.post("portal-v1/user/create", RequestOptions.create().setData(user));
        log.info("create user '{}' {} - {}", user.email(), user.companyName(), response.status());
        if (response.status() >= 500) {
            throw new SkipException(response.text());
        }
    }

    public static boolean exists(APIRequestContext request, String email) {
        APIResponse response = request.get("portal-v1/user?email=%s".formatted(encode(email)));
//        log.info("exist user '{}' - {}", email, response.status());
        if (response.status() >= 500) {
            throw new SkipException(response.text());
        }
        return response.ok() && response.text().contains(email);
    }

    public static User[] getAll(APIRequestContext request, String companyName) {
        APIResponse response = request.get("portal-v1/user/list/%s".formatted(encode(companyName)));
        log.info("get all users for company '{}' - {}", companyName, response.status());
        if (response.status() >= 500) {
            throw new SkipException(response.text());
        }
        if (response.status() == 404) {
            return new User[]{};
        }
        return new Gson().fromJson(response.text(), User[].class);
    }

    public static void delete(APIRequestContext request, String email) {
        APIResponse response = request.delete("portal-v1/user?email=%s".formatted(encode(email)));
        log.info("delete user '{}' - {}", email, response.status());
    }

    public static TokenResponse getTokenResponse(APIRequestContext request, Credentials credentials) {
        APIResponse response = request.post("/portal-v1/user/token", RequestOptions.create().setData(credentials));
        log.info("get token '{}' - {}", credentials.email(), response.status());
        if (response.status() >= 500) {
            throw new SkipException(response.text());
        }
        return new Gson().fromJson(response.text(), TokenResponse.class);
    }

    @SneakyThrows
    public static void passChallenge(APIRequestContext request, String email, String password) {
        Credentials credentials = new Credentials(email, password);
        TokenResponse tokenResponse;
        do {
            TimeUnit.SECONDS.sleep(1);
            tokenResponse = getTokenResponse(request, credentials);
        } while (tokenResponse.userChallengeType() == null);
        if (tokenResponse.userChallengeType().equals("NEW_PASSWORD_REQUIRED")) {
            APIResponse response = request.post("/portal-v1/user/challenge",
                    RequestOptions.create().setData(new Challenge(
                            tokenResponse.sessionId(),
                            credentials,
                            tokenResponse.userChallengeType())));

            log.info("pass challenge '{}' - {}", email, response.status());
            if (response.status() >= 500) {
                throw new SkipException(response.text());
            }
        }
        exists(request, email);
    }
}
