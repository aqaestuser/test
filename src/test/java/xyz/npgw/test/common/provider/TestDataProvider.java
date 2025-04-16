package xyz.npgw.test.common.provider;

import org.testng.annotations.DataProvider;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.UserRole;
import xyz.npgw.test.common.util.User;

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
}
