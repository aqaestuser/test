package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTestForLogout;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.page.LoginPage;
import xyz.npgw.test.page.dashboard.SuperDashboardPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HeaderLogoutTest extends BaseTestForLogout {

    @Test(dataProvider = "getUserRole", dataProviderClass = TestDataProvider.class, priority = 1)
    @TmsLink("289")
    @Epic("Header")
    @Feature("User menu")
    @Description("Check if the user can change the password through the profile settings in the user menu")
    public void testChangePassword(String userRole) {
        String newPassword = "QWEdsa123@";

        SuperDashboardPage dashboardPage = new SuperDashboardPage(getPage())
                .clickUserMenuButton()
                .clickProfileSettingsButton()
                .fillPasswordField(newPassword)
                .fillRepeatPasswordField(newPassword)
                .clickSaveButton();

        Allure.step("Verify: success message for changing password");
        assertThat(dashboardPage.getAlert().getMessage())
                .hasText("SUCCESSPassword was changed successfully");

        dashboardPage
                .clickLogOutButton()
                .loginAs("%s.%s@email.com".formatted(getUid(), userRole.toLowerCase()), newPassword, userRole);

        Allure.step("Verify: Successfully login with changed password");
        assertThat(dashboardPage.getPage()).hasURL(Constants.DASHBOARD_PAGE_URL);
    }

    @Test
    @TmsLink("300")
    @Epic("Header")
    @Feature("User menu")
    @Description("Log out via button in the user menu")
    public void testLogOutViaButtonInUserMenu() {
        LoginPage loginPage = new SuperDashboardPage(getPage())
                .clickUserMenuButton()
                .clickLogOutButtonUserMenu();

        Allure.step("Verify: Login Page URL");
        assertThat(loginPage.getPage()).hasURL(Constants.LOGIN_PAGE_URL);
    }

    @Test
    @TmsLink("308")
    @Epic("Header")
    @Feature("Logout")
    @Description("Log out via button in the Header")
    public void testLogOutViaButtonInHeader() {
        LoginPage loginPage = new SuperDashboardPage(getPage())
                .clickLogOutButton();

        Allure.step("Verify: Login Page URL");
        assertThat(loginPage.getPage()).hasURL(Constants.LOGIN_PAGE_URL);
    }
}
