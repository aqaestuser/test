package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.AboutBlankPage;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.LoginPage;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.dialog.ProfileSettingsDialog;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HeaderTest extends BaseTest {

    @Test
    @TmsLink("209")
    @Epic("Header")
    @Feature("Logo")
    @Description("Check that Logo in header contains text 'NPGW' and image")
    public void testLogoContainsTextAndImage() {
        DashboardPage dashboardPage = new DashboardPage(getPage());

        Allure.step("Verify: Logo contains text 'NPGW'");
        assertThat(dashboardPage.getLogo()).hasText("NPGW");

        Allure.step("Verify: Logo contains image");
        assertThat(dashboardPage.getLogoImg()).isVisible();
        assertThat(dashboardPage.getLogoImg()).hasAttribute("src", Pattern.compile("/assets/.*png"));

        Allure.step("Verify: Image inside logo is fully loaded");
        Assert.assertTrue(dashboardPage.isLogoImageLoaded(), "Image inside logo should be fully loaded");
    }

    @Test
    @TmsLink("211")
    @Epic("Header")
    @Feature("Transactions menu item")
    @Description("Check after clicking on Transactions user redirected to Transactions page")
    public void testTransactionsLink() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink();

        Allure.step("Verify: Transactions Page URL");
        assertThat(transactionsPage.getPage()).hasURL(Constants.TRANSACTIONS_PAGE_URL);
    }

    @Test
    @TmsLink("242")
    @Epic("Header")
    @Feature("Logo")
    @Description("Check that click on Logo return user to the dashboard page from other pages")
    public void testClickLogoReturnToDashboardPage() {
        DashboardPage dashboardPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .clickLogoButton();

        Allure.step("Verify: Dashboard Page URL");
        assertThat(dashboardPage.getPage()).hasURL(Constants.DASHBOARD_PAGE_URL);
    }

    @Ignore
    @Test(dataProvider = "getNewUsers", dataProviderClass = TestDataProvider.class, priority = 1)
    @TmsLink("289")
    @Epic("Header")
    @Feature("User menu")
    @Description("Check if the user can change the password through the profile settings in the user menu")
    public void testChangePassword(String userRole, User user) {
        TestUtils.createUser(getApiRequestContext(), user);
        String newPassword = "QWEdsa123@";

        DashboardPage dashboardPage = new AboutBlankPage(getPage())
                .navigate("/")
                .login(user.email(), user.password())
                .clickUserMenuButton()
                .clickProfileSettingsButton()
                .fillPasswordField(newPassword)
                .fillRepeatPasswordField(newPassword)
                .clickSaveButton();

        Allure.step("Verify: success message for changing password");
        assertThat(dashboardPage.getAlert().getMessage())
                .hasText("SUCCESSPassword was changed successfull"); // TODO bug - typo in message

        dashboardPage
                .clickLogOutButton()
                .login(user.email(), newPassword);

        Allure.step("Verify: Successfully login with changed password");
        assertThat(dashboardPage.getPage()).hasURL(Constants.DASHBOARD_PAGE_URL);
    }

    @Test
    @TmsLink("300")
    @Epic("Header")
    @Feature("User menu")
    @Description("Log out via button in the user menu")
    public void testLogOutViaButtonInUserMenu() {
        LoginPage loginPage = new DashboardPage(getPage())
                .clickUserMenuButton()
                .clickLogOutButtonUserMenu();

        Allure.step("Verify: Login Page URL");
        assertThat(loginPage.getPage()).hasURL(Constants.LOGIN_PAGE_URL);
    }

    @Test
    @TmsLink("308")
    @Epic("Header")
    @Feature("Log Out")
    @Description("Log out via button in the Header")
    public void testLogOutViaButtonInHeader() {
        LoginPage loginPage = new DashboardPage(getPage())
                .clickLogOutButton();

        Allure.step("Verify: Login Page URL");
        assertThat(loginPage.getPage()).hasURL(Constants.LOGIN_PAGE_URL);
    }

    @Test
    @TmsLink("437")
    @Epic("Header")
    @Feature("User menu")
    @Description("Verify that the user can switch to the dark theme")
    public void testDarkColorThemeSwitch() {
        DashboardPage dashboardPage = new DashboardPage(getPage())
                .clickUserMenuButton()
                .clickDarkRadioButton();

        Allure.step("Verify that the dark color theme is selected");
        assertThat(dashboardPage.getHtmlTag()).hasClass("dark");
    }

    @Test
    @TmsLink("440")
    @Epic("Header")
    @Feature("User menu")
    @Description("Verify that the user can switch to the light theme")
    public void testLightColorThemeSwitch() {
        DashboardPage dashboardPage = new DashboardPage(getPage())
                .clickUserMenuButton()
                .clickLightRadioButton();

        Allure.step("Verify that the light color theme is selected");
        assertThat(dashboardPage.getHtmlTag()).hasClass("light");
    }

    @Test
    @TmsLink("494")
    @Epic("Header")
    @Feature("User menu")
    @Description("Verify that the color theme matching with the default browser theme")
    public void testDefaultThemeMatching() {
        DashboardPage dashboardPage = new DashboardPage(getPage());

        Allure.step("Verify that the current color theme matches the default browser theme");
        assertThat(dashboardPage.getHtmlTag()).hasClass(ProjectProperties.getColorScheme().name().toLowerCase());
    }

    @Test(dataProvider = "getUserRole", dataProviderClass = TestDataProvider.class)
    @TmsLink("540")
    @Epic("Header")
    @Feature("User menu")
    @Description("Check password policy validation error messages when changing password in user menu")
    public void testChangePasswordValidationMessages(String userRole) {
        ProfileSettingsDialog<DashboardPage> profileSettingsDialog = new DashboardPage(getPage())
                .clickUserMenuButton()
                .clickProfileSettingsButton()
                .fillPasswordField("QWERTY1!")
                .fillRepeatPasswordField("QWERTY1!")
                .clickSaveButtonWhenError();

        Allure.step("Verify: error message for missing lowercase");
        assertThat(profileSettingsDialog.getAlert().getMessage())
                .hasText("ERRORPassword does not conform to policy: Password must have lowercase characters");

        profileSettingsDialog
                .getAlert().waitUntilAlertIsHidden()
                .fillPasswordField("qwerty1!")
                .fillRepeatPasswordField("qwerty1!")
                .clickSaveButtonWhenError();

        Allure.step("Verify: error message for missing uppercase");
        assertThat(profileSettingsDialog.getAlert().getMessage())
                .hasText("ERRORPassword does not conform to policy: Password must have uppercase characters");

        profileSettingsDialog
                .getAlert().waitUntilAlertIsHidden()
                .fillPasswordField("Qwertyu!")
                .fillRepeatPasswordField("Qwertyu!")
                .clickSaveButtonWhenError();

        Allure.step("Verify: error message for missing numeric");
        assertThat(profileSettingsDialog.getAlert().getMessage())
                .hasText("ERRORPassword does not conform to policy: Password must have numeric characters");

        profileSettingsDialog
                .getAlert().waitUntilAlertIsHidden()
                .fillPasswordField("Qwertyu1")
                .fillRepeatPasswordField("Qwertyu1")
                .clickSaveButtonWhenError();

        Allure.step("Verify: error message for missing symbol");
        assertThat(profileSettingsDialog.getAlert().getMessage())
                .hasText("ERRORPassword does not conform to policy: Password must have symbol characters");
    }

    @Test(dataProvider = "getUserRole", dataProviderClass = TestDataProvider.class)
    @TmsLink("626")
    @Epic("Header")
    @Feature("User menu")
    @Description("Verify Minimum and Maximum Password Length Restrictions (negative)")
    public void testPasswordLengthRestrictionsOnChange(String userRole) {
        ProfileSettingsDialog<DashboardPage> profileSettingsDialog = new DashboardPage(getPage())
                .clickUserMenuButton()
                .clickProfileSettingsButton()
                .fillPasswordField("A".repeat(7))
                .fillRepeatPasswordField("A".repeat(7));

        Allure.step("Verify: error message for 7 characters short password is displayed");
        assertThat(profileSettingsDialog.getErrorMessage())
                .hasText("Password must be at least 8 characters long");

        Allure.step("Verify: Save button is disabled");
        assertThat(profileSettingsDialog.getSaveButton()).isDisabled();

        profileSettingsDialog
                .fillPasswordField("A".repeat(21))
                .fillRepeatPasswordField("A".repeat(21));

        Allure.step("Verify that the 'Password' field is limited to 20 characters.");
        assertThat(profileSettingsDialog.getPasswordField()).hasValue("A".repeat(20));

        Allure.step("Verify that the 'RepeatPassword' field is limited to 20 characters.");
        assertThat(profileSettingsDialog.getRepeatPasswordField()).hasValue("A".repeat(20));
    }
}
