package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.UserRole;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.AboutBlankPage;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.LoginPage;
import xyz.npgw.test.page.dialog.user.AddUserDialog;
import xyz.npgw.test.page.dialog.user.EditUserDialog;
import xyz.npgw.test.page.system.TeamPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TeamPageTest extends BaseTest {

    private static final String COMPANY_NAME = "Smitham-Johnson";
    private static final String ADMIN_COMPANY_NAME = "Admin company";
    private static final String ADMIN_EMAIL = "admin.email@gmail.com";
    private static final String ADMIN_PASSWORD = "AdminPassword1!";
    private static final String SUCCESS_MESSAGE_USER_CREATED = "SUCCESSUser was created successfully";
    private static final String SUCCESS_MESSAGE_USER_UPDATED = "SUCCESSUser was updated successfully";

    User user = new User(
            COMPANY_NAME,
            true,
            UserRole.USER,
            new String[]{"MerchantNameTest"},
            "dummy@email.com",
            ProjectProperties.getUserPassword());

    User updatedUser = new User(
            COMPANY_NAME,
            false,
            UserRole.ADMIN,
            new String[]{},
            "dummy@email.com",
            ProjectProperties.getAdminPassword()
    );

    @Test
    @TmsLink("154")
    @Epic("System/Team")
    @Feature("Navigation")
    @Description("User navigate to 'System administration page' after clicking "
            + "on 'System administration' link on the header")
    public void testNavigateToSystemAdministrationPage() {
        TeamPage systemAdministrationPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink();

        Allure.step("Verify: System administration Page URL");
        assertThat(systemAdministrationPage.getPage()).hasURL(Constants.SYSTEM_PAGE_URL);

        Allure.step("Verify: System administration Page Title");
        assertThat(systemAdministrationPage.getPage()).hasTitle(Constants.SYSTEM_URL_TITLE);
    }

    @Ignore("")
    @Test(dataProvider = "getUsers", dataProviderClass = TestDataProvider.class)
    @TmsLink("298")
    @Epic("System/Team")
    @Feature("Add user")
    @Description("Add users with roles [SUPER, ADMIN, USER] as super admin")
    public void testAddUser(User user) {
        TestUtils.createBusinessUnitsIfNeeded(getApiRequestContext(), user);
        TestUtils.deleteUser(getApiRequestContext(), user);

        TeamPage teamPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(user.companyName())
                .clickAddUserButton()
                .fillEmailField(user.email())
                .fillPasswordField(user.password())
                .setStatusRadiobutton(user.enabled())
                .setUserRoleRadiobutton(user.userRole())
                .setAllowedBusinessUnits(user.merchantIds())
                .clickCreateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getAlertMessage()).hasText(SUCCESS_MESSAGE_USER_CREATED);
    }

    @Ignore()
    @Test
    @TmsLink("330")
    @Epic("System/Team")
    @Feature("Add user")
    @Description("Add a new user and verify that all fields, statuses, and icons are correctly displayed(e2e).")
    public void testAddCompanyAnalyst() {
        TestUtils.deleteUser(getApiRequestContext(), user);
        TestUtils.createCompanyIfNeeded(getApiRequestContext(), user.companyName());
        TestUtils.createBusinessUnitsIfNeeded(getApiRequestContext(), user);

        AddUserDialog addUserDialog = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(user.companyName())
                .clickAddUserButton();

        Allure.step("Verify: 'Add user' header is displayed");
        assertThat(addUserDialog.getDialogHeader()).hasText("Add user");

        TeamPage teamPage = addUserDialog
                .createUser(user);

        Allure.step("Verify: a success alert appears after user creation");
        assertThat(teamPage.getAlert().getAlertMessage()).hasText(SUCCESS_MESSAGE_USER_CREATED);

        teamPage.clickRefreshDataButton();

        Allure.step("Verify: selected company is displayed in the 'Select company' field");
        assertThat(teamPage.getSelectCompany().getSelectCompanyField()).hasValue(user.companyName());

        Allure.step("Verify: new user has the role 'USER'");
        assertThat(teamPage.getTable().getCell("User role", user.email())).hasText("USER");

        Allure.step("Verify: new user has status 'Active'");
        assertThat(teamPage.getTable().getCell("Status", user.email())).hasText("Active");

        Allure.step("Verify: 'Deactivate' icon is shown for the new user");
        assertEquals(teamPage.getTable().getUserActivityIcon(user.email()).getAttribute("data-icon"), "ban");
    }

    @Ignore
    @Test
    @TmsLink("331")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Edits the user's role and status, verifies the updates, and reactivates the user(e2e).")
    public void testEditUser() {
        TestUtils.deleteUser(getApiRequestContext(), user.email());
        TestUtils.createBusinessUnitsIfNeeded(getApiRequestContext(), user);

        EditUserDialog editUserDialog = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(user.companyName())
                .clickAddUserButton()
                .createUser(user)
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickRefreshDataButton()
                .clickEditUserButton(user.email());

        Allure.step("Verify: 'Edit user' header is displayed");
        assertThat(editUserDialog.getDialogHeader()).hasText("Edit user");

        TeamPage teamPage = editUserDialog
                .setStatusRadiobutton(updatedUser.enabled())
                .unsetAllowedBusinessUnits(user.merchantIds())
                .setUserRoleRadiobutton(updatedUser.userRole())
                .clickSaveChangesButton()
                .clickRefreshDataButton();

        Allure.step("Verify: success alert appears after user update");
        assertThat(teamPage.getAlert().getAlertMessage()).hasText(SUCCESS_MESSAGE_USER_UPDATED);

        Allure.step("Verify: selected company is displayed in the 'Select company' field");
        assertThat(teamPage.getSelectCompany().getSelectCompanyField()).hasValue(user.companyName());

        Allure.step("Verify: user role was updated to 'ADMIN'");
        assertThat(teamPage.getTable().getCell("User role", user.email())).hasText("ADMIN");

        Allure.step("Verify: Verify that user status was updated to 'Inactive'");
        assertThat(teamPage.getTable().getCell("Status", user.email())).hasText("Inactive");

        Allure.step("Verify: 'Activate' icon is shown for the user");
        assertEquals(teamPage.getTable().getUserActivityIcon(user.email()).getAttribute("data-icon"), "check");
    }

    @Ignore
    @Test
    @TmsLink("474")
    @Epic("System/Team")
    @Feature("Add user")
    @Description("Create new company admin user")
    public void testCreateCompanyAdminUser(@Optional("UNAUTHORISED") String userRole) {
        String email = "email@gmail.com";
        TestUtils.deleteUser(getApiRequestContext(), email);
        TestUtils.createCompany(getApiRequestContext(), ADMIN_COMPANY_NAME);
        TestUtils.createCompanyAdmin(getApiRequestContext(), ADMIN_COMPANY_NAME, ADMIN_EMAIL, ADMIN_PASSWORD);

        TeamPage teamPage = new AboutBlankPage(getPage())
                .navigate("/login")
                .loginAndChangePassword(ADMIN_EMAIL, ADMIN_PASSWORD)
                .getHeader().clickSystemAdministrationLink()
                .clickAddUserButton()
                .createCompanyAdmin(email, "Password1!");

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getAlertMessage()).hasText(SUCCESS_MESSAGE_USER_CREATED);
    }

    @Ignore("")
    @Test
    @TmsLink("471")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Deactivate user by 'Change user activity button' and verify status change")
    public void testDeactivateUserViaChangeUserActivityButton() {
        TestUtils.deleteUser(getApiRequestContext(), user.email());
        TestUtils.createCompanyIfNeeded(getApiRequestContext(), user);
        TestUtils.createBusinessUnitsIfNeeded(getApiRequestContext(), user);

        TeamPage teamPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(user.companyName())
                .clickAddUserButton()
                .createUser(user)
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickRefreshDataButton()
                .getTable().deactivateUser(user.email());

        Allure.step("Verify: selected company is displayed in the 'Select company' field");
        assertThat(teamPage.getSelectCompany().getSelectCompanyField()).hasValue(user.companyName());

        Allure.step("Verify: user status becomes 'Inactive' in the table");
        assertThat(teamPage.getTable().getCell("Status", user.email())).hasText("Inactive");

        Allure.step("Verify: 'Activate user' icon is shown for the user");
        assertEquals(teamPage.getTable().getUserActivityIcon(user.email()).getAttribute("data-icon"), "check");
    }

    @Ignore
    @Test
    @TmsLink("475")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Edit user under company admin")
    public void testEditCompanyUser(@Optional("UNAUTHORISED") String userRole) {
        String email = "edit.user@gmail.com";
        TestUtils.deleteUser(getApiRequestContext(), email);
        TestUtils.createCompany(getApiRequestContext(), ADMIN_COMPANY_NAME);
        TestUtils.createCompanyAdmin(getApiRequestContext(), ADMIN_COMPANY_NAME, ADMIN_EMAIL, ADMIN_PASSWORD);

        TeamPage teamPage = new AboutBlankPage(getPage())
                .navigate("/login")
                .loginAndChangePassword(ADMIN_EMAIL, ADMIN_PASSWORD)
                .getHeader().clickSystemAdministrationLink()
                .clickAddUserButton()
                .createCompanyAdmin(email, "Password1!")
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickRefreshDataButton()
                .getTable().clickEditUserButton(email)
                .checkInactiveRadiobutton()
                .clickSaveChangesButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getAlertMessage()).hasText(SUCCESS_MESSAGE_USER_UPDATED);

        Allure.step("Verify: status of the user was changed");
        assertThat(teamPage.getTable().getCell("Status", user.email())).hasText("Inactive");
    }

    @Ignore
    @Test
    @TmsLink("476")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Deactivate and activate user under company admin")
    public void testDeactivateAndActivateCompanyUser(@Optional("UNAUTHORISED") String userRole) {
        String email = "deactivated@gmail.com";
        TestUtils.deleteUser(getApiRequestContext(), email);
        TestUtils.createCompany(getApiRequestContext(), ADMIN_COMPANY_NAME);
        TestUtils.createCompanyAdmin(getApiRequestContext(), ADMIN_COMPANY_NAME, ADMIN_EMAIL, ADMIN_PASSWORD);

        TeamPage teamPage = new AboutBlankPage(getPage())
                .navigate("/login")
                .loginAndChangePassword(ADMIN_EMAIL, ADMIN_PASSWORD)
                .getHeader().clickSystemAdministrationLink()
                .clickAddUserButton()
                .createCompanyAdmin(email, "Password1!")
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickRefreshDataButton()
                .getTable().clickDeactivateUserButton(email)
                .clickDeactivateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getAlertMessage()).hasText("SUCCESSUser was deactivated successfully");

        teamPage.clickRefreshDataButton();

        Allure.step("Verify: status of the user was changed");
        assertThat(teamPage.getTable().getCell("Status", user.email())).hasText("Inactive");

        Allure.step("Verify: deactivate user icon appears");
        assertThat(teamPage.getTable().getUserActivityIcon(email)).hasAttribute("data-icon", "check");

        teamPage.getTable().clickActivateUserButton(email)
                .clickActivateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getAlertMessage()).hasText("SUCCESSUser was activated successfully");

        teamPage.clickRefreshDataButton();

        Allure.step("Verify: status of the user was changed");
        assertThat(teamPage.getTable().getCell("Status", user.email())).hasText("Active");

        Allure.step("Verify: activate user icon appears");
        assertThat(teamPage.getTable().getUserActivityIcon(email)).hasAttribute("data-icon", "ban");
    }

    @Ignore
    @Test
    @TmsLink("554")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Reset company analyst password under company admin")
    public void testResetPasswordForCompanyAnalyst(@Optional("UNAUTHORISED") String userRole) {
        String email = "reset.password@gmail.com";
        TestUtils.deleteUser(getApiRequestContext(), email);
        TestUtils.createCompany(getApiRequestContext(), ADMIN_COMPANY_NAME);
        TestUtils.createCompanyAdmin(getApiRequestContext(), ADMIN_COMPANY_NAME, ADMIN_EMAIL, ADMIN_PASSWORD);

        TeamPage teamPage = new AboutBlankPage(getPage())
                .navigate("/login")
                .loginAndChangePassword(ADMIN_EMAIL, ADMIN_PASSWORD)
                .getHeader().clickSystemAdministrationLink()
                .clickAddUserButton()
                .createCompanyAdmin(email, "Password1!")
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickRefreshDataButton()
                .getTable().clickResetUserPasswordButton(email)
                .fillPasswordField("NewPassword1!")
                .clickResetButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getAlertMessage()).hasText("SUCCESSPassword was reseted successfully");

        teamPage.getHeader().clickLogOutButton()
                .fillEmailField(email)
                .fillPasswordField("NewPassword1!")
                .clickLoginButtonToChangePassword()
                .fillNewPasswordField("ChangedNewPassword1!")
                .fillRepeatNewPasswordField("ChangedNewPassword1!")
                .clickSaveButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getAlertMessage()).hasText("SUCCESSPassword is changed successfully");
    }

    @Ignore
    @Test
    @TmsLink("492")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Create company analyst")
    public void testCreateCompanyAnalystAndDeactivate(@Optional("UNAUTHORISED") String userRole) {
        String analystEmail = "company.analyst@gmail.com";
        String analystPassword = "CompanyAnalyst123!";
        String companyName = "Analyst company";
        TestUtils.deleteUser(getApiRequestContext(), analystEmail);
        TestUtils.deleteCompany(getApiRequestContext(), companyName);
        TestUtils.createCompany(getApiRequestContext(), companyName);
        TestUtils.createMerchantIfNeeded(getApiRequestContext(), companyName, "Business unit 1");
        TestUtils.createCompanyAdmin(getApiRequestContext(), companyName, ADMIN_EMAIL, ADMIN_PASSWORD);

        TeamPage teamPage = new AboutBlankPage(getPage())
                .navigate("/login")
                .loginAndChangePassword(ADMIN_EMAIL, ADMIN_PASSWORD)
                .getHeader().clickSystemAdministrationLink()
                .clickAddUserButton()
                .fillEmailField(analystEmail)
                .fillPasswordField(analystPassword)
                .setAllowedBusinessUnit("Business unit 1")
                .clickCreateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getAlertMessage()).hasText(SUCCESS_MESSAGE_USER_CREATED);

        teamPage.getAlert().waitUntilSuccessAlertIsGone()
                .clickRefreshDataButton();

        Allure.step("Verify: status of the user was changed");
        assertThat(teamPage.getTable().getCell("User role", analystEmail)).hasText("USER");

        Allure.step("Verify: status of the user");
        assertThat(teamPage.getTable().getCell("Status", user.email())).hasText("Active");

        Allure.step("Verify: deactivate user icon appears");
        assertThat(teamPage.getTable().getUserActivityIcon(analystEmail)).hasAttribute("data-icon", "ban");

        teamPage.getTable().clickDeactivateUserButton(analystEmail)
                .clickDeactivateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getAlertMessage()).hasText("SUCCESSUser was deactivated successfully");

        teamPage.clickRefreshDataButton();

        Allure.step("Verify: status of the user was changed");
        assertThat(teamPage.getTable().getCell("Status", user.email())).hasText("Inactive");

        Allure.step("Verify: deactivate user icon appears");
        assertThat(teamPage.getTable().getUserActivityIcon(analystEmail)).hasAttribute("data-icon", "check");

        LoginPage loginPage = teamPage.getHeader().clickLogOutButton()
                .loginAsDisabledUser(analystEmail, analystPassword);

        Allure.step("Verify: error message is displayed");
        assertThat(teamPage.getAlert().getAlertMessage()).hasText("ERRORUser is disabled.");

        DashboardPage dashboardPage = loginPage.login(ADMIN_EMAIL, ADMIN_PASSWORD)
                .getHeader().clickSystemAdministrationLink()
                .getTable().clickEditUserButton(analystEmail)
                .checkActiveRadiobutton()
                .clickSaveChangesButton()
                .getHeader().clickLogOutButton()
                .loginAndChangePassword(analystEmail, analystPassword);

        Allure.step("Verify: error message is displayed");
        assertThat(dashboardPage.getHeader().getUserMenuButton()).hasText(analystEmail.substring(0, 3));
    }

    @Test
    @TmsLink("531")
    @Epic("System/Team")
    @Feature("Status")
    @Description("Status filter correctly displays users with 'Active' or 'Inactive' status")
    public void testStatusFilterDisplaysCorrectUsers() {
        final String statusColumnName = "Status";
        final String companyAdmin = "dummyadmin@email.com";
        final String companyAdminPassword = ProjectProperties.getAdminPassword();
        final String companyName = "framework";

        TestUtils.createCompanyIfNeeded(getApiRequestContext(), companyName);
        TestUtils.createCompanyAdmin(getApiRequestContext(), companyName, companyAdmin, companyAdminPassword);

        TeamPage teamPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(companyName)
                .getTable().deactivateUser(companyAdmin)
                .clickStatusSelector()
                .selectActiveStatus();

        Allure.step("Verify: All visible users are 'Active' after applying 'Active' filter");
        assertTrue(teamPage.getTable().getColumnValues(statusColumnName)
                .stream().allMatch(value -> value.equals("Active")));

        teamPage.clickStatusSelector()
                .selectInactiveStatus();

        Allure.step("Verify: All visible users are 'Inactive' after applying Inactive filter");
        assertTrue(teamPage.getTable().getColumnValues(statusColumnName)
                .stream().allMatch(value -> value.equals("Inactive")));

        TestUtils.deleteUser(getApiRequestContext(), companyAdmin);
    }

    @Test
    @TmsLink("551")
    @Epic("System/Team")
    @Feature("Sorting in table")
    @Description("Verify that users can be sorted alphabetically")
    public void testCheckSortingListOfUsersAlphabetically() {
        final String companyAdmin = "dummyadmin@email.com";
        final String companyAdminPassword = ProjectProperties.getAdminPassword();
        final String companyName = "framework";

        TestUtils.createCompanyIfNeeded(getApiRequestContext(), companyName);
        TestUtils.createCompanyAdmin(getApiRequestContext(), companyName, companyAdmin, companyAdminPassword);

        List<String> sortedUsersAlphabetically = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(companyName)
                .getTable().clickSortIcon("Username")
                .getTable().getColumnValues("Username");

        List<String> expectedSortedList = new ArrayList<>(sortedUsersAlphabetically);
        Collections.sort(expectedSortedList);

        Assert.assertEquals(sortedUsersAlphabetically, expectedSortedList,
                "Список пользователей не отсортирован по алфавиту");
    }

    @Test
    @TmsLink("552")
    @Epic("System/Team")
    @Feature("Sorting in table")
    @Description("Verify that users can be sorted in reverse alphabetical order")
    public void testCheckSortingListOfUsersReverse() {
        final String companyAdmin = "dummyadmin@email.com";
        final String companyAdminPassword = ProjectProperties.getAdminPassword();
        final String companyName = "framework";

        TestUtils.createCompanyIfNeeded(getApiRequestContext(), companyName);
        TestUtils.createCompanyAdmin(getApiRequestContext(), companyName, companyAdmin, companyAdminPassword);

        List<String> sortedUsersReverseAlphabetically = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(companyName)
                .getTable().clickSortIcon("Username")
                .getTable().clickSortIcon("Username")
                .getTable().getColumnValues("Username");

        List<String> expectedSortedList = new ArrayList<>(sortedUsersReverseAlphabetically);
        expectedSortedList.sort(Collections.reverseOrder());

        Assert.assertEquals(sortedUsersReverseAlphabetically, expectedSortedList,
                "Список пользователей не отсортирован по алфавиту в обратном порядке");
    }
}
