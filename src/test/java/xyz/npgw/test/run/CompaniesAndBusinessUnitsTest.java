package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTestForSingleLogin;
import xyz.npgw.test.common.entity.Address;
import xyz.npgw.test.common.entity.Company;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.dashboard.SuperDashboardPage;
import xyz.npgw.test.page.dialog.company.AddCompanyDialog;
import xyz.npgw.test.page.dialog.company.DeleteCompanyDialog;
import xyz.npgw.test.page.dialog.merchant.AddBusinessUnitDialog;
import xyz.npgw.test.page.dialog.merchant.DeleteBusinessUnitDialog;
import xyz.npgw.test.page.dialog.merchant.EditBusinessUnitDialog;
import xyz.npgw.test.page.system.SuperCompaniesAndBusinessUnitsPage;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static xyz.npgw.test.common.Constants.COMPANY_NAME_FOR_TEST_RUN;
import static xyz.npgw.test.common.Constants.MERCHANT_ID_FOR_TEST_RUN;
import static xyz.npgw.test.common.Constants.TOOLTIPSCONTENT;

public class CompaniesAndBusinessUnitsTest extends BaseTestForSingleLogin {

    private static final String COMPANY_NAME_TEST = "%s company name test".formatted(RUN_ID);
    private static final String COMPANY_DELETION_BLOCKED_NAME = "%s deletion-blocked company".formatted(RUN_ID);
    private static final String COMPANY_NAME_REQUIRED_FIELD = "%s company required field".formatted(RUN_ID);
    private static final String COMPANY_TYPE = "CompanyType";
    private static final String BUSINESS_UNIT_NAME = "Business unit name test";
    private static final String BUSINESS_UNIT_NAME_EDITED = "Edited Business unit name test";
    private static final String ADMIN_EMAIL = "%s.admin123@email.com".formatted(TestUtils.now());

    Company company = new Company(
            COMPANY_NAME_TEST, "Company Type Test",
            new Address("Warwick", "PA",
                    "19876", "US",
                    "+1234567", "+1234567", "+1234567"),
            "Description Test",
            "https://www.test.com", "James Smith", "test@yahoo.com",
            true, true
    );

    Company editedCompany = new Company(
            COMPANY_NAME_TEST, "Edited company type",
            new Address("Delmor", "CA",
                    "19000", "AL",
                    "+2222222", "+2222222", "+2222222"),
            "Edited Description Test",
            "https://www.editedtest.com", "Catty Smith", "editedtest@yahoo.com",
            false, false
    );

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createCompany(getApiRequestContext(), COMPANY_DELETION_BLOCKED_NAME);
    }

    @Test
    @TmsLink("691")
    @Epic("System/Companies and business units")
    @Feature("Settings")
    @Description("The company info block can be hidden and shown via settings.")
    public void testToggleCompanyInfoVisibilityViaSettings() {
        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .clickSettings()
                .checkHideCompanyInfo();

        Allure.step("Verify: company info block is hidden after selecting 'Hide' in settings");
        assertThat(companiesAndBusinessUnitsPage.getCompanyInfoBlock()).isHidden();

        companiesAndBusinessUnitsPage
                .checkShowCompanyInfo();

        Allure.step("Verify: company info block is visible again after selecting 'Show' in settings");
        assertThat(companiesAndBusinessUnitsPage.getCompanyInfoBlock()).isVisible();
    }

    @Test
    @TmsLink("246")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Validates successful company creation and correct field persistence.")
    public void testAddCompany() {
        AddCompanyDialog addCompanyDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton();

        Allure.step("Verify: 'Create' button is disabled before filling required fields");
        assertThat(addCompanyDialog.getCreateButton()).isDisabled();

        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = addCompanyDialog
                .fillCompanyNameField(company.companyName())
                .fillCompanyTypeField(company.companyType())
                .fillCompanyDescriptionField(company.description())
                .fillCompanyWebsiteField(company.website())
                .fillCompanyPrimaryContactField(company.primaryContact())
                .fillCompanyEmailField(company.email())
                .setApiActiveCheckbox(company.isApiActive())
                .setPortalActiveCheckbox(company.isPortalActive())
                .fillCompanyCountryField(company.companyAddress().country())
                .fillCompanyStateField(company.companyAddress().state())
                .fillCompanyZipField(company.companyAddress().zip())
                .fillCompanyCityField(company.companyAddress().city())
                .fillCompanyPhoneField(company.companyAddress().phone())
                .fillCompanyMobileField(company.companyAddress().mobile())
                .fillCompanyFaxField(company.companyAddress().fax())
                .clickCreateButton()
                .getAlert().clickCloseButton()
                .clickOnResetFilterButton()
                .getSelectCompany().selectCompany(company.companyName());

        Allure.step("Verify: selected company is shown in the input field");
        assertThat(companiesAndBusinessUnitsPage.getSelectCompany().getSelectCompanyField())
                .hasValue(company.companyName());

        Allure.step("Verify: name field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getName()).hasValue(company.companyName());

        Allure.step("Verify: type field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getType()).hasValue(company.companyType());

        Allure.step("Verify: description field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getDescription()).hasValue(company.description());

        Allure.step("Verify: website field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getWebsite()).hasValue(company.website());

        Allure.step("Verify: primary contact field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getPrimaryContact()).hasValue(company.primaryContact());

        Allure.step("Verify: email field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getEmail()).hasValue(company.email());

        Allure.step("Verify: 'API active' checkbox is checked");
        assertThat(companiesAndBusinessUnitsPage.getApiActive()).isChecked();

        Allure.step("Verify: 'Portal active' checkbox is checked");
        assertThat(companiesAndBusinessUnitsPage.getPortalActive()).isChecked();

        Allure.step("Verify: phone field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getPhone()).hasValue(company.companyAddress().phone());

        Allure.step("Verify: mobile field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getMobile()).hasValue(company.companyAddress().mobile());

        Allure.step("Verify: fax field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getFax()).hasValue(company.companyAddress().fax());

        Allure.step("Verify: country field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getCountry()).hasValue(company.companyAddress().country());

        Allure.step("Verify: state field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getState()).hasValue(company.companyAddress().state());

        Allure.step("Verify: ZIP code field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getZip()).hasValue(company.companyAddress().zip());

        Allure.step("Verify: city field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getCity()).hasValue(company.companyAddress().city());
    }

    @Test(dependsOnMethods = "testAddCompany")
    @TmsLink("241")
    @Epic("System/Companies and business units")
    @Feature("Add business unit")
    @Description("Verify that a new business unit wasn't added once click 'Close' button")
    public void testCloseButtonAndDiscardChanges() {
        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(company.companyName())
                .clickOnAddBusinessUnitButton()
                .fillBusinessUnitNameField("BU-2")
                .clickCloseButton();

        Allure.step("Verify: The table is empty and 'No rows to display.' is displayed");
        assertThat(companiesAndBusinessUnitsPage.getMerchantsTable()).containsText("No rows to display.");
    }

    @Test(dependsOnMethods = "testCloseButtonAndDiscardChanges")
    @TmsLink("290")
    @Epic("System/Companies and business units")
    @Feature("Add business unit")
    @Description("Validates successful business unit addition to company")
    public void testAddBusinessUnit() {
        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab();

        Allure.step("Verify: 'Add business unit' button is disabled before selecting a company");
        assertThat(companiesAndBusinessUnitsPage.getAddBusinessUnitButton())
                .isDisabled();

        AddBusinessUnitDialog addBusinessUnitDialog = companiesAndBusinessUnitsPage
                .getSelectCompany().selectCompany(company.companyName())
                .clickOnAddBusinessUnitButton();

        Allure.step("Verify: 'Add business unit' dialog is opened");
        assertThat(addBusinessUnitDialog.getGetAddMerchantDialogHeader())
                .hasText("Add business unit");

        Allure.step("Verify: Company name is pre-filled correctly");
        assertThat(addBusinessUnitDialog.getCompanyNameField())
                .hasValue(company.companyName());

        Allure.step("Verify: 'Company name' field is non-editable");
        assertThat(addBusinessUnitDialog.getCompanyNameField()).hasAttribute("aria-readonly", "true");

        companiesAndBusinessUnitsPage = addBusinessUnitDialog
                .fillBusinessUnitNameField(BUSINESS_UNIT_NAME)
                .clickCreateButton();

        Allure.step("Verify: Success alert is shown after business unit is added");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("SUCCESSBusiness unit was created successfully");

        Allure.step("Verify: Selected company is preserved after creation");
        assertThat(companiesAndBusinessUnitsPage.getSelectCompany().getSelectCompanyField())
                .hasValue(company.companyName());

        Allure.step("Verify: New business unit name appears in the list");
        assertThat(companiesAndBusinessUnitsPage.getTable().getFirstRowCell("Business unit name"))
                .hasText(BUSINESS_UNIT_NAME);

        Allure.step("Verify: Merchant ID is displayed");
        assertThat(companiesAndBusinessUnitsPage.getTable().getFirstRowCell("Business unit ID"))
                .containsText("id.merchant");
    }

    @Test(dependsOnMethods = "testAddBusinessUnit")
    @TmsLink("794")
    @Epic("System/Companies and business units")
    @Feature("Edit business unit")
    @Description("Editing a business unit updates its name while preserving the same ID")
    public void testEditBusinessUnit() {
        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(company.companyName());

        Locator originalBusinessUnitId = companiesAndBusinessUnitsPage
                .getTable().getCell(BUSINESS_UNIT_NAME, "Business unit ID");

        companiesAndBusinessUnitsPage
                .getTable().clickEditBusinessUnitButton(BUSINESS_UNIT_NAME)
                .fillBusinessUnitNameField(BUSINESS_UNIT_NAME_EDITED)
                .clickSaveChangesButton();

        Allure.step("Verify: the success alert is displayed with correct message");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("SUCCESSBusiness unit was updated successfully");

        Locator editedBusinessUnitIdLocator = companiesAndBusinessUnitsPage.getTable()
                .getCell(BUSINESS_UNIT_NAME_EDITED, "Business unit ID");

        Allure.step("Verify: the Business Unit ID remains the same after editing name");
        assertThat(editedBusinessUnitIdLocator).hasText(originalBusinessUnitId.innerText());
    }

    @Test(dependsOnMethods = "testEditBusinessUnit")
    @TmsLink("728")
    @Epic("System/Companies and business units")
    @Feature("Delete company")
    @Description("Verify that company cannot be deleted if there are associated business units")
    public void testCannotDeleteCompanyWithAssociatedBusinessUnit() {
        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(company.companyName())
                .clickDeleteSelectedCompany()
                .clickDeleteButton();

        Allure.step("Verify: error message is shown when trying to delete a company with business unit");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("ERRORCompany could not be deleted: there are still merchants associated with it");

    }

    @Test(dependsOnMethods = "testCannotDeleteCompanyWithAssociatedBusinessUnit")
    @TmsLink("722")
    @Epic("System/Companies and business units")
    @Feature("Delete business unit")
    @Description("Verify that business unit can be deleted")
    public void testDeleteBusinessUnit() {
        DeleteBusinessUnitDialog deleteBusinessUnitDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(company.companyName())
                .getTable().clickDeleteBusinessUnitButton(BUSINESS_UNIT_NAME_EDITED);

        Allure.step("Verify: confirmation question contains correct Business unit name");
        assertThat(deleteBusinessUnitDialog.getConfirmationQuestion())
                .hasText("Are you sure you want to delete business unit %s?".formatted(BUSINESS_UNIT_NAME_EDITED));

        Allure.step("Verify: dialog header text");
        assertThat(deleteBusinessUnitDialog.getDialogHeader())
                .hasText("Delete business unit");

        SuperCompaniesAndBusinessUnitsPage superCompaniesAndBusinessUnitsPage = deleteBusinessUnitDialog
                .clickDeleteButton();

        Allure.step("Verify: alert text");
        assertThat(superCompaniesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("SUCCESSBusiness unit was deleted successfully");
    }

    @Test(dependsOnMethods = "testDeleteBusinessUnit")
    @TmsLink("266")
    @Epic("System/Companies and business units")
    @Feature("Edit company")
    @Description("Edit company info and save")
    public void testEditCompanyInfoAndSave() {
        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(company.companyName())
                .clickEditCompanyButton()
                .fillCompanyTypeField(editedCompany.companyType())
                .fillCompanyDescriptionField(editedCompany.description())
                .fillCompanyWebsiteField(editedCompany.website())
                .fillCompanyPrimaryContactField(editedCompany.primaryContact())
                .fillCompanyEmailField(editedCompany.email())
                .setApiActiveCheckbox(editedCompany.isApiActive())
                .setPortalActiveCheckbox(editedCompany.isPortalActive())
                .fillCompanyCountryField(editedCompany.companyAddress().country())
                .fillCompanyStateField(editedCompany.companyAddress().state())
                .fillCompanyZipField(editedCompany.companyAddress().zip())
                .fillCompanyCityField(editedCompany.companyAddress().city())
                .fillCompanyPhoneField(editedCompany.companyAddress().phone())
                .fillCompanyMobileField(editedCompany.companyAddress().mobile())
                .fillCompanyFaxField(editedCompany.companyAddress().fax())
                .clickSaveChangesButton();

        Allure.step("Verify: success message is displayed");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("SUCCESSCompany was updated successfully");

        companiesAndBusinessUnitsPage
                .getAlert().clickCloseButton();

        Allure.step("Verify: name field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getName()).hasValue(editedCompany.companyName());

        Allure.step("Verify: type field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getType()).hasValue(editedCompany.companyType());

        Allure.step("Verify: description field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getDescription()).hasValue(editedCompany.description());

        Allure.step("Verify: website field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getWebsite()).hasValue(editedCompany.website());

        Allure.step("Verify: primary contact field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getPrimaryContact()).hasValue(editedCompany.primaryContact());

        Allure.step("Verify: email field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getEmail()).hasValue(editedCompany.email());

        Allure.step("Verify: 'API active' checkbox is checked");
        assertThat(companiesAndBusinessUnitsPage.getApiActive()).not().isChecked();

        Allure.step("Verify: 'Portal active' checkbox is checked");
        assertThat(companiesAndBusinessUnitsPage.getPortalActive()).not().isChecked();

        Allure.step("Verify: phone field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getPhone()).hasValue(editedCompany.companyAddress().phone());

        Allure.step("Verify: mobile field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getMobile()).hasValue(editedCompany.companyAddress().mobile());

        Allure.step("Verify: fax field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getFax()).hasValue(editedCompany.companyAddress().fax());

        Allure.step("Verify: country field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getCountry()).hasValue(editedCompany.companyAddress().country());

        Allure.step("Verify: state field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getState()).hasValue(editedCompany.companyAddress().state());

        Allure.step("Verify: ZIP code field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getZip()).hasValue(editedCompany.companyAddress().zip());

        Allure.step("Verify: city field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getCity()).hasValue(editedCompany.companyAddress().city());
    }

    @Test(dependsOnMethods = "testEditCompanyInfoAndSave")
    @TmsLink("723")
    @Epic("System/Companies and business units")
    @Feature("Delete company")
    @Description("Verify that company can be deleted")
    public void testDeleteCompany() {
        DeleteCompanyDialog deleteCompanyDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(editedCompany.companyName())
                .clickDeleteSelectedCompany();

        Allure.step("Verify: dialog header is 'Delete company'");
        assertThat(deleteCompanyDialog.getDialogHeader()).hasText("Delete company");

        Allure.step("Verify: confirmation question contains correct company name");
        assertThat(deleteCompanyDialog.getConfirmationQuestion())
                .hasText("Are you sure you want to delete company %s?".formatted(editedCompany.companyName()));

        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = deleteCompanyDialog
                .clickDeleteButton();

        Allure.step("Verify: the success alert appears after deleting the company");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("SUCCESSCompany was deleted successfully");

        companiesAndBusinessUnitsPage
                .getAlert().clickCloseButton()
                .waitForCompanyAbsence(getApiRequestContext(), editedCompany.companyName());

        Allure.step("Verify: the deleted company is no longer present on the page");
        assertThat(companiesAndBusinessUnitsPage.getPageContent())
                .hasText("Select company name to view merchants");

        getPage().waitForTimeout(2000);

        Allure.step("Verify: the deleted company is no longer present in the dropdown list");
        assertFalse(companiesAndBusinessUnitsPage.getSelectCompany().isCompanyPresent(editedCompany.companyName()));
    }


    @Test
    @Epic("System/Companies and business units")
    @Feature("Delete company")
    @Description("Verify that company cannot be deleted if there are users assigned to it")
    public void testCannotDeleteCompanyWithAssignedUser() {
        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickTeamTab()
                .getSelectCompany().selectCompany(COMPANY_DELETION_BLOCKED_NAME)
                .clickAddUserButton()
                .fillEmailField(ADMIN_EMAIL)
                .fillPasswordField("Qwerty123!")
                .checkCompanyAdminRadiobutton()
                .clickCreateButton()
                .getAlert().clickCloseButton()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(COMPANY_DELETION_BLOCKED_NAME)
                .clickDeleteSelectedCompany()
                .clickDeleteButton();

        Allure.step("Verify: error message is shown when trying to delete a company with users");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("ERRORCompany could not be deleted: there are still users associated with it");
    }

    @Test
    @TmsLink("232")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Error is displayed when trying to create a company with an already existing name")
    public void testAddCompanyWithSameName() {
        AddCompanyDialog addCompanyDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(COMPANY_NAME_FOR_TEST_RUN)
                .fillCompanyTypeField(COMPANY_TYPE)
                .clickCreateButtonAndTriggerError();

        Allure.step("Verify: error message is displayed for duplicate company name");
        assertThat(addCompanyDialog.getAlert().getMessage())
                .containsText("Company with name {%s} already exists.".formatted(COMPANY_NAME_FOR_TEST_RUN));
    }

    @Test
    @TmsLink("480")
    @Epic("System/Companies and business units")
    @Feature("Reset filter")
    @Description("Verify default filter state was applied once reset")
    public void testResetAppliedFilter() {
        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .clickOnResetFilterButton();

        Allure.step("Verify: Ensure the prompt appears when no company is selected");
        assertThat(companiesAndBusinessUnitsPage.getPageContent())
                .hasText("Select company name to view merchants");

        Allure.step("Verify: the 'Company' input field is empty after reset");
        assertThat(companiesAndBusinessUnitsPage.getSelectCompany().getSelectCompanyField()).isEmpty();
    }


    @Test
    @TmsLink("191")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Error message is shown for company name is shorter than 4 or longer than 100 characters.")
    public void testVerifyErrorMessageForInvalidCompanyNameLength() {
        AddCompanyDialog addCompanyDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField("1");

        Allure.step("Verify: 'Create' button is disabled for name length = 1");
        assertThat(addCompanyDialog.getCreateButton()).isDisabled();

        addCompanyDialog.fillCompanyNameField("123");

        Allure.step("Verify: 'Create' button is disabled for name length = 3");
        assertThat(addCompanyDialog.getCreateButton()).isDisabled();

        addCompanyDialog.fillCompanyNameField("1234");

        Allure.step("Verify: 'Create' button is enabled for name length = 4");
        assertThat(addCompanyDialog.getCreateButton()).isEnabled();

        addCompanyDialog.fillCompanyNameField("1".repeat(120));

        Allure.step("Verify: 'Create' button is enabled for name length = 100");
        assertThat(addCompanyDialog.getCreateButton()).isEnabled();
        Allure.step("Verify: 'Maximal Company name length = 100");
        assertEquals(addCompanyDialog.getCompanyNameValue().length(), 100);
    }

    @Test
    @TmsLink("206")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Verify required field states, placeholders, and button state in empty 'Add Company' form.")
    public void testAddCompanyFormValidationWhenEmpty() {
        AddCompanyDialog addCompanyDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton();

        Allure.step("Verify: 'Add company' dialog is displayed");
        assertThat(addCompanyDialog.getDialogHeader()).hasText("Add company");

        Allure.step("Verify: 'Company name' field is marked invalid");
        assertThat(addCompanyDialog.getCompanyNameField()).hasAttribute("aria-invalid", "true");

        Allure.step("Verify: all placeholders are correct for each field");
        assertEquals(addCompanyDialog.getAllPlaceholders(), List.of(
                "Enter name",
                "Enter type",
                "Enter company description",
                "Enter website",
                "Enter primary contact",
                "Enter email",
                "Enter country",
                "Enter state",
                "Enter ZIP",
                "Enter city",
                "Enter phone",
                "Enter mobile",
                "Enter fax"
        ));

        Allure.step("Verify: 'Create' button is disabled when required fields are not filled.");
        assertThat(addCompanyDialog.getCreateButton()).isDisabled();

        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = addCompanyDialog
                .clickCloseButton();

        Allure.step("Verify: the 'Add Company' dialog is no longer visible");
        assertThat(companiesAndBusinessUnitsPage.getAddCompanyDialog()).isHidden();
    }

    @Test(dataProvider = "getCompanyNameInvalidSpecialCharacters", dataProviderClass = TestDataProvider.class)
    @TmsLink("215")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Error is displayed when trying to create a company with special characters in the name.")
    public void testErrorIsDisplayedWhenCreatingCompanyWithSpecialCharacters(String character) {
        AddCompanyDialog addCompanyDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField("Company" + character)
                .fillCompanyTypeField(COMPANY_TYPE);

        Allure.step("Verify: 'Create' button is disabled");
        assertThat(addCompanyDialog.getCreateButton()).isDisabled();

        Allure.step("Verify: 'Company name' field is marked invalid");
        assertThat(addCompanyDialog.getCompanyNameField()).hasAttribute("aria-invalid", "true");
    }

    @Test(dataProvider = "getInvalidCompanyNamesByLengthAndChar", dataProviderClass = TestDataProvider.class)
    @TmsLink("261")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Error message when trying to create a company with invalid length and special characters.")
    public void testErrorForInvalidCompanyNameLengthAndCharacters(String name, String character) {
        AddCompanyDialog addCompanyDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(name + character)
                .fillCompanyTypeField(COMPANY_TYPE);

        Allure.step("Verify: 'Create' button is disabled");
        assertThat(addCompanyDialog.getCreateButton()).isDisabled();

        Allure.step("Verify: 'Company name' field is marked invalid");
        assertThat(addCompanyDialog.getCompanyNameField()).hasAttribute("aria-invalid", "true");
    }

    @Test
    @TmsLink("223")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Company can be added by filling out only required field")
    public void testAddCompanyByFillOnlyRequiredField() {
        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(COMPANY_NAME_REQUIRED_FIELD)
                .clickCreateButton();

        Allure.step("Verify: company creation success message is displayed");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("SUCCESSCompany was created successfully");
    }

    @Test
    @TmsLink("387")
    @TmsLink("501")
    @TmsLink("515")
    @TmsLink("528")
    @Epic("System/Companies and business units")
    @Feature("Edit business unit")
    @Description("Verify that all elements of dialog are displayed properly")
    public void testElementsOfEditBusinessUnitDialog() {
        EditBusinessUnitDialog editBusinessUnitDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getTable().clickEditBusinessUnitButton(MERCHANT_ID_FOR_TEST_RUN);

        Allure.step("Verify: the header contains the expected title text");
        assertThat(editBusinessUnitDialog.getDialogHeader()).hasText("Edit business unit");

        Allure.step("Verify: Company name is pre-filled correctly");
        assertThat(editBusinessUnitDialog.getCompanyNameField()).hasValue(COMPANY_NAME_FOR_TEST_RUN);

        Allure.step("Verify: Company name field is read-only");
        assertThat(editBusinessUnitDialog.getCompanyNameField()).hasAttribute("aria-readonly", "true");

        Allure.step("Verify: all labels are correct for each field");
        assertThat(editBusinessUnitDialog.getFieldLabel()).hasText(new String[]{"Company name", "Business unit name"});

        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = editBusinessUnitDialog
                .clickCloseButton();

        Allure.step("Verify: Dialog 'Edit business unit' is not displayed after clicking on the 'Close' button");
        assertThat(companiesAndBusinessUnitsPage.getEditBusinessUnitDialog()).isHidden();

        companiesAndBusinessUnitsPage
                .getTable().clickEditBusinessUnitButton(MERCHANT_ID_FOR_TEST_RUN)
                .clickCloseIcon();

        Allure.step("Verify: Dialog 'Edit business unit' is not displayed after clicking on the 'Close' icon");
        assertThat(companiesAndBusinessUnitsPage.getEditBusinessUnitDialog()).isHidden();
    }

    @Test
    @TmsLink("1192")
    @Epic("System/Companies and business units")
    @Feature("Tooltips")
    @Description("Verify, that contents of Tooltips, that appear after hovering on the icon-buttons, are correct")
    public void testTooltipsContent() {
        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab();

        String iconName;
        String tooltip;
        List<Locator> initialCommonIcons = companiesAndBusinessUnitsPage.getInitialCommonIcon().all();
        for (Locator icon : initialCommonIcons) {
            iconName =  companiesAndBusinessUnitsPage.getIconName(icon);
            Allure.step("Hover on '" + iconName + "' icon");
            icon.hover();

            tooltip = companiesAndBusinessUnitsPage.getTooltip().last().textContent();
            Allure.step("Verify, over '" + iconName + "' appears '" + tooltip + "'");
            assertEquals(TOOLTIPSCONTENT.get(icon.getAttribute("data-testid")), tooltip);
        }

        companiesAndBusinessUnitsPage
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN);

        List<Locator> commonIconButtons = companiesAndBusinessUnitsPage.getCommonIconButton().all();
        for (Locator icon : commonIconButtons) {
            iconName =  companiesAndBusinessUnitsPage.getIconName(icon);
            Allure.step("Hover on '" + iconName + "' icon");
            icon.hover();

            tooltip = companiesAndBusinessUnitsPage.getTooltip().last().textContent();
            Allure.step("Verify, over '" + iconName + "' appears '" + tooltip + "'");
            assertEquals(TOOLTIPSCONTENT.get(icon.getAttribute("data-testid")), tooltip);
        }

        List<Locator> rowIconButtons = companiesAndBusinessUnitsPage
                .getTable().getRowIcon(Constants.BUSINESS_UNIT_FOR_TEST_RUN).all();
        for (Locator rowIcon : rowIconButtons) {
            iconName = companiesAndBusinessUnitsPage.getTable().getIconName(rowIcon);
            Allure.step("Hover on '" + iconName + "' icon");
            rowIcon.hover();

            tooltip = companiesAndBusinessUnitsPage.getTooltip().last().textContent();
            Allure.step("Verify, over '" + iconName + "' appears '" + tooltip + "'");
            assertEquals(TOOLTIPSCONTENT.get(rowIcon.getAttribute("data-testid")), tooltip);
        }
    }

    @AfterClass
    @Override
    protected void afterClass() {
        User.delete(getApiRequestContext(), ADMIN_EMAIL);
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_DELETION_BLOCKED_NAME);
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME_REQUIRED_FIELD);
        super.afterClass();
    }
}
