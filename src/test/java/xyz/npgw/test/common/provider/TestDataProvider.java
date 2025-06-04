package xyz.npgw.test.common.provider;

import org.testng.annotations.DataProvider;
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.SystemConfig;

public class TestDataProvider {

    @DataProvider
    public static Object[][] getUserRole() {
        return new Object[][]{
                {"SUPER"},
                {"ADMIN"},
                {"USER"}
        };
    }

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
    public static Object[][] getCurrency() {
        return new Object[][]{
                {"EUR"},
                {"USD"},
                {"GBP"},
        };
    }

    @DataProvider
    public static Object[][] getCardType() {
        return new Object[][]{
                {"VISA"},
                {"MASTERCARD"},
        };
    }

    @DataProvider
    public static Object[][] getStatus() {
        return new Object[][]{
                {"PENDING"},
                {"INITIATED"},
                {"SUCCESS"},
                {"FAILED"},
                {"CANCELLED"},
                {"EXPIRED"},
        };
    }

    @DataProvider
    public static Object[][] getMultiStatus2() {
        return new Object[][]{
                {"PENDING", "INITIATED"},
                {"SUCCESS", "FAILED"},
                {"CANCELLED", "EXPIRED"},
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
}
