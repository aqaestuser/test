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
}
