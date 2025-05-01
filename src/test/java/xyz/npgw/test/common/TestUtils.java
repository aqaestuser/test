package xyz.npgw.test.common;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.SystemConfig;
import xyz.npgw.test.common.util.BusinessUnit;
import xyz.npgw.test.common.util.Company;
import xyz.npgw.test.common.util.User;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class TestUtils {

    public static void deleteUser(APIRequestContext request, User user) {
        APIResponse response = request.delete("portal-v1/user?email=%s".formatted(encode(user.email())));
        log.info("delete user '{}' - {} {}", user.email(), response.status(), response.text());
    }

    public static void createBusinessUnit(APIRequestContext request, String companyName, String businessUnitName) {
        APIResponse response = request.post("portal-v1/company/%s/merchant".formatted(encode(companyName)),
                RequestOptions.create().setData(new BusinessUnit(businessUnitName)));
        log.info("create business unit '{}' - {} {}", businessUnitName, response.status(), response.text());
    }

    public static void createBusinessUnitsIfNeeded(APIRequestContext request, User user) {
        createCompanyIfNeeded(request, user);
        for (String businessUnitName : user.merchantIds()) {
            if (!existsBusinessUnit(request, user.companyName(), businessUnitName)) {
                createBusinessUnit(request, user.companyName(), businessUnitName);
            }
        }
    }

    public static void createCompany(APIRequestContext request, User user) {
        createCompany(request, user.companyName());
    }

    public static void createCompany(APIRequestContext request, String companyName) {
        APIResponse response = request.post("/portal-v1/company",
                RequestOptions.create().setData(new Company(companyName)));
        log.info("create company '{}' - {} {}", companyName, response.status(), response.text());
    }

    public static void createCompanyIfNeeded(APIRequestContext request, User user) {
        createCompanyIfNeeded(request, user.companyName());
    }

    public static void createCompanyIfNeeded(APIRequestContext request, String companyName) {
        if (!existsCompany(request, companyName)) {
            createCompany(request, companyName);
        }
    }

    public static void deleteCompany(APIRequestContext request, String companyName) {
        APIResponse response = request.delete("portal-v1/company/%s".formatted(encode(companyName)));
        log.info("delete company '{}' - {} {}", companyName, response.status(), response.text());
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

    private static boolean existsCompany(APIRequestContext request, String companyName) {
        APIResponse response = request.get("portal-v1/company/%s".formatted(encode(companyName)));
        log.info("get company '{}' - {} {}", companyName, response.status(), response.text());
        return response.ok() && response.text().contains(companyName);
    }

    private static boolean existsBusinessUnit(APIRequestContext request, String companyName, String businessUnitName) {
        APIResponse response = request.get("portal-v1/company/%s/merchant".formatted(encode(companyName)));
        log.info("get business unit '{}' - {} {}", businessUnitName, response.status(), response.text());
        return response.ok() && response.text().contains(businessUnitName);
    }

    public static void createAcquirer(APIRequestContext request, String acquirerName) {
        Acquirer acquirer = new Acquirer("NGenius", "et", new SystemConfig(), acquirerName, new String[]{"USD"}, true);
        APIResponse response = request.post("/portal-v1/acquirer", RequestOptions.create().setData(acquirer));
        log.info("create acquirer '{}' - {} {}", acquirerName, response.status(), response.text());
    }

    public static boolean getAcquirer(APIRequestContext request, String acquirerName) {
        APIResponse response = request.get("portal-v1/acquirer/%s".formatted(encode(acquirerName)));
        log.info("get acquirer '{}' - {} {}", acquirerName, response.status(), response.text());

        return response.ok();
    }

    public static void deleteAcquirer(APIRequestContext request, String acquirerName) {
        APIResponse response = request.delete(
                "/portal-v1/acquirer/" + acquirerName);

        if (response.ok()) {
            log.info("Acquirer {} successfully deleted", acquirerName);
        } else {
            log.info("{}", response.text());
        }
    }
}
