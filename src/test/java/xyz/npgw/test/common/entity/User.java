package xyz.npgw.test.common.entity;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.CustomLog;
import lombok.SneakyThrows;
import xyz.npgw.test.common.util.TestUtils;

import java.util.concurrent.TimeUnit;

import static xyz.npgw.test.common.util.TestUtils.encode;

@CustomLog
public record User(
        String companyName,
        boolean enabled,
        UserRole userRole,
        String[] merchantIds,
        String email,
        String password) {

    @SneakyThrows
    public static void create(APIRequestContext request, User user) {
        APIResponse response = request.post("portal-v1/user/create", RequestOptions.create().setData(user));
        log.response(response, "create user %s %s".formatted(user.email(), user.companyName()));

        TestUtils.waitForUserPresence(request, user.email, user.companyName);
    }

    public static boolean exists(APIRequestContext request, String email) {
        APIResponse response = request.get("portal-v1/user?email=%s".formatted(encode(email)));
        log.response(response, "exist user %s".formatted(email));
        return response.ok() && response.text().contains(email);
    }

    public static User[] getAll(APIRequestContext request, String companyName) {
        APIResponse response = request.get("portal-v1/user/list/%s".formatted(encode(companyName)));
        log.response(response, "get all users for company %s".formatted(companyName));
        return response.status() == 404 ? new User[]{} : new Gson().fromJson(response.text(), User[].class);
    }

    public static void delete(APIRequestContext request, String email) {
        APIResponse response = request.delete("portal-v1/user?email=%s".formatted(encode(email)));
        log.response(response, "delete user %s".formatted(email));
    }

    public static TokenResponse getTokenResponse(APIRequestContext request, Credentials credentials) {
        APIResponse response = request.post("/portal-v1/user/token", RequestOptions.create().setData(credentials));
        log.response(response, "get token %s".formatted(credentials.email()));
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

            log.response(response, "pass challenge %s".formatted(email));
        }
        while (!exists(request, email)) {
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
