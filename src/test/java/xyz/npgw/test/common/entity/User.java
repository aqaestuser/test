package xyz.npgw.test.common.entity;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.Builder;
import lombok.CustomLog;
import lombok.Getter;
import lombok.SneakyThrows;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.util.TestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static xyz.npgw.test.common.util.TestUtils.encode;

@Getter
@Builder
@CustomLog
public class User {

    @Builder.Default
    private String companyName = "super";
    @Builder.Default
    private boolean enabled = true;
    @Builder.Default
    private UserRole userRole = UserRole.SUPER;
    @Builder.Default
    private String[] merchantIds = new String[]{};
    @Builder.Default
    private String email = "";
    @Builder.Default
    private String password = ProjectProperties.getPassword();

    @SneakyThrows
    public static void create(APIRequestContext request, User user) {
        APIResponse response = request.post("portal-v1/user/create", RequestOptions.create().setData(user));
        log.response(response, "create user %s %s".formatted(user.email, user.companyName));

        TestUtils.waitForUserPresence(request, user.email, user.companyName);
    }

    public static boolean exists(APIRequestContext request, String email) {
        APIResponse response = request.get("portal-v1/user?email=%s".formatted(encode(email)));
        log.response(response, "exist user %s".formatted(email));
        return response.ok() && response.text().contains(email);
    }

    public static User getOne(APIRequestContext request, String email) {
        APIResponse response = request.get("portal-v1/user?email=%s".formatted(encode(email)));
        log.response(response, "get user %s".formatted(email));
        return new Gson().fromJson(response.text(), User.class);
    }

    public static User[] getAll(APIRequestContext request, String companyName) {
        APIResponse response = request.get("portal-v1/user/list/%s".formatted(encode(companyName)));
        log.response(response, "get all users for company %s".formatted(companyName));
        return response.status() == 404 ? new User[]{} : new Gson().fromJson(response.text(), User[].class);
    }

    public static void addMerchant(APIRequestContext request, String email, String merchantId) {
        User user = getOne(request, email);
        List<String> merchantIdsList = new ArrayList<>(Arrays.asList(user.merchantIds));
        merchantIdsList.add(merchantId);
        APIResponse response = request.patch("/portal-v1/user?email=%s".formatted(encode(email)),
                RequestOptions.create().setData(new User(
                        user.companyName,
                        user.enabled,
                        user.userRole,
                        merchantIdsList.toArray(String[]::new),
                        user.email,
                        user.password)));
        log.response(response, "update user %s".formatted(email));
    }

    public static void removeMerchant(APIRequestContext request, String email, String merchantId) {
        User user = getOne(request, email);
        List<String> merchantIdsList = new ArrayList<>(Arrays.asList(user.merchantIds));
        merchantIdsList.remove(merchantId);
        APIResponse response = request.patch("/portal-v1/user?email=%s".formatted(encode(email)),
                RequestOptions.create().setData(new User(
                        user.companyName,
                        user.enabled,
                        user.userRole,
                        merchantIdsList.toArray(String[]::new),
                        user.email,
                        user.password)));
        log.response(response, "update user %s".formatted(email));
    }

    public static void delete(APIRequestContext request, String email) {
        APIResponse response = request.delete("/portal-v1/user?email=%s".formatted(encode(email)));
        log.response(response, "delete user %s".formatted(email));
    }

    public static TokenResponse getTokenResponse(APIRequestContext request, Credentials credentials) {
        APIResponse response = request.post("/portal-v1/user/token", RequestOptions.create().setData(credentials));
        log.response(response, "get user token %s".formatted(credentials.email()));
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

    public Credentials getCredentials() {
        return new Credentials(email, password);
    }
}
