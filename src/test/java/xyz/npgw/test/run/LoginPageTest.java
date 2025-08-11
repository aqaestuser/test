package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.page.AboutBlankPage;
import xyz.npgw.test.page.LoginPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPageTest extends BaseTest {

    @Test
    @TmsLink("149")
    @Epic("Login")
    @Feature("Navigation")
    @Description("Unauthenticated user navigate to 'Login page'")
    public void testNavigateToLoginPageUnauthenticated() {
        LoginPage loginPage = new AboutBlankPage(getPage())
                .navigate("/");

        Allure.step("Verify: Login Page URL");
        assertThat(loginPage.getPage()).hasURL(Constants.LOGIN_PAGE_URL);

        Allure.step("Verify: Login Page Title");
        assertThat(loginPage.getPage()).hasTitle(Constants.LOGIN_URL_TITLE);
    }

    @Test
    @TmsLink("81")
    @Epic("Login")
    @Feature("Remember me")
    @Description("User email is remembered after first successful login with checked 'Remember me'")
    public void testRememberMeCheckedSavesUserEmailUnauthenticated() {
        LoginPage loginPage = new AboutBlankPage(getPage())
                .navigate("/login")
                .fillEmailField(ProjectProperties.getEmail())
                .fillPasswordField(ProjectProperties.getPassword())
                .checkRememberMeCheckbox()
                .clickLoginButton()
                .clickLogOutButton();

        Allure.step("Verify: The user's email is in the email field");
        assertThat(loginPage.getEmailField()).hasValue(ProjectProperties.getEmail());
    }

    @Test
    @TmsLink("82")
    @Epic("Login")
    @Feature("Remember me")
    @Description("User email is NOT remembered after first successful login with unchecked 'Remember me'")
    public void testRememberMeUncheckedDontSaveUserEmailUnauthenticated() {
        LoginPage loginPage = new AboutBlankPage(getPage())
                .navigate("/")
                .fillEmailField(ProjectProperties.getEmail())
                .fillPasswordField(ProjectProperties.getPassword())
                .uncheckRememberMeCheckbox()
                .clickLoginButton()
                .clickLogOutButton();

        Allure.step("Verify: The user's email is not in the email field");
        assertThat(loginPage.getEmailField()).isEmpty();
    }

    @Test(dataProvider = "getAuthenticatedEndpoints", dataProviderClass = TestDataProvider.class)
    @TmsLink("165")
    @Epic("Login")
    @Feature("Navigation")
    @Description("Unauthenticated users are automatically redirected to the 'Login page'")
    public void testUnauthenticatedUserRedirectionToLoginPageUnauthenticated(String endpoint) {
        LoginPage loginPage = new AboutBlankPage(getPage())
                .navigate(endpoint);

        Allure.step("Verify: Unauthenticated user is on 'Login page'");
        assertThat(loginPage.getLoginFormTitle()).hasText("Welcome to NPGW");
    }
}
