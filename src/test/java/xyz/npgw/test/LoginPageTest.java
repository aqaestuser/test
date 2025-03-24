package xyz.npgw.test;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.LoginPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPageTest extends BaseTest {

    @Test
    public void testNavigateToLoginPage() {

        LoginPage loginPage = new LoginPage(getPage());

        assertThat(loginPage.getPage()).hasURL(Constants.LOGIN_PAGE_URL);
        assertThat(loginPage.getPage()).hasTitle(Constants.BASE_URL_TITLE);
    }

    @Test
    public void testLogin() {
        DashboardPage dashboardPage = new LoginPage(getPage())
                .fillEmailField(Constants.USER_EMAIL)
                .fillPasswordField(Constants.USER_PASSWORD)
                .clickLoginButton();

        assertThat(dashboardPage.getPage()).hasURL(Constants.DASHBOARD_PAGE_URL);
        assertThat(dashboardPage.getPage()).hasTitle(Constants.DASHBOARD_URL_TITLE);
    }

    @Test
    @TmsLink("81")
    @Epic("Login page")
    @Feature("Remember me")
    @Description("User email is remembered after first successful login with checked 'Remember me'")
    public void testRememberMeCheckedSavesUserEmail() {
        LoginPage loginPage = new LoginPage(getPage())
                .fillEmailField(Constants.USER_EMAIL)
                .fillPasswordField(Constants.USER_PASSWORD)
                .checkRememberMeCheckbox()
                .clickLoginButton()
                .clickLogOutButton();

        assertThat(loginPage.getEmailField()).hasValue(Constants.USER_EMAIL);
    }

    @Test
    @TmsLink("82")
    @Epic("Login page")
    @Feature("Remember me")
    @Description("User email is NOT remembered after first successful login with unchecked 'Remember me'")
    public void testRememberMeUncheckedDontSaveUserEmail() {
        LoginPage loginPage = new LoginPage(getPage())
                .fillEmailField(Constants.USER_EMAIL)
                .fillPasswordField(Constants.USER_PASSWORD)
                .uncheckRememberMeCheckbox()
                .clickLoginButton()
                .clickLogOutButton();

        assertThat(loginPage.getEmailField()).hasValue("");
    }
}
