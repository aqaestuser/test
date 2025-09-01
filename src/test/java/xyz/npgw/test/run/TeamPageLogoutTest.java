package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.LoginPage;
import xyz.npgw.test.page.dashboard.AdminDashboardPage;
import xyz.npgw.test.page.dashboard.SuperDashboardPage;
import xyz.npgw.test.page.system.AdminTeamPage;
import xyz.npgw.test.page.system.SuperTeamPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TeamPageLogoutTest extends BaseTest {

    private static final String MERCHANT_TITLE = "Business unit 1";
    private static final String SUCCESS_MESSAGE_USER_CREATED = "SUCCESSUser was created successfully";

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createBusinessUnit(getApiRequestContext(), getCompanyName(), MERCHANT_TITLE);
    }

    @Test
    @TmsLink("554")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Reset company analyst password as super admin")
    public void testResetPasswordForCompanyAnalyst() {
        String email = "%s.reset.password@gmail.com".formatted(TestUtils.now());

        SuperTeamPage teamPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(getCompanyName())
                .clickAddUserButton()
                .fillEmailField(email)
                .fillPasswordField("Password1!")
                .checkCompanyAdminRadiobutton()
                .clickCreateButton()
                .waitForUserPresence(getApiRequestContext(), email, getCompanyName())
                .getTable()
                .clickResetUserPasswordIcon(email)
                .fillPasswordField("NewPassword1!")
                .clickResetButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText("SUCCESSPassword was reset successfully");

        teamPage.clickLogOutButton()
                .fillEmailField(email)
                .fillPasswordField("NewPassword1!")
                .clickLoginButtonToChangePassword()
                .fillNewPasswordField("ChangedNewPassword1!")
                .fillRepeatNewPasswordField("ChangedNewPassword1!")
                .clickSaveButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText("SUCCESSPassword is changed successfully");
    }

    @Test
    @TmsLink("554")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Reset company analyst password as company admin")
    public void testResetPasswordForCompanyAnalystAsAdmin() {
        String email = "%s.reset.password@gmail.com".formatted(TestUtils.now());

        AdminTeamPage teamPage = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickAddUserButton()
                .fillEmailField(email)
                .fillPasswordField("Password1!")
                .checkCompanyAdminRadiobutton()
                .clickCreateButton()
                .waitForUserPresence(getApiRequestContext(), email, getCompanyName())
                .getTable()
                .clickResetUserPasswordIcon(email)
                .fillPasswordField("NewPassword1!")
                .clickResetButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText("SUCCESSPassword was reset successfully");

        teamPage.clickLogOutButton()
                .fillEmailField(email)
                .fillPasswordField("NewPassword1!")
                .clickLoginButtonToChangePassword()
                .fillNewPasswordField("ChangedNewPassword1!")
                .fillRepeatNewPasswordField("ChangedNewPassword1!")
                .clickSaveButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText("SUCCESSPassword is changed successfully");
    }


    @Test
    @TmsLink("492")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Create and deactivate company analyst as super admin")
    public void testCreateCompanyAnalystAndDeactivate() {
        String analystEmail = "%s.company.analyst@gmail.com".formatted(TestUtils.now());
        String analystPassword = "CompanyAnalyst123!";

        SuperTeamPage teamPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(getCompanyName())
                .clickAddUserButton()
                .fillEmailField(analystEmail)
                .fillPasswordField(analystPassword)
                .checkAllowedBusinessUnitCheckbox(MERCHANT_TITLE)
                .clickCreateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText(SUCCESS_MESSAGE_USER_CREATED);

        teamPage
                .getAlert().clickCloseButton()
                .waitForUserPresence(getApiRequestContext(), analystEmail, getCompanyName());

        Allure.step("Verify: status of the user was changed");
        assertThat(teamPage.getTable().getCell(analystEmail, "User role")).hasText("USER");

        Allure.step("Verify: status of the user");
        assertThat(teamPage.getTable().getCell(analystEmail, "Status")).hasText("Active");

        Allure.step("Verify: deactivate user icon appears");
        assertThat(teamPage.getTable().getUserActivityIcon(analystEmail)).hasAttribute("data-icon", "ban");

        teamPage
                .getTable().clickDeactivateUserButton(analystEmail)
                .clickDeactivateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText("SUCCESSUser was deactivated successfully");

        teamPage
                .getAlert().clickCloseButton()
                .waitForUserDeactivation(getApiRequestContext(), analystEmail, getCompanyName());

        Allure.step("Verify: status of the user was changed");
        assertThat(teamPage.getTable().getCell(analystEmail, "Status")).hasText("Inactive");

        Allure.step("Verify: deactivate user icon appears");
        assertThat(teamPage.getTable().getUserActivityIcon(analystEmail)).hasAttribute("data-icon", "check");

        LoginPage loginPage = teamPage
                .clickLogOutButton()
                .loginAsDisabledUser(analystEmail, analystPassword);

        Allure.step("Verify: error message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText("ERRORUser is disabled.");

        loginPage
                .loginAsSuper("%s.super@email.com".formatted(getUid()), ProjectProperties.getPassword())
                .getHeader().clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(getCompanyName())
                .getTable().clickEditUserButton(analystEmail)
                .checkActiveRadiobutton()
                .clickSaveChangesButton()
                .clickLogOutButton()
                .fillEmailField(analystEmail)
                .fillPasswordField(analystPassword)
                .clickLoginButtonToChangePassword()
                .fillNewPasswordField(analystPassword)
                .fillRepeatNewPasswordField(analystPassword)
                .clickSaveButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText("SUCCESSPassword is changed successfully");
    }

    @Test
    @TmsLink("492")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Create company analyst under admin")
    public void testCreateCompanyAnalystAndDeactivateAsAdmin() {
        String analystEmail = "%s.company.analyst@gmail.com".formatted(TestUtils.now());
        String analystPassword = "CompanyAnalyst123!";

        AdminTeamPage teamPage = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickAddUserButton()
                .fillEmailField(analystEmail)
                .fillPasswordField(analystPassword)
                .checkAllowedBusinessUnitCheckbox(MERCHANT_TITLE)
                .clickCreateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText(SUCCESS_MESSAGE_USER_CREATED);

        teamPage
                .getAlert().clickCloseButton()
                .waitForUserPresence(getApiRequestContext(), analystEmail, getCompanyName());

        Allure.step("Verify: status of the user was changed");
        assertThat(teamPage.getTable().getCell(analystEmail, "User role")).hasText("USER");

        Allure.step("Verify: status of the user");
        assertThat(teamPage.getTable().getCell(analystEmail, "Status")).hasText("Active");

        Allure.step("Verify: deactivate user icon appears");
        assertThat(teamPage.getTable().getUserActivityIcon(analystEmail)).hasAttribute("data-icon", "ban");

        teamPage
                .getTable().clickDeactivateUserButton(analystEmail)
                .clickDeactivateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText("SUCCESSUser was deactivated successfully");

        teamPage
                .getAlert().clickCloseButton()
                .waitForUserDeactivation(getApiRequestContext(), analystEmail, getCompanyName());

        Allure.step("Verify: status of the user was changed");
        assertThat(teamPage.getTable().getCell(analystEmail, "Status")).hasText("Inactive");

        Allure.step("Verify: deactivate user icon appears");
        assertThat(teamPage.getTable().getUserActivityIcon(analystEmail)).hasAttribute("data-icon", "check");

        LoginPage loginPage = teamPage
                .clickLogOutButton()
                .loginAsDisabledUser(analystEmail, analystPassword);

        Allure.step("Verify: error message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText("ERRORUser is disabled.");

        loginPage
                .loginAsAdmin("%s.admin@email.com".formatted(getUid()), ProjectProperties.getPassword())
                .getHeader().clickSystemAdministrationLink()
                .getTable().clickEditUserButton(analystEmail)
                .checkActiveRadiobutton()
                .clickSaveChangesButton()
                .clickLogOutButton()
                .fillEmailField(analystEmail)
                .fillPasswordField(analystPassword)
                .clickLoginButtonToChangePassword()
                .fillNewPasswordField(analystPassword)
                .fillRepeatNewPasswordField(analystPassword)
                .clickSaveButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText("SUCCESSPassword is changed successfully");
    }
}
