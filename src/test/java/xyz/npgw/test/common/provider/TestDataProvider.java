package xyz.npgw.test.common.provider;

import org.testng.annotations.DataProvider;

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
                {"/dashboard"},
                {"/transactions"},
                {"/reports"},
                {"/system"},
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
                        + "Aenean commodo ligula eget dolor. Aenean ", "!"}
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
                {"PARTIAL_REFUND"},
                {"REFUND"},
        };
    }

    @DataProvider
    public static Object[][] getMultiStatus2() {
        return new Object[][]{
                {"PENDING", "INITIATED"},
                {"SUCCESS", "FAILED"},
                {"CANCELLED", "EXPIRED"},
                {"PARTIAL_REFUND", "REFUNDED"},
        };
    }

    @DataProvider
    public Object[][] getExportFileType() {
        return new Object[][]{
                {"CSV"},
                {"EXCEL"},
                {"PDF"},
        };
    }
}
