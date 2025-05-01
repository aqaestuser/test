package xyz.npgw.test.common;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.common.util.BusinessUnit;
import xyz.npgw.test.common.util.Company;
import xyz.npgw.test.common.util.User;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Log4j2
public class TestUtils {

    public static void deleteUser(APIRequestContext request, User user) {
        request.delete(
                "portal-v1/user?email=%s".formatted(encode(user.email())));
    }

    public static void createBusinessUnit(APIRequestContext request, String companyName, String merchantName) {
        request.post(
                "portal-v1/company/%s/merchant".formatted(encode(companyName)),
                RequestOptions.create().setData(new BusinessUnit(merchantName)));
    }

    public static void createBusinessUnitIfNeeded(APIRequestContext request, User user) {
        createCompanyIfNeeded(request, user);
        for (String merchantName : user.merchantIds()) {
            if (!getBusinessUnit(request, user.companyName(), merchantName)) {
                createBusinessUnit(request, user.companyName(), merchantName);
            }
        }
    }

    public static void createBusinessUnitIfNeeded(APIRequestContext request,
                                                  String companyName,
                                                  String[] businessUnits) {
        createCompanyIfNeeded(request, companyName);
        for (String merchantName : businessUnits) {
            if (!getBusinessUnit(request, companyName, merchantName)) {
                createBusinessUnit(request, companyName, merchantName);
            }
        }
    }

    public static void createCompany(APIRequestContext request, User user) {
        createCompany(request, user.companyName());
    }

    public static void createCompany(APIRequestContext request, String companyName) {
        request.post("/portal-v1/company", RequestOptions.create().setData(new Company(companyName)));
    }

    public static void createCompanyIfNeeded(APIRequestContext request, User user) {
        createCompanyIfNeeded(request, user.companyName());
    }

    public static void createCompanyIfNeeded(APIRequestContext request, String companyName) {
        if (!getCompany(request, companyName)) {
            createCompany(request, companyName);
        }
    }

    public static void deleteCompany(APIRequestContext request, String companyName) {
        request.delete(
                "portal-v1/company/%s".formatted(encode(companyName)));
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

    private static boolean getCompany(APIRequestContext request, String companyName) {
        APIResponse response = request.get(
                "portal-v1/company/%s".formatted(encode(companyName)));
        return response.ok() && response.text().contains(companyName);
    }

    private static boolean getBusinessUnit(APIRequestContext request, String companyName, String merchantName) {
        APIResponse response = request.get(
                "portal-v1/company/%s/merchant".formatted(encode(companyName)));
        return response.ok() && response.text().contains(merchantName);
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
