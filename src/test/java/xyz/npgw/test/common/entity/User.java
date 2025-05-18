package xyz.npgw.test.common.entity;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.UserRole;

import static xyz.npgw.test.common.util.TestUtils.encode;

@Log4j2
public record User(
        String companyName,
        boolean enabled,
        UserRole userRole,
        String[] merchantIds,
        String email,
        String password) {

    public static User newSystemAdmin(String email, String password) {
        return new User("super", true, UserRole.SUPER, new String[]{}, email, password);
    }

    public static User newSystemAdmin(String email) {
        return newSystemAdmin(email, ProjectProperties.getUserPassword());
    }

    public static User newCompanyAdmin(String companyName, String email, String password) {
        return new User(companyName, true, UserRole.ADMIN, new String[]{}, email, password);
    }

    public static User newCompanyAdmin(String companyName, String email) {
        return newCompanyAdmin(companyName, email, ProjectProperties.getUserPassword());
    }

    public static User newCompanyAnalyst(String companyName, String[] merchantIds, String email, String password) {
        return new User(companyName, true, UserRole.USER, merchantIds, email, password);
    }

    public static User newCompanyAnalyst(String companyName, String[] merchantIds, String email) {
        return newCompanyAnalyst(companyName, merchantIds, email, ProjectProperties.getUserPassword());
    }

    public static void create(APIRequestContext request, User user) {
        APIResponse response = request.post("portal-v1/user/create", RequestOptions.create().setData(user));
        log.info("create company admin '{}' - {} {}", user.email(), response.status(), response.text());
    }

    public static void delete(APIRequestContext request, User user) {
        delete(request, user.email());
    }

    public static void delete(APIRequestContext request, String email) {
        APIResponse response = request.delete("portal-v1/user?email=%s".formatted(encode(email)));
        log.info("delete user '{}' - {} {}", email, response.status(), response.text());
    }

    @Override
    public String toString() {
        return "User: %s %s".formatted(email, userRole);
    }
}
