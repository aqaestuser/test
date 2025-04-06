package xyz.npgw.test.testdata;

import org.testng.annotations.DataProvider;

public class TestDataProvider {

    @DataProvider(name = "getAuthenticatedEndpoints")
    public static Object[][] getAuthenticatedEndpoints() {
        return new Object[][]{
                {"GUEST", "/dashboard"},
                {"GUEST", "/transactions"},
                {"GUEST", "/reports"},
                {"GUEST", "/system"},
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
    public static Object[][] getEmptyRequiredFields() {
        return new Object[][]{
                {"", ""},
                {"Company name", ""},
                {"", "Company type"}
        };
    }

    @DataProvider
    public static Object[][] getCompanyNameInvalidSpecialCharacters() {
        return new Object[][]{
                {"@"}, {"#"}, {"$"}, {"%"}, {"*"}, {"!"}, {"?"}, {"'"}, {")"},
                {"\""}, {"/"}, {"\\"}, {":"}, {";"}, {"<"}, {">"}, {"="}, {"("}
        };
    }
}
