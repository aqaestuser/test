package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.LoginPage;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.common.HeaderComponent;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Slf4j
public class HeaderTest extends BaseTest {

    @Test
    @TmsLink("209")
    @Epic("Header")
    @Feature("Logo")
    @Description("Check that Logo in header contains text 'NPGW' and image")
    public void testLogoContainsTextAndImage() {
        HeaderComponent headerComponent = new DashboardPage(getPage()).getHeader();

        Allure.step("Verify: Logo contains text 'NPGW'");
        assertThat(headerComponent.getLogo()).hasText("NPGW");

        Allure.step("Verify: Logo contains image");
        Assert.assertTrue(headerComponent.getImg().isVisible(), "Image inside logo should be visible");
        Assert.assertNotNull(headerComponent.getImg().getAttribute("src"), "Image should have a 'src' attribute");

        Allure.step("Verify: Image inside logo is fully loaded");
        Assert.assertTrue(headerComponent.isLogoImageLoaded(), "Image inside logo should be fully loaded");
    }

    @Test
    @TmsLink("211")
    @Epic("Header")
    @Feature("Transactions menu item")
    @Description("Check after clicking on Transactions user redirected to Transactions page")
    public void testTransactionsLink() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink();

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
                .getHeader().clickTransactionsLink()
                .getHeader().clickLogoButton();

        Allure.step("Verify: Dashboard Page URL");
        assertThat(dashboardPage.getPage()).hasURL(Constants.DASHBOARD_PAGE_URL);
    }

    @Test(dataProvider = "getUserRoleAndEmail", dataProviderClass = TestDataProvider.class)
    @TmsLink("289")
    @Epic("Header")
    @Feature("User menu")
    @Description("Check if the user can change the password through the profile settings in the user menu")
    public void testChangePassword(String userRole, String email) {
        String newPassword = "QWEdsa123@";
        TestUtils.changeUserPassword(getPage().request(), email, newPassword);

        DashboardPage dashboardPage = new DashboardPage(getPage())
                .getHeader().clickLogOutButton()
                .login(email, newPassword)
                .getHeader().clickUserMenuButton()
                .getHeader().clickProfileSettingsButton()
                .getHeader().fillPasswordField(ProjectProperties.getUserPassword())
                .getHeader().fillRepeatPasswordField(ProjectProperties.getUserPassword())
                .getHeader().clickSaveButton();

        Allure.step("Verify: success message for changing password");
        assertThat(dashboardPage.getAlert().getMessage())
                .hasText("SUCCESSPassword was changed successfull");

        dashboardPage
                .getHeader().clickLogOutButton()
                .login(email, ProjectProperties.getUserPassword());

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
                .getHeader().clickUserMenuButton()
                .getHeader().clickLogOutButtonUserMenu();

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
                .getHeader().clickLogOutButton();

        Allure.step("Verify: Login Page URL");
        assertThat(loginPage.getPage()).hasURL(Constants.LOGIN_PAGE_URL);
    }

    @Test
    @TmsLink("437")
    @Epic("Header")
    @Feature("User menu")
    @Description("Verify that the user can switch to the dark theme")
    public void testDarkColorThemeSwitch() {
        new DashboardPage(getPage())
                .getHeader().clickUserMenuButton()
                .getHeader().clickDarkRadioButton();

        Allure.step("Verify that the dark color theme is selected");
        assertThat(getPage().locator("html.dark")).isVisible();
    }

    @Test
    @TmsLink("440")
    @Epic("Header")
    @Feature("User menu")
    @Description("Verify that the user can switch to the light theme")
    public void testLightColorThemeSwitch() {
        new DashboardPage(getPage())
                .getHeader().clickUserMenuButton()
                .getHeader().clickLightRadioButton();

        Allure.step("Verify that the light color theme is selected");
        assertThat(getPage().locator("html.light")).isVisible();
    }

    @Test
    @TmsLink("494")
    @Epic("Header")
    @Feature("User menu")
    @Description("Verify that the color theme matching with the default browser theme")
    public void testDefaultThemeMatching() {
        Allure.step("Verify that the current color theme matches the default browser theme");
        assertThat(getPage().locator("html")).hasClass(ProjectProperties.getColorScheme().name().toLowerCase());
    }

    @Test(dataProvider = "getUserRole", dataProviderClass = TestDataProvider.class)
    @TmsLink("540")
    @Epic("Header")
    @Feature("User menu")
    @Description("Check password policy validation error messages when changing password in user menu")
    public void testChangePasswordValidationMessages(String userRole) {
        DashboardPage dashboardPage = new DashboardPage(getPage())
                .getHeader().clickUserMenuButton()
                .getHeader().clickProfileSettingsButton()
                .getHeader().fillPasswordField("QWERTY1!")
                .getHeader().fillRepeatPasswordField("QWERTY1!")
                .getHeader().clickSaveButton();

        Allure.step("Verify: error message for missing lowercase");
        assertThat(dashboardPage.getAlert().getMessage())
                .hasText("ERRORPassword does not conform to policy: Password must have lowercase characters");

        dashboardPage
                .getHeader().fillPasswordField("qwerty1!")
                .getHeader().fillRepeatPasswordField("qwerty1!")
                .getHeader().clickSaveButton();

        Allure.step("Verify: error message for missing uppercase");
        assertThat(dashboardPage.getAlert().getMessage())
                .hasText("ERRORPassword does not conform to policy: Password must have uppercase characters");

        dashboardPage
                .getHeader().fillPasswordField("Qwertyu!")
                .getHeader().fillRepeatPasswordField("Qwertyu!")
                .getHeader().clickSaveButton();

        Allure.step("Verify: error message for missing numeric");
        assertThat(dashboardPage.getAlert().getMessage())
                .hasText("ERRORPassword does not conform to policy: Password must have numeric characters");

        dashboardPage
                .getHeader().fillPasswordField("Qwertyu1")
                .getHeader().fillRepeatPasswordField("Qwertyu1")
                .getHeader().clickSaveButton();

        Allure.step("Verify: error message for missing symbol");
        assertThat(dashboardPage.getAlert().getMessage())
                .hasText("ERRORPassword does not conform to policy: Password must have symbol characters");
    }
}
