package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.LoginPage;
import xyz.npgw.test.testdata.Constants;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DashboardPageTest extends BaseTest {

    @Test
    @TmsLink("151")
    @Epic("Dashboard")
    @Feature("Navigation")
    @Description("User navigate to 'Dashboard page' after login")
    public void testNavigateToDashboardAfterLogin() {
        DashboardPage dashboardPage = new DashboardPage(getPage());

        Allure.step("Verify: Dashboard Page URL");
        assertThat(dashboardPage.getPage()).hasURL(Constants.DASHBOARD_PAGE_URL);

        Allure.step("Verify: Dashboard Page Title");
        assertThat(dashboardPage.getPage()).hasTitle(Constants.DASHBOARD_URL_TITLE);
    }

    @Test
    @TmsLink("150")
    @Epic("Dashboard")
    @Feature("Navigation")
    @Description("User navigate to 'Login page' after logout")
    public void testNavigateToLoginPageAfterLogout() {

        LoginPage loginPage = new DashboardPage(getPage())
                .getHeader()
                .clickLogOutButton();

        Allure.step("Verify: Login Page URL");
        assertThat(loginPage.getPage()).hasURL(Constants.LOGIN_PAGE_URL);

        Allure.step("Verify: Login Page Title");
        assertThat(loginPage.getPage()).hasTitle(Constants.LOGIN_URL_TITLE);
    }
}
