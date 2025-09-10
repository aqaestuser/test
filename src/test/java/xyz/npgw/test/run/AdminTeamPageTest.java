package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTestForSingleLogin;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.dashboard.AdminDashboardPage;
import xyz.npgw.test.page.dialog.user.AdminAddUserDialog;
import xyz.npgw.test.page.dialog.user.AdminEditUserDialog;
import xyz.npgw.test.page.system.AdminTeamPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static xyz.npgw.test.common.Constants.TOOLTIPSCONTENT;

public class AdminTeamPageTest extends BaseTestForSingleLogin {

    private static final String MERCHANT_TITLE = "Business unit 1";
    private static final String SUCCESS_MESSAGE_USER_CREATED = "SUCCESSUser was created successfully";
    private static final String SUCCESS_MESSAGE_USER_UPDATED = "SUCCESSUser was updated successfully";
    private static final String SUCCESS_MESSAGE_USER_DELETED = "SUCCESSUser was deleted successfully";
    private static String companyAdminEmail;
    private static String companyAnalystEmail;

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createBusinessUnit(getApiRequestContext(), getCompanyName(), MERCHANT_TITLE);
        super.openSiteAccordingRole();
    }

    @Test
    @TmsLink("154")
    @Epic("System/Team")
    @Feature("Navigation")
    @Description("User navigate to 'System administration page'")
    public void testNavigateToSystemAdministrationPageAsAdmin() {
        AdminTeamPage adminTeamPage = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab();

        Allure.step("Verify: System administration Page URL");
        assertThat(adminTeamPage.getPage()).hasURL(Constants.SYSTEM_PAGE_URL);

        Allure.step("Verify: System administration Page Title");
        assertThat(adminTeamPage.getPage()).hasTitle(Constants.SYSTEM_URL_TITLE);
    }

    @Test
    @TmsLink("298")
    @Epic("System/Team")
    @Feature("Add user")
    @Description("Add new company admin as company admin")
    public void testAddCompanyAdminAsAdmin() {
        companyAdminEmail = "%s.newadmin@email.com".formatted(TestUtils.now());

        AdminTeamPage teamPage = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab()
                .clickAddUserButton()
                .fillEmailField(companyAdminEmail)
                .fillPasswordField("Qwerty123!")
                .checkCompanyAdminRadiobutton()
                .clickCreateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText(SUCCESS_MESSAGE_USER_CREATED);
    }

    @Test(dependsOnMethods = "testAddCompanyAdminAsAdmin")
    @TmsLink("747")
    @Epic("System/Team")
    @Feature("Delete user")
    @Description("Verify company admin can be deleted by another company admin")
    public void testDeleteCompanyAdminAsAdmin() {
        AdminTeamPage teamPage = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab()
                .getTable().clickDeleteUserIcon(companyAdminEmail)
                .clickDeleteButton();

        Allure.step("Verify: success alert appears after deleting the company admin");
        assertThat(teamPage.getAlert().getMessage()).hasText(SUCCESS_MESSAGE_USER_DELETED);

        teamPage
                .getAlert().clickCloseButton()
                .waitForUserAbsence(getApiRequestContext(), companyAdminEmail, getCompanyName());

        Allure.step("Verify: deleted company admin is no longer present in the users table");
        assertFalse(teamPage.getTable().getColumnValuesFromAllPages("Username").contains(companyAdminEmail));
    }

    @Test
    @TmsLink("330")
    @Epic("System/Team")
    @Feature("Add user")
    @Description("Add a new company analyst as company admin and verify all fields, statuses, and icons")
    public void testAddCompanyAnalystAsAdmin() {
        companyAnalystEmail = "%s.newuser@email.com".formatted(TestUtils.now());

        AdminAddUserDialog addUserDialog = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab()
                .clickAddUserButton();

        Allure.step("Verify: 'Add user' header is displayed");
        assertThat(addUserDialog.getDialogHeader()).hasText("Add user");

        Allure.step("Verify: company name is pre-filled correctly ");
        assertThat(addUserDialog.getCompanyNameField()).hasValue(getCompanyName());

        Allure.step("Verify: company name field is not editable");
        assertThat(addUserDialog.getCompanyNameField()).isDisabled();

        AdminTeamPage teamPage = addUserDialog
                .fillEmailField(companyAnalystEmail)
                .fillPasswordField("Qwerty123!")
                .checkCompanyAnalystRadiobutton()
                .checkAllowedBusinessUnitCheckbox(MERCHANT_TITLE)
                .clickCreateButton();

        Allure.step("Verify: a success alert appears after user creation");
        assertThat(teamPage.getAlert().getMessage()).hasText(SUCCESS_MESSAGE_USER_CREATED);

        teamPage
                .getAlert().clickCloseButton()
                .waitForUserPresence(getApiRequestContext(), companyAnalystEmail, getCompanyName());

        Allure.step("Verify: new user has the role 'USER'");
        assertThat(teamPage.getTable().getCell(companyAnalystEmail, "User role")).hasText("USER");

        Allure.step("Verify: new user has status 'Active'");
        assertThat(teamPage.getTable().getCell(companyAnalystEmail, "Status")).hasText("Active");

        Allure.step("Verify: 'Deactivate' icon is shown for the new user");
        assertEquals(teamPage.getTable().getUserActivityIcon(companyAnalystEmail).getAttribute("data-icon"), "ban");
    }

    @Test(dependsOnMethods = "testAddCompanyAnalystAsAdmin")
    @TmsLink("748")
    @Epic("System/Team")
    @Feature("Delete user")
    @Description("Verify company analyst can be deleted by company admin")
    public void testDeleteCompanyAnalystAsAdmin() {
        AdminTeamPage teamPage = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab()
                .getTable().clickDeleteUserIcon(companyAnalystEmail)
                .clickDeleteButton();

        Allure.step("Verify: success alert appears after deleting the company analyst");
        assertThat(teamPage.getAlert().getMessage()).hasText(SUCCESS_MESSAGE_USER_DELETED);

        teamPage
                .getAlert().clickCloseButton()
                .waitForUserAbsence(getApiRequestContext(), companyAnalystEmail, getCompanyName());

        Allure.step("Verify: deleted company analyst is no longer present in the users table");
        assertFalse(teamPage.getTable().getColumnValuesFromAllPages("Username").contains(companyAnalystEmail));
    }

    @Test
    @TmsLink("331")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Change company analyst role to company admin and change it's status as company admin")
    public void testEditUserAsAdmin() {
        String email = "%s.edit.analyst@email.com".formatted(TestUtils.now());

        AdminEditUserDialog editUserDialog = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab()
                .clickAddUserButton()
                .fillEmailField(email)
                .fillPasswordField("Qwerty123!")
                .checkCompanyAnalystRadiobutton()
                .checkAllowedBusinessUnitCheckbox(MERCHANT_TITLE)
                .clickCreateButton()
                .waitForUserPresence(getApiRequestContext(), email, getCompanyName())
                .getTable().clickEditUserButton(email);

        Allure.step("Verify: 'Edit user' header is displayed");
        assertThat(editUserDialog.getDialogHeader()).hasText("Edit user");

        Allure.step("Verify: company name is pre-filled correctly ");
        assertThat(editUserDialog.getCompanyNameField()).hasValue(getCompanyName());

        Allure.step("Verify: company name field is not editable");
        assertThat(editUserDialog.getCompanyNameField()).isDisabled();

        AdminTeamPage teamPage = editUserDialog
                .checkInactiveRadiobutton()
                .uncheckAllowedBusinessUnitCheckbox(MERCHANT_TITLE)
                .checkCompanyAdminRadiobutton()
                .clickSaveChangesButton();

        Allure.step("Verify: success alert appears after user update");
        assertThat(teamPage.getAlert().getMessage()).hasText(SUCCESS_MESSAGE_USER_UPDATED);

        teamPage
                .getAlert().clickCloseButton()
                .waitForUserDeactivation(getApiRequestContext(), email, getCompanyName());

        Allure.step("Verify: user role was updated to 'ADMIN'");
        assertThat(teamPage.getTable().getCell(email, "User role")).hasText("ADMIN");

        Allure.step("Verify: Verify that user status was updated to 'Inactive'");
        assertThat(teamPage.getTable().getCell(email, "Status")).hasText("Inactive");

        Allure.step("Verify: 'Activate' icon is shown for the user");
        assertThat(teamPage.getTable().getUserActivityIcon(email)).hasAttribute("data-icon", "check");
    }

    @Test
    @TmsLink("474")
    @Epic("System/Team")
    @Feature("Add user")
    @Description("Create new company admin user under admin")
    public void testCreateCompanyAdminUserAsAdmin() {
        String email = "%s.email@gmail.com".formatted(TestUtils.now());

        AdminTeamPage teamPage = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab()
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
    @Description("Deactivate company analyst by 'Change user activity button' and verify status change as admin")
    public void testDeactivateUserViaChangeUserActivityButtonAsAdmin() {
        String email = "%s.change@gmail.com".formatted(TestUtils.now());

        AdminTeamPage teamPage = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab()
                .clickAddUserButton()
                .fillEmailField(email)
                .fillPasswordField("Qwerty123!")
                .checkCompanyAnalystRadiobutton()
                .checkAllowedBusinessUnitCheckbox(MERCHANT_TITLE)
                .clickCreateButton()
                .waitForUserPresence(getApiRequestContext(), email, getCompanyName())
                .getTable().clickDeactivateUserButton(email)
                .clickDeactivateButton()
                .waitForUserDeactivation(getApiRequestContext(), email, getCompanyName());

        Allure.step("Verify: user status becomes 'Inactive' in the table");
        assertThat(teamPage.getTable().getCell(email, "Status")).hasText("Inactive");

        Allure.step("Verify: 'Activate user' icon is shown for the user");
        assertThat(teamPage.getTable().getUserActivityIcon(email)).hasAttribute("data-icon", "check");
    }

    @Test
    @TmsLink("475")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Edit company analyst as company admin")
    public void testEditCompanyUserAsAdmin() {
        String email = "%s.edit@gmail.com".formatted(TestUtils.now());

        AdminTeamPage teamPage = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab()
                .clickAddUserButton()
                .fillEmailField(email)
                .fillPasswordField("Password1!")
                .checkCompanyAdminRadiobutton()
                .clickCreateButton()
                .waitForUserPresence(getApiRequestContext(), email, getCompanyName())
                .getTable().clickEditUserButton(email)
                .checkInactiveRadiobutton()
                .clickSaveChangesButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText(SUCCESS_MESSAGE_USER_UPDATED);

        teamPage
                .waitForUserDeactivation(getApiRequestContext(), email, getCompanyName());

        Allure.step("Verify: status of the user was changed");
        assertThat(teamPage.getTable().getCell(email, "Status")).hasText("Inactive");
    }

    @Test
    @TmsLink("476")
    @Epic("System/Team")
    @Feature("Edit user")
    @Description("Deactivate and activate company admin under company admin")
    public void testDeactivateAndActivateCompanyUserAsAdmin() {
        String email = "%s.deactivate.and.activate@gmail.com".formatted(TestUtils.now());

        AdminTeamPage teamPage = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab()
                .clickAddUserButton()
                .fillEmailField(email)
                .fillPasswordField("Password1!")
                .checkCompanyAdminRadiobutton()
                .clickCreateButton()
                .waitForUserPresence(getApiRequestContext(), email, getCompanyName())
                .getTable().clickDeactivateUserButton(email)
                .clickDeactivateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(teamPage.getAlert().getMessage()).hasText("SUCCESSUser was deactivated successfully");

        teamPage
                .getAlert().clickCloseButton()
                .waitForUserDeactivation(getApiRequestContext(), email, getCompanyName());

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
                .getAlert().clickCloseButton()
                .waitForUserActivation(getApiRequestContext(), email, getCompanyName());

        Allure.step("Verify: status of the user was changed");
        assertThat(teamPage.getTable().getCell(email, "Status")).hasText("Active");

        Allure.step("Verify: activate user icon appears");
        assertThat(teamPage.getTable().getUserActivityIcon(email)).hasAttribute("data-icon", "ban");
    }

    @Test
    @TmsLink("531")
    @Epic("System/Team")
    @Feature("Status")
    @Description("Status filter correctly displays users with 'Active' or 'Inactive' status")
    public void testStatusFilterDisplaysCorrectUsersAsAdmin() {
        final String statusColumnName = "Status";
        final String email = "%s.filter@email.com".formatted(TestUtils.now());

        AdminTeamPage teamPage = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab()
                .clickAddUserButton()
                .fillEmailField(email)
                .fillPasswordField("Password1!")
                .checkCompanyAdminRadiobutton()
                .clickCreateButton()
                .waitForUserPresence(getApiRequestContext(), email, getCompanyName())
                .getTable().clickDeactivateUserButton(email)
                .clickDeactivateButton()
                .waitForUserDeactivation(getApiRequestContext(), email, getCompanyName())
                .getSelectStatus().select("Active");

        Allure.step("Verify: All visible users are 'Active' after applying 'Active' filter");
        assertTrue(teamPage.getTable().getColumnValues(statusColumnName)
                .stream().allMatch(value -> value.equals("Active")));

        teamPage
                .getSelectStatus().select("Inactive");

        Allure.step("Verify: All visible users are 'Inactive' after applying Inactive filter");
        assertTrue(teamPage.getTable().getColumnValues(statusColumnName)
                .stream().allMatch(value -> value.equals("Inactive")));
    }

    @Test
    @TmsLink("551")
    @Epic("System/Team")
    @Feature("Sorting in table")
    @Description("Verify that users can be sorted alphabetically")
    public void testCheckSortingListOfUsersAlphabeticallyAsAdmin() {
        List<String> sortedUsersAlphabetically = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab()
                .getTable().clickColumnHeader("Username")
                .getTable().getColumnValues("Username");

        List<String> expectedSortedList = new ArrayList<>(sortedUsersAlphabetically);
        Collections.sort(expectedSortedList);

        Assert.assertEquals(sortedUsersAlphabetically, expectedSortedList,
                "The list of users is not sorted alphabetically.");
    }

    @Test
    @TmsLink("552")
    @Epic("System/Team")
    @Feature("Sorting in table")
    @Description("Verify that users can be sorted in reverse alphabetical order")
    public void testCheckSortingListOfUsersReverseAsAdmin() {
        List<String> sortedUsersReverseAlphabetically = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab()
                .getTable().clickColumnHeader("Username")
                .getTable().clickColumnHeader("Username")
                .getTable().getColumnValues("Username");

        List<String> expectedSortedList = new ArrayList<>(sortedUsersReverseAlphabetically);
        expectedSortedList.sort(Collections.reverseOrder());

        Assert.assertEquals(sortedUsersReverseAlphabetically, expectedSortedList,
                "The list of users is not sorted in reverse alphabetical order.");
    }

    @Test
    @TmsLink("612")
    @Epic("System/Team")
    @Feature("Add user")
    @Description("Adding a user with an existing email address results in an error message.")
    public void testAddUserWithExistingEmailAsAdmin() {
        final String companyAdmin = "%s.companydmin@email.com".formatted(TestUtils.now());

        AdminAddUserDialog addUserDialog = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab()
                .clickAddUserButton()
                .fillEmailField(companyAdmin)
                .fillPasswordField("Qwerty123!")
                .checkActiveRadiobutton()
                .checkCompanyAdminRadiobutton()
                .clickCreateButton()
                .getAlert().clickCloseButton()
                .waitForUserPresence(getApiRequestContext(), companyAdmin, getCompanyName())
                .clickAddUserButton()
                .fillEmailField(companyAdmin)
                .fillPasswordField("Qwerty123!")
                .checkCompanyAdminRadiobutton()
                .clickCreateButtonAndTriggerError();

        Allure.step("Verify: Error message is displayed for existing user");
        assertThat(addUserDialog.getAlert().getMessage()).hasText("ERRORUser account already exists");
    }

    @Test
    @TmsLink("683")
    @Epic("System/Team")
    @Feature("Reset filter")
    @Description("'Reset filter' button resets the 'Status' filter to 'All' and clears the selected company")
    public void testResetFilterAsAdmin() {
        AdminTeamPage teamPage = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab();

        Allure.step("Verify: 'Status' filter displays 'All' by default");
        assertThat(teamPage.getSelectStatus().getStatusValue()).hasText("All");

        List.of("Active", "Inactive").forEach(status -> {
            teamPage
                    .getSelectStatus().selectTransactionStatuses(status)
                    .clickResetFilterButton();

            Allure.step("Verify: 'Status' filter displays 'All' after reset");
            assertThat(teamPage.getSelectStatus().getStatusValue()).hasText("All");
        });
    }

    @Test
    @TmsLink("1188")
    @Epic("System/Team")
    @Feature("Tooltips")
    @Description("For Admin : contents of Tooltips, that appear after hovering on the icon-buttons, are correct")
    public void testTeamTooltipsContentAsAdmin() {
        String email = "%s.deactivate.and.activate@gmail.com".formatted(TestUtils.now());

        AdminTeamPage teamPage = new AdminDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab()
                .clickAddUserButton()
                .fillEmailField(email)
                .fillPasswordField("Qwerty123!")
                .checkCompanyAnalystRadiobutton()
                .checkAllowedBusinessUnitCheckbox(MERCHANT_TITLE)
                .clickCreateButton()
                .waitForUserPresence(getApiRequestContext(), email, getCompanyName());

        String iconAttributeValue;
        String tooltip;
        List<Locator> panelIcons = teamPage.getCommonPanelIcon().all();
        for (Locator icon : panelIcons) {
            iconAttributeValue = icon.getAttribute("data-icon");
            Allure.step("Hover on '" + iconAttributeValue + "' icon");
            icon.hover();

            tooltip = teamPage.getTooltip().last().textContent();
            Allure.step("Verify, over '" + iconAttributeValue + "' appears '" + tooltip);
            assertEquals(TOOLTIPSCONTENT.get(iconAttributeValue), tooltip);
        }

        List<Locator> rowIcons = teamPage.getTable().getRowIcon(email).all();
        for (Locator rowIcon : rowIcons) {
            iconAttributeValue = rowIcon.getAttribute("data-icon");
            Allure.step("Hover on '" + iconAttributeValue + "' icon");
            rowIcon.hover();

            tooltip = teamPage.getTooltip().last().textContent();
            Allure.step("Verify, over '" + iconAttributeValue + "' appears '" + tooltip);
            assertEquals(TOOLTIPSCONTENT.get(iconAttributeValue), tooltip);
        }

        teamPage.getTable().clickDeactivateUserButton(email)
                .clickDeactivateButton()
                .waitForUserDeactivation(getApiRequestContext(), email, getCompanyName());
        for (Locator rowIcon : rowIcons) {
            iconAttributeValue = rowIcon.getAttribute("data-icon");
            Allure.step("Hover on " + iconAttributeValue + " icon");
            rowIcon.hover();

            tooltip = teamPage.getTooltip().last().textContent();
            Allure.step("Verify, over " + iconAttributeValue + " appears '" + tooltip);
            assertEquals(TOOLTIPSCONTENT.get(iconAttributeValue), tooltip);
        }
        User.delete(getApiRequestContext(), email);
    }
}
