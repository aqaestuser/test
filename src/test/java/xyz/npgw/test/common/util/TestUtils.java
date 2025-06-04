package xyz.npgw.test.common.util;

import com.microsoft.playwright.APIRequestContext;
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.entity.Company;
import xyz.npgw.test.common.entity.User;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public final class TestUtils {

    public static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

    public static void createUser(APIRequestContext request, User user) {
        TestUtils.deleteCompany(request, user.companyName());
        Company.create(request, user);
        List<BusinessUnit> businessUnits = BusinessUnit.create(request, user);
        User newUser = new User(
                user.companyName(),
                user.enabled(),
                user.userRole(),
                businessUnits.stream().map(BusinessUnit::merchantId).toArray(String[]::new),
                user.email(),
                user.password());
        User.delete(request, newUser);
        User.create(request, newUser);
        User.passChallenge(request, user.email(), user.password());
    }

    public static void createCompanyAdmin(APIRequestContext request, String company, String email, String password) {
        User user = User.newCompanyAdmin(company, email, password);
        User.delete(request, user);
        User.create(request, user);
        User.passChallenge(request, user.email(), user.password());
    }

    public static void deleteUser(APIRequestContext request, String email) {
        User.delete(request, email);
    }

    public static void deleteUsers(APIRequestContext request, User[] users) {
        Arrays.stream(users).forEach(user -> User.delete(request, user.email()));
    }

    public static void changeUserPassword(APIRequestContext request, String email, String newPassword) {
        User.changePassword(request, email, newPassword);
    }

    public static BusinessUnit createBusinessUnit(APIRequestContext request, String companyName, String merchantTitle) {
        return BusinessUnit.create(request, companyName, merchantTitle);
    }

    public static BusinessUnit[] createBusinessUnits(APIRequestContext request, String company, String[] merchants) {
        return Arrays.stream(merchants)
                .map(merchantTitle -> BusinessUnit.create(request, company, merchantTitle))
                .toArray(BusinessUnit[]::new);
    }

    public static void deleteBusinessUnit(APIRequestContext request, String companyName, BusinessUnit businessUnit) {
        BusinessUnit.delete(request, companyName, businessUnit);
    }

    public static void deleteBusinessUnits(APIRequestContext request, String company, BusinessUnit[] businessUnits) {
        Arrays.stream(businessUnits).forEach(businessUnit -> BusinessUnit.delete(request, company, businessUnit));
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
        if (companyName.equals("super")) {
            return;
        }
        while (Company.delete(request, companyName) != 204) {
            deleteBusinessUnits(request, companyName, BusinessUnit.getAll(request, companyName));
            deleteUsers(request, User.getAll(request, companyName));
        }
    }

    public static void createAcquirer(APIRequestContext request, Acquirer acquirer) {
        Acquirer.create(request, acquirer);
    }

    public static void deleteAcquirer(APIRequestContext request, String acquirerName) {
        Acquirer.delete(request, acquirerName);
    }
}
