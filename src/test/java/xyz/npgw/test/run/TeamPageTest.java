package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.common.entity.UserRole;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.AboutBlankPage;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.LoginPage;
import xyz.npgw.test.page.dialog.user.AddUserDialog;
import xyz.npgw.test.page.dialog.user.EditUserDialog;
import xyz.npgw.test.page.system.TeamPage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TeamPageTest extends BaseTest {

    private static final String COMPANY_NAME = "%s teamPageTest company".formatted(RUN_ID);
    private static final String ADMIN_COMPANY_NAME = "%s admin company".formatted(RUN_ID);
    private static final String ANALYST_COMPANY_NAME = "%s analyst company".formatted(RUN_ID);
    private static final String FRAMEWORK_COMPANY_NAME = "%s framework".formatted(RUN_ID);
    private static final String DUMMY_COMPANY = "%s dummy company".formatted(RUN_ID);
    private static final String ADMIN_EMAIL = "%s.admin@email.com".formatted(RUN_ID);
    private static final String ADMIN_PASSWORD = "AdminPassword1!";
    private static final String MERCHANT_TITLE = "Business unit 1";
    private static final String SUCCESS_MESSAGE_USER_CREATED = "SUCCESSUser was created successfully";
    private static final String SUCCESS_MESSAGE_USER_UPDATED = "SUCCESSUser was updated successfully";

    User user = User.newCompanyAnalyst(COMPANY_NAME, new String[]{"MerchantNameTest"}, "dummy@email.com");
    User updatedUser = User.newCompanyAdmin(COMPANY_NAME, false, "dummy@email.com");

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createBusinessUnit(getApiRequestContext(), "%s test run company".formatted(getUid()), MERCHANT_TITLE);
        TestUtils.createCompany(getApiRequestContext(), FRAMEWORK_COMPANY_NAME);
    }

    @Test
    @TmsLink("154")
    @Epic("System/Team")
    @Feature("Navigation")
    @Description("User navigate to 'System administration page'")
    public void testNavigateToSystemAdministrationPage() {
        TeamPage systemAdministrationPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink();

        Allure.step("Verify: System administration Page URL");
        assertThat(systemAdministrationPage.getPage()).hasURL(Constants.SYSTEM_PAGE_URL);

        Allure.step("Verify: System administration Page Title");
        assertThat(systemAdministrationPage.getPage()).hasTitle(Constants.SYSTEM_URL_TITLE);
    }

    @Test
    @TmsLink("298")
    @Epic("System/Team")
    @Feature("Add user")
    @Description("Add new system admin under super admin")
    public void testAddSystemAdmin() {
        TeamPage teamPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSelectCompany().selectCompany("super")
                .clickAddUserButton()
                .fillEmailField("%s.newsuper@email.com".formatted(getUid()))
                .fillPasswordField("Qwerty123!")
                .setUserRoleRadiobutton(UserRole.SUPER)
                .clickCreateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText(SUCCESS_MESSAGE_USER_CREATED);
    }

    @Test
    @TmsLink("298")
    @Epic("System/Team")
    @Feature("Add user")
    @Description("Add new company admin under super admin")
    public void testAddCompanyAdmin() {
        TeamPage teamPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(getCompanyName())
                .clickAddUserButton()
                .fillEmailField("%s.newadmin@email.com".formatted(getUid()))
                .fillPasswordField("Qwerty123!")
                .setUserRoleRadiobutton(UserRole.ADMIN)
                .clickCreateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText(SUCCESS_MESSAGE_USER_CREATED);
    }

    @Test
    @TmsLink("330")
    @Epic("System/Team")
    @Feature("Add user")
    @Description("Add a new user and verify that all fields, statuses, and icons are correctly displayed(e2e).")
    public void testAddCompanyAnalyst() {
        TestUtils.deleteUser(getApiRequestContext(), user.email());
        TestUtils.createCompanyIfNeeded(getApiRequestContext(), user.companyName());
        TestUtils.createBusinessUnitsIfNeeded(getApiRequestContext(), user);

        AddUserDialog addUserDialog = new DashboardPage(getPage())
                .refreshDashboard()
                .clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(user.companyName())
                .clickAddUserButton();

        Allure.step("Verify: 'Add user' header is displayed");
        assertThat(addUserDialog.getDialogHeader()).hasText("Add user");

        Allure.step("Verify: company name is pre-filled correctly ");
        assertThat(addUserDialog.getCompanyNameField()).hasValue(user.companyName());

        Allure.step("Verify: company name field is not editable");
        assertThat(addUserDialog.getCompanyNameField()).isDisabled();

        TeamPage teamPage = addUserDialog
                .fillEmailField(user.email())
                .fillPasswordField(user.password())
                .setStatusRadiobutton(user.enabled())
                .setUserRoleRadiobutton(user.userRole())
                .setAllowedBusinessUnits(user.merchantIds())
                .clickCreateButton();

        Allure.step("Verify: a success alert appears after user creation");
        assertThat(teamPage.getAlert().getMessage()).hasText(SUCCESS_MESSAGE_USER_CREATED);

        teamPage.clickRefreshDataButton();

        Allure.step("Verify: selected company is displayed in the 'Select company' field");
        assertThat(teamPage.getSelectCompany().getSelectCompanyField()).hasValue(user.companyName());

        Allure.step("Verify: new user has the role 'USER'");
        assertThat(teamPage.getTable().getCell(user.email(), "User role")).hasText("USER");

        Allure.step("Verify: new user has status 'Active'");
        assertThat(teamPage.getTable().getCell(user.email(), "Status")).hasText("Active");

        Allure.step("Verify: 'Deactivate' icon is shown for the new user");
        assertEquals(teamPage.getTable().getUserActivityIcon(user.email()).getAttribute("data-icon"), "ban");
    }

    @Test
    @TmsLink("331")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Edits the user's role and status, verifies the updates, and reactivates the user(e2e).")
    public void testEditUser() {
        TestUtils.deleteUser(getApiRequestContext(), user.email());
        TestUtils.createBusinessUnitsIfNeeded(getApiRequestContext(), user);

        EditUserDialog editUserDialog = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(user.companyName())
                .clickAddUserButton()
                .fillEmailField(user.email())
                .fillPasswordField(user.password())
                .checkCompanyAnalystRadiobutton()
                .setAllowedBusinessUnits(user.merchantIds())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickRefreshDataButton()
                .clickEditUserButton(user.email());

        Allure.step("Verify: 'Edit user' header is displayed");
        assertThat(editUserDialog.getDialogHeader()).hasText("Edit user");

        Allure.step("Verify: company name is pre-filled correctly ");
        assertThat(editUserDialog.getCompanyNameField()).hasValue(user.companyName());

        Allure.step("Verify: company name field is not editable");
        assertThat(editUserDialog.getCompanyNameField()).isDisabled();

        TeamPage teamPage = editUserDialog
                .setStatusRadiobutton(updatedUser.enabled())
                .unsetAllowedBusinessUnits(user.merchantIds())
                .setUserRoleRadiobutton(updatedUser.userRole())
                .clickSaveChangesButton()
                .clickRefreshDataButton();

        Allure.step("Verify: success alert appears after user update");
        assertThat(teamPage.getAlert().getMessage()).hasText(SUCCESS_MESSAGE_USER_UPDATED);

        Allure.step("Verify: selected company is displayed in the 'Select company' field");
        assertThat(teamPage.getSelectCompany().getSelectCompanyField()).hasValue(user.companyName());

        Allure.step("Verify: user role was updated to 'ADMIN'");
        assertThat(teamPage.getTable().getCell(user.email(), "User role")).hasText("ADMIN");

        Allure.step("Verify: Verify that user status was updated to 'Inactive'");
        assertThat(teamPage.getTable().getCell(user.email(), "Status")).hasText("Inactive");

        Allure.step("Verify: 'Activate' icon is shown for the user");
        assertEquals(teamPage.getTable().getUserActivityIcon(user.email()).getAttribute("data-icon"), "check");
    }

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
                .login(ADMIN_EMAIL, ADMIN_PASSWORD)
                .clickSystemAdministrationLink()
                .clickAddUserButton()
                .fillEmailField(email)
                .fillPasswordField("Password1!")
                .checkCompanyAdminRadiobutton()
                .clickCreateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText(SUCCESS_MESSAGE_USER_CREATED);
    }

    @Test
    @TmsLink("471")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Deactivate user by 'Change user activity button' and verify status change")
    public void testDeactivateUserViaChangeUserActivityButton() {
        TestUtils.deleteUser(getApiRequestContext(), user.email());
        TestUtils.createCompanyIfNeeded(getApiRequestContext(), user.companyName());
        TestUtils.createBusinessUnitsIfNeeded(getApiRequestContext(), user);

        TeamPage teamPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(user.companyName())
                .clickAddUserButton()
                .fillEmailField(user.email())
                .fillPasswordField(user.password())
                .checkCompanyAnalystRadiobutton()
                .setAllowedBusinessUnits(user.merchantIds())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickRefreshDataButton()
                .getTable().deactivateUser(user.email());

        Allure.step("Verify: selected company is displayed in the 'Select company' field");
        assertThat(teamPage.getSelectCompany().getSelectCompanyField()).hasValue(user.companyName());

        Allure.step("Verify: user status becomes 'Inactive' in the table");
        assertThat(teamPage.getTable().getCell(user.email(), "Status")).hasText("Inactive");

        Allure.step("Verify: 'Activate user' icon is shown for the user");
        assertEquals(teamPage.getTable().getUserActivityIcon(user.email()).getAttribute("data-icon"), "check");
    }

    @Test
    @TmsLink("475")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Edit user under company admin")
    public void testEditCompanyUser(@Optional("ADMIN") String userRole) {
        String email = "%s.edit@gmail.com"
                .formatted(new SimpleDateFormat("MMdd.HHmmss").format(new Date()));

        TeamPage teamPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .clickAddUserButton()
                .fillEmailField(email)
                .fillPasswordField("Password1!")
                .checkCompanyAdminRadiobutton()
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickRefreshDataButton()
                .getTable().clickEditUserButton(email)
                .checkInactiveRadiobutton()
                .clickSaveChangesButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText(SUCCESS_MESSAGE_USER_UPDATED);

        Allure.step("Verify: status of the user was changed");
        assertThat(teamPage.getTable().getCell(email, "Status")).hasText("Inactive");
    }

    @Test
    @TmsLink("476")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Deactivate and activate user under company admin")
    public void testDeactivateAndActivateCompanyUser(@Optional("ADMIN") String userRole) {
        String email = "%s.deactivate.and.activate@gmail.com"
                .formatted(new SimpleDateFormat("MMdd.HHmmss").format(new Date()));

        TeamPage teamPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .clickAddUserButton()
                .fillEmailField(email)
                .fillPasswordField("Password1!")
                .checkCompanyAdminRadiobutton()
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickRefreshDataButton()
                .getTable().clickDeactivateUserButton(email)
                .clickDeactivateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText("SUCCESSUser was deactivated successfully");

        teamPage
                .clickRefreshDataButton();

        Allure.step("Verify: status of the user was changed");
        assertThat(teamPage.getTable().getCell(email, "Status")).hasText("Inactive");

        Allure.step("Verify: deactivate user icon appears");
        assertThat(teamPage.getTable().getUserActivityIcon(email)).hasAttribute("data-icon", "check");

        teamPage
                .getTable().clickActivateUserButton(email)
                .clickActivateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText("SUCCESSUser was activated successfully");

        teamPage
                .clickRefreshDataButton();

        Allure.step("Verify: status of the user was changed");
        assertThat(teamPage.getTable().getCell(email, "Status")).hasText("Active");

        Allure.step("Verify: activate user icon appears");
        assertThat(teamPage.getTable().getUserActivityIcon(email)).hasAttribute("data-icon", "ban");
    }

    @Test
    @TmsLink("554")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Reset company analyst password under company admin")
    public void testResetPasswordForCompanyAnalyst(@Optional("ADMIN") String userRole) {
        String email = "%s.reset.password@gmail.com"
                .formatted(new SimpleDateFormat("MMdd.HHmmss").format(new Date()));

        TeamPage teamPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .clickAddUserButton()
                .fillEmailField(email)
                .fillPasswordField("Password1!")
                .checkCompanyAdminRadiobutton()
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickRefreshDataButton()
                .getTable().clickResetUserPasswordButton(email)
                .fillPasswordField("NewPassword1!")
                .clickResetButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText("SUCCESSPassword was reseted successfully");
//        TODO bug - correct past form of 'reseted' is reset

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
    @Description("Create company analyst under admin")
    public void testCreateCompanyAnalystAndDeactivate(@Optional("ADMIN") String userRole) {
        String analystEmail = "%s.company.analyst@gmail.com"
                .formatted(new SimpleDateFormat("MMdd.HHmmss").format(new Date()));
        String analystPassword = "CompanyAnalyst123!";

        TeamPage teamPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .clickAddUserButton()
                .fillEmailField(analystEmail)
                .fillPasswordField(analystPassword)
                .setAllowedBusinessUnit(MERCHANT_TITLE)
                .clickCreateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText(SUCCESS_MESSAGE_USER_CREATED);

        teamPage
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickRefreshDataButton();

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
                .clickRefreshDataButton();

        Allure.step("Verify: status of the user was changed");
        assertThat(teamPage.getTable().getCell(analystEmail, "Status")).hasText("Inactive");

        Allure.step("Verify: deactivate user icon appears");
        assertThat(teamPage.getTable().getUserActivityIcon(analystEmail)).hasAttribute("data-icon", "check");

        LoginPage loginPage = teamPage
                .clickLogOutButton()
                .loginAsDisabledUser(analystEmail, analystPassword);

        Allure.step("Verify: error message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText("ERRORUser is disabled.");

        DashboardPage dashboardPage = loginPage
                .login("%s.admin@email.com".formatted(getUid()), ProjectProperties.getUserPassword())
                .clickSystemAdministrationLink()
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

        Allure.step("Verify: error message is displayed");
        assertThat(dashboardPage.getUserMenuButton()).hasText(analystEmail.substring(0, 3));
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
                .clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(companyName)
                .getTable().deactivateUser(companyAdmin)
                .getSelectStatus().select("Active");

        Allure.step("Verify: All visible users are 'Active' after applying 'Active' filter");
        assertTrue(teamPage.getTable().getColumnValues(statusColumnName)
                .stream().allMatch(value -> value.equals("Active")));

        teamPage.getSelectStatus().select("Inactive");

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
        List<String> sortedUsersAlphabetically = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(FRAMEWORK_COMPANY_NAME)
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
        List<String> sortedUsersReverseAlphabetically = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(FRAMEWORK_COMPANY_NAME)
                .getTable().clickSortIcon("Username")
                .getTable().clickSortIcon("Username")
                .getTable().getColumnValues("Username");

        List<String> expectedSortedList = new ArrayList<>(sortedUsersReverseAlphabetically);
        expectedSortedList.sort(Collections.reverseOrder());

        Assert.assertEquals(sortedUsersReverseAlphabetically, expectedSortedList,
                "Список пользователей не отсортирован по алфавиту в обратном порядке");
    }

    @Test
    @TmsLink("612")
    @Epic("System/Team")
    @Feature("Add user")
    @Description("Adding a user with an existing email address results in an error message.")
    public void testAddUserWithExistingEmail() {
        final String companyAdmin = "dummyadmin@email.com";

        TestUtils.deleteUser(getApiRequestContext(), companyAdmin);
        TestUtils.createCompanyIfNeeded(getApiRequestContext(), DUMMY_COMPANY);
        TestUtils.createCompanyAdmin(getApiRequestContext(), DUMMY_COMPANY, companyAdmin,
                ProjectProperties.getAdminPassword());

        AddUserDialog addUserDialog = new DashboardPage(getPage())
                .refreshDashboard()
                .clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(DUMMY_COMPANY)
                .clickAddUserButton()
                .fillEmailField(companyAdmin)
                .fillPasswordField(ProjectProperties.getSuperPassword())
                .checkActiveRadiobutton()
                .checkSystemAdminRadiobutton()
                .clickCreateButtonAndTriggerError();

        Allure.step("Verify: Error message is displayed for existing user");
        assertThat(addUserDialog.getAlert().getMessage()).hasText("ERRORUser account already exists");
    }

    @Test
    @TmsLink("683")
    @Epic("System/Team")
    @Feature("Reset filter")
    @Description("'Reset filter' button resets the 'Status' filter to 'All' and clears the selected company")
    public void testResetFilter() {
        final List<String> statusList = List.of("Active", "Inactive");

        TeamPage teamPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink();

        Allure.step("Verify: 'Status' filter displays 'All' by default");
        assertThat(teamPage.getSelectStatus().getStatusValue()).hasText("All");

        Allure.step("Verify: 'Select company' filter is empty by default");
        assertThat(teamPage.getSelectCompany().getSelectCompanyField()).isEmpty();

        for (String status : statusList) {
            teamPage
                    .getSelectCompany().selectCompany(COMPANY_NAME)
                    .getSelectStatus().selectTransactionStatuses(status)
                    .clickResetFilterButton();

            Allure.step("Verify: 'Status' filter displays 'All' after reset");
            assertThat(teamPage.getSelectStatus().getStatusValue()).hasText("All");

            Allure.step("Verify: 'Select company' filter is empty after reset");
            assertThat(teamPage.getSelectCompany().getSelectCompanyField()).isEmpty();
        }
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        TestUtils.deleteCompany(getApiRequestContext(), ADMIN_COMPANY_NAME);
        TestUtils.deleteCompany(getApiRequestContext(), ANALYST_COMPANY_NAME);
        TestUtils.deleteCompany(getApiRequestContext(), FRAMEWORK_COMPANY_NAME);
        TestUtils.deleteCompany(getApiRequestContext(), DUMMY_COMPANY);
        super.afterClass();
    }
}
