package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.opentest4j.AssertionFailedError;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.Address;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.entity.Company;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.dialog.company.AddCompanyDialog;
import xyz.npgw.test.page.dialog.merchant.AddBusinessUnitDialog;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

public class AddCompanyDialogTest extends BaseTest {

    private static final String COMPANY_NAME = "CompanyName";
    private static final String COMPANY_TYPE = "CompanyType";
    private static final String SUCCESS_MESSAGE_COMPANY_CREATED = "SUCCESSCompany was created successfully";

    Company company = new Company(
            "CompanyNameTest", "Company Type Test",
            new Address("USA", "PA",
                    "19876", "Warwick",
                    "2151111111", "2152222222", "222333444"),
            "Description Test",
            "https://www.test.com", "James Smith", "test@yahoo.com",
            true, true
    );

    @Test
    @TmsLink("160")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Verify that the 'Add Company' window displays the correct title in the header.")
    public void testVerifyAddCompanyWindowTitle() {
        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton();

        Allure.step("Verify: the header contains the expected title text");
        assertThat(addCompanyDialog.getDialogHeader()).hasText("Add company");
    }

    @Test
    @TmsLink("189")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Verify that the placeholder text for each field is correct.")
    public void testVerifyPlaceholders() {
        List<String> expectedPlaceholders = List.of(
                "Enter company name",
                "Enter type",
                "Enter company description",
                "Enter company website",
                "Enter company primary contact",
                "Enter company email",
                "Enter country",
                "Enter state",
                "Enter ZIP",
                "Enter city",
                "Enter phone",
                "Enter mobile",
                "Enter fax"
        );

        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton();

        Allure.step("Verify: all placeholders are correct for each field");
        assertEquals(addCompanyDialog.getAllFieldPlaceholders(), expectedPlaceholders);
    }

    @Test(dataProvider = "getInvalidCompanyNameLengths", dataProviderClass = TestDataProvider.class)
    @TmsLink("191")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Error message is shown for company name is shorter than 4 or longer than 100 characters.")
    public void testVerifyErrorMessageForInvalidCompanyNameLength(String name) {
        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(name)
                .fillCompanyTypeField(COMPANY_TYPE)
                .clickCreateButtonAndTriggerError();

        Allure.step("Verify: error message for invalid company name: '{name}' is displayed");
        assertThat(addCompanyDialog
                .getAlert().getAlertMessage())
                .containsText("Invalid companyName: '%s'. It must contain between 4 and 100 characters".formatted(name));
    }

    @Test(dataProvider = "getEmptyRequiredFields", dataProviderClass = TestDataProvider.class)
    @TmsLink("206")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("'Create' button is disabled when required fields are not filled.")
    public void testCreateButtonDisabledWhenRequiredFieldsAreEmpty(String name, String type) {
        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(name)
                .fillCompanyTypeField(type);

        Allure.step("Verify: 'Create' button is disabled when required fields are not filled.");
        assertThat(addCompanyDialog.getCreateButton()).isDisabled();
    }

    @Test
    @TmsLink("184")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Verify that clicking the Close button successfully closes the 'Add Company' dialog.")
    public void testVerifyCloseAddCompanyDialogWhenCloseButtonIsClicked() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
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
        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
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
        String fullName = name + character;

        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(fullName)
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
    @Description("Company can be added by filling out required fields")
    public void testAddCompanyByFillRequiredFields() {
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(COMPANY_NAME)
                .fillCompanyTypeField(COMPANY_TYPE)
                .clickCreateButton();

        Allure.step("Verify: company creation success message is displayed");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getAlertMessage()).hasText(
                SUCCESS_MESSAGE_COMPANY_CREATED);
    }

    @Test
    @TmsLink("224")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Added company appears in the 'Select company' dropdown list")
    public void testVerifyCompanyPresenceInDropdown() {
        TestUtils.createCompanyIfNeeded(getApiRequestContext(), COMPANY_NAME);

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(COMPANY_NAME);

        Allure.step("Verify: company is present in the 'Select company' field");
        assertThat(companiesAndBusinessUnitsPage.getSelectCompany().getSelectCompanyField()).hasValue(COMPANY_NAME);
    }

    @Test
    @TmsLink("232")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Error is displayed when trying to create a company with an already existing name")
    public void testAddCompanyWithSameName() {
        TestUtils.createCompanyIfNeeded(getApiRequestContext(), COMPANY_NAME);

        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(COMPANY_NAME)
                .fillCompanyTypeField(COMPANY_TYPE)
                .clickCreateButtonAndTriggerError();

        Allure.step("Verify: error message is displayed for duplicate company name");
        assertThat(addCompanyDialog
                .getAlert().getAlertMessage())
                .containsText("Company with name {%s} already exists.".formatted(COMPANY_NAME));
    }

    @Test(expectedExceptions = AssertionFailedError.class)
    @TmsLink("227")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Company creation with Cyrillic symbols")
    public void testAddCompanyWithCyrillicSymbols() {
        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField("Амазон")
                .fillCompanyTypeField("ООО")
                .fillCompanyDescriptionField("Описание деятельности компании Амазон")
                .fillCompanyWebsiteField("амазон.рф")
                .fillCompanyPrimaryContactField("Даниил Иванов")
                .fillCompanyEmailField("amazon@gmail.com")
                .fillCompanyCountryField("Россия")
                .fillCompanyStateField("Московская")
                .fillCompanyZipField("876905")
                .fillCompanyCityField("Москва")
                .fillCompanyPhoneField("8(495) 223-56-11")
                .fillCompanyMobileField("+7 (951) 789-78-76")
                .fillCompanyFaxField("84952235611");

        Allure.step("Verify: Create button is enabled");
        assertThat(addCompanyDialog.getCreateButton()).isEnabled();
    }

    @Test
    @TmsLink("228")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Company creation with Latin symbols")
    public void testAddCompanyWithAllFilledFields() {
        final String companyName = "Google";
        TestUtils.deleteCompany(getApiRequestContext(), companyName);

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(companyName)
                .fillCompanyTypeField("LLC")
                .fillCompanyDescriptionField("Description of company business model")
                .fillCompanyWebsiteField("google.com")
                .fillCompanyPrimaryContactField("John Doe")
                .fillCompanyEmailField("google@gmail.com")
                .fillCompanyCountryField("France")
                .fillCompanyStateField("Provence")
                .fillCompanyZipField("75001")
                .fillCompanyCityField("Paris")
                .fillCompanyPhoneField("+33 1 22-83-56-11")
                .fillCompanyMobileField("+33 7 22-83-56-11")
                .fillCompanyFaxField("84952235611")
                .clickCreateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getAlertMessage()).hasText(
                SUCCESS_MESSAGE_COMPANY_CREATED);
    }

    @Test
    @TmsLink("246")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Validates successful company creation and correct field persistence (E2E test).")
    public void testAddCompanyEndToEndTest() {
        TestUtils.deleteCompany(getApiRequestContext(), company.companyName());

        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton();

        Allure.step("Verify: 'Add company' dialog is displayed");
        assertThat(addCompanyDialog.getDialogHeader()).hasText("Add company");

        Allure.step("Verify: 'Company name' field is marked as invalid");
        assertEquals(addCompanyDialog.getCompanyNameField().getAttribute("aria-invalid"), "true");

        Allure.step("Verify: 'Company type' field is marked as invalid");
        assertEquals(addCompanyDialog.getCompanyTypeField().getAttribute("aria-invalid"), "true");

        Allure.step("Verify: 'Create' button is disabled before filling required fields");
        assertThat(addCompanyDialog.getCreateButton()).isDisabled();

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = addCompanyDialog
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
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickOnResetFilterButton()
                .getSelectCompany().selectCompany(company.companyName());

        Allure.step("Verify: selected company is shown in the input field");
        assertThat(companiesAndBusinessUnitsPage.getSelectCompany().getSelectCompanyField())
                .hasValue(company.companyName());

        Allure.step("Verify: description field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getDescriptionFromCompanyInfoSection())
                .hasValue(company.description());

        Allure.step("Verify: website field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getWebsiteFromCompanyInfoSection())
                .hasValue(company.website());

        Allure.step("Verify: primary contact field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getPrimaryContactFromCompanyInfoSection())
                .hasValue(company.primaryContact());

        Allure.step("Verify: email field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getEmailFromCompanyInfoSection())
                .hasValue(company.email());

        Allure.step("Verify: 'API active' checkbox is checked");
        assertThat(companiesAndBusinessUnitsPage.getApiActiveCheckboxFromCompanyInfoSection())
                .isChecked();

        Allure.step("Verify: 'Portal active' checkbox is checked");
        assertThat(companiesAndBusinessUnitsPage.getPortalActiveCheckboxFromCompanyInfoSection())
                .isChecked();

        Allure.step("Verify: phone field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getPhoneFromCompanyInfoSection())
                .hasValue(company.companyAddress().phone());

        Allure.step("Verify: mobile field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getMobileFromCompanyInfoSection())
                .hasValue(company.companyAddress().mobile());

        Allure.step("Verify: fax field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getFaxFromCompanyInfoSection())
                .hasValue(company.companyAddress().fax());

        Allure.step("Verify: country field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getCountryFromCompanyInfoSection())
                .hasValue(company.companyAddress().country());

        Allure.step("Verify: state field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getStateFromCompanyInfoSection())
                .hasValue(company.companyAddress().state());

        Allure.step("Verify: ZIP code field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getZipFromCompanyInfoSection())
                .hasValue(company.companyAddress().zip());

        Allure.step("Verify: city field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getCityFromCompanyInfoSection())
                .hasValue(company.companyAddress().city());
    }

    @Test
    @TmsLink("290")
    @Epic("System/Companies and business units")
    @Feature("Add business unit")
    @Description("Validates successful business unit addition to company (E2E test).")
    public void testAddBusinessUnitEndToEndTest() {
        TestUtils.createCompanyIfNeeded(getApiRequestContext(), COMPANY_NAME);

        BusinessUnit businessUnit = new BusinessUnit("MerchantNameTest");

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab();

        Allure.step("Verify: 'Add business unit' button is disabled before selecting a company");
        assertThat(companiesAndBusinessUnitsPage.getAddBusinessUnitButton()).isDisabled();

        AddBusinessUnitDialog addBusinessUnitDialog = companiesAndBusinessUnitsPage
                .getSelectCompany().selectCompany(company.companyName())
                .clickOnAddBusinessUnitButton();

        Allure.step("Verify: 'Add business unit' dialog is opened");
        assertThat(addBusinessUnitDialog.getGetAddMerchantDialogHeader()).hasText("Add business unit");

        Allure.step("Verify: Company name is pre-filled correctly");
        assertThat(addBusinessUnitDialog.getCompanyNameField()).hasValue(company.companyName());

        addBusinessUnitDialog.clickCreateButtonAndTriggerError();

        Allure.step("Verify: Validation error is shown when merchant name is not filled");
        assertThat(addBusinessUnitDialog
                .getAlert().getAlertMessage())
                .containsText("Enter merchant name");

        companiesAndBusinessUnitsPage = addBusinessUnitDialog
                .fillBusinessUnitNameField(businessUnit.merchantName())
                .clickCreateButton();

        Allure.step("Verify: Success alert is shown after business unit is added");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getAlertMessage()).hasText(
                "SUCCESSBusiness unit was created successfully");

        Allure.step("Verify: Selected company is preserved after creation");
        assertThat(companiesAndBusinessUnitsPage
                .getSelectCompany().getSelectCompanyField()).hasValue(company.companyName());

        Allure.step("Verify: New business unit name appears in the list");
        assertThat(companiesAndBusinessUnitsPage.getBusinessUnitNameData()).hasText(businessUnit.merchantName());

        Allure.step("Verify: Merchant ID is displayed");
        assertThat(companiesAndBusinessUnitsPage.getMerchantIdData()).containsText("id.merchant");
    }
}
