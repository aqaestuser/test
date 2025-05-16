package xyz.npgw.test.common.provider;

import org.testng.annotations.DataProvider;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.UserRole;
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.SystemConfig;
import xyz.npgw.test.common.entity.User;

public class TestDataProvider {

    @DataProvider
    public static Object[][] getAuthenticatedEndpoints() {
        return new Object[][]{
                {"UNAUTHORISED", "/dashboard"},
                {"UNAUTHORISED", "/transactions"},
                {"UNAUTHORISED", "/reports"},
                {"UNAUTHORISED", "/system"},
        };
    }

    @DataProvider
    public static Object[][] getInvalidCompanyNameLengths() {
        return new Object[][]{
                {"C"},
                {"Com"},
                {"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. "
                        + "Aenean commodo ligula eget dolor. Aenean ma"}
        };
    }

    @DataProvider
    public static Object[][] getCompanyNameInvalidSpecialCharacters() {
        return new Object[][]{
                {"%"},
//                {"@"}, {"#"}, {"$"}, {"%"}, {"*"}, {"!"}, {"?"}, {")"},
//                {"\""}, {"/"}, {"\\"}, {":"}, {";"}, {"<"}, {">"}, {"="}, {"("}
        };
    }

    @DataProvider
    public static Object[][] getInvalidCompanyNamesByLengthAndChar() {
        return new Object[][]{
                {"C", "@"},
                {"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. "
                        + "Aenean commodo ligula eget dolor. Aenean ma", "!"}
        };
    }

    @DataProvider
    public static Object[][] getAcquirersStatus() {
        return new Object[][]{
                {"Active"},
                {"Inactive"},
        };
    }

    @DataProvider
    public static Object[][] getUsers() {
        return new Object[][]{
                {new User("super", true,
                        UserRole.SUPER, new String[]{},
                        "super@test.com", ProjectProperties.getSuperPassword())},
                {new User("testframework", true,
                        UserRole.ADMIN, new String[]{},
                        "admin@test.com", ProjectProperties.getAdminPassword())},
                {new User("testframework", true,
                        UserRole.USER, new String[]{"businessUnitName"},
                        "user@test.com", ProjectProperties.getUserPassword())}
        };
    }

    @DataProvider
    public static Object[][] getCurrency() {
        return new Object[][]{
                {"EUR"},
                {"USD"},
        };
    }

    @DataProvider
    public Object[][] getMenuItemName() {
        return new Object[][]{
                {"CSV"},
                {"EXCEL"},
                //{"PDF"},
        };
    }

    @DataProvider
    public Object[][] acquirerNegativeData() {
        String acquirerName = "Acquirer with Error Message";
        String acquirerCode = "NGenius";

        return new Object[][]{
                {
                        new Acquirer(
                                acquirerCode,
                                "Acquirer Config",
                                new SystemConfig(
                                        "https://challenge.example.com",
                                        "https://fingerprint.example.com",
                                        "https://resource.example.com",
                                        "notification-queue"),
                                acquirerName,
                                new String[]{},
                                true),
                        """
ErrorSelect at least one allowed currency"""
                },
                {
                        new Acquirer(
                                acquirerCode,
                                "",
                                new SystemConfig(
                                        "",
                                        "",
                                        "",
                                        ""),
                                acquirerName,
                                new String[]{"USD"},
                                true),
                        """
ERRORsystemConfig.challengeUrl must be defined
systemConfig.fingerprintUrl must be defined
systemConfig.resourceUrl must be defined"""
                },
                {
                        new Acquirer(
                                acquirerCode,
                                "",
                                new SystemConfig(
                                        "https://challenge.example.com",
                                        "",
                                        "",
                                        ""),
                                acquirerName,
                                new String[]{"USD"},
                                true),
                        """
ERRORsystemConfig.fingerprintUrl must be defined
systemConfig.resourceUrl must be defined"""
                },
                {
                        new Acquirer(
                                acquirerCode,
                                "",
                                new SystemConfig(
                                        "https://challenge.example.com",
                                        "https://fingerprint.example.com",
                                        "",
                                        ""),
                                acquirerName,
                                new String[]{"USD"},
                                true),
                        """
ERRORsystemConfig.resourceUrl must be defined"""
                },
                {
                        new Acquirer(
                                acquirerCode,
                                "",
                                new SystemConfig(
                                        "//challenge.example.com",
                                        "//fingerprint.example.com",
                                        "ps://fingerprint.example.com",
                                        "some.text"),
                                acquirerName,
                                new String[]{"USD"},
                                true),
                        """
ERRORsystemConfig.challengeUrl doesn’t qualify for the URL syntax
systemConfig.fingerprintUrl doesn’t qualify for the URL syntax
systemConfig.resourceUrl doesn’t qualify for the URL syntax"""
                }
        };
    }

    @DataProvider(name = "passwordValidationData")
    public static Object[][] passwordValidationData() {
        return new Object[][]{
                {"UNAUTHORISED", "QWERTY1!",
                        "Password does not conform to policy: Password must have lowercase characters"},
                {"UNAUTHORISED", "qwerty1!",
                        "Password does not conform to policy: Password must have uppercase characters"},
                {"UNAUTHORISED", "Qwertyu!",
                        "Password does not conform to policy: Password must have numeric characters"},
                {"UNAUTHORISED", "Qwertyu1",
                        "Password does not conform to policy: Password must have symbol characters"}
        };
    }
}
