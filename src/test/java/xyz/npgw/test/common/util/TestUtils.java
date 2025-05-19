package xyz.npgw.test.common.util;

import com.microsoft.playwright.APIRequestContext;
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.entity.Company;
import xyz.npgw.test.common.entity.User;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class TestUtils {

    public static void createCompanyAdmin(APIRequestContext request, String company, String email, String password) {
        User user = User.newCompanyAdmin(company, email, password);
        User.delete(request, user);
        User.create(request, user);
    }

    public static void deleteUser(APIRequestContext request, String email) {
        User.delete(request, email);
    }

    public static void changeUserPassword(APIRequestContext request, String email, String newPassword) {
        User.changePassword(request, email, newPassword);
    }

    public static void createBusinessUnit(APIRequestContext request, String companyName, String merchantTitle) {
        BusinessUnit.create(request, companyName, merchantTitle);
    }

    public static void createBusinessUnitsIfNeeded(APIRequestContext request, User user) {
        Company.create(request, user.companyName());
        for (String merchantTitle : user.merchantIds()) {
            if (!BusinessUnit.exists(request, user.companyName(), merchantTitle)) {
                BusinessUnit.create(request, user.companyName(), merchantTitle);
            }
        }
    }

    public static void createMerchantTitleIfNeeded(APIRequestContext request, String company, String merchantTitle) {
        if (!BusinessUnit.exists(request, company, merchantTitle)) {
            BusinessUnit.create(request, company, merchantTitle);
        }
    }

    public static void deleteAllByMerchantTitle(APIRequestContext request, String companyName, String merchantTitle) {
        BusinessUnit.delete(request, companyName, merchantTitle);
    }

    public static void createCompany(APIRequestContext request, String companyName) {
        Company.create(request, companyName);
    }

    public static void createCompanyIfNeeded(APIRequestContext request, String companyName) {
        if (!Company.exists(request, companyName)) {
            Company.create(request, companyName);
        }
    }

    public static void deleteCompany(APIRequestContext request, String companyName) {
        Company.delete(request, companyName);
    }

    public static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

    public static void createAcquirer(APIRequestContext request, Acquirer acquirer) {
        Acquirer.create(request, acquirer);
    }

    public static boolean getAcquirer(APIRequestContext request, String acquirerName) {
        return Acquirer.get(request, acquirerName);
    }

    public static void deleteAcquirer(APIRequestContext request, String acquirerName) {
        Acquirer.delete(request, acquirerName);
    }
}
