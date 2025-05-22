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
import xyz.npgw.test.common.entity.Company;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.dialog.company.AddCompanyDialog;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

public class AddCompanyDialogTest extends BaseTest {

    private static final String COMPANY_NAME = "CompanyName";
    private static final String COMPANY_TYPE = "CompanyType";

    Company company = new Company(
            "CompanyNameTest", "Company Type Test",
            new Address("USA", "PA",
                    "19876", "Warwick",
                    "+1234567", "+1234567", "+1234567"),
            "Description Test",
            "https://www.test.com", "James Smith", "test@yahoo.com",
            true, true
    );

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
                .clickSystemAdministrationLink()
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
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(name)
                .fillCompanyTypeField(COMPANY_TYPE)
                .clickCreateButtonAndTriggerError();

        Allure.step("Verify: error message for invalid company name: '{name}' is displayed");
        assertThat(addCompanyDialog.getAlert().getMessage()).containsText(
                "Invalid companyName: '%s'. It must contain between 4 and 100 characters".formatted(name));
    }

    @Test
    @TmsLink("206")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("'Create' button is disabled when required fields are not filled.")
    public void testCreateButtonDisabledWhenRequiredFieldsAreEmpty() {
        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton();

        Allure.step("Verify: 'Company name' field is marked invalid");
        assertThat(addCompanyDialog.getCompanyNameField()).hasAttribute("aria-invalid", "true");

        Allure.step("Verify: 'Company type' field is marked invalid");
        assertThat(addCompanyDialog.getCompanyTypeField()).hasAttribute("aria-invalid", "true");

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
                .clickSystemAdministrationLink()
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
                .clickSystemAdministrationLink()
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
                .clickSystemAdministrationLink()
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
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(COMPANY_NAME)
                .fillCompanyTypeField(COMPANY_TYPE)
                .clickCreateButton();

        Allure.step("Verify: company creation success message is displayed");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("SUCCESSCompany was created successfully");

        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
    }

    @Test
    @TmsLink("232")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Error is displayed when trying to create a company with an already existing name")
    public void testAddCompanyWithSameName() {
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME);

        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(COMPANY_NAME)
                .fillCompanyTypeField(COMPANY_TYPE)
                .clickCreateButtonAndTriggerError();

        Allure.step("Verify: error message is displayed for duplicate company name");
        assertThat(addCompanyDialog
                .getAlert().getMessage())
                .containsText("Company with name {%s} already exists.".formatted(COMPANY_NAME));

        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
    }

    @Test(expectedExceptions = AssertionFailedError.class)
    @TmsLink("227")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Company creation with Cyrillic symbols")
    public void testAddCompanyWithCyrillicSymbols() {
        String companyName = "Амазон";

        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(companyName)
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

        TestUtils.deleteCompany(getApiRequestContext(), companyName);
    }

    @Test
    @TmsLink("246")
    @Epic("System/Companies and business units")
    @Feature("Add company")
    @Description("Validates successful company creation and correct field persistence (E2E test).")
    public void testAddCompanyEndToEndTest() {
        TestUtils.deleteCompany(getApiRequestContext(), company.companyName());

        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton();

        Allure.step("Verify: 'Add company' dialog is displayed");
        assertThat(addCompanyDialog.getDialogHeader()).hasText("Add company");

        Allure.step("Verify: 'Company name' field is marked as invalid");
        assertThat(addCompanyDialog.getCompanyNameField()).hasAttribute("aria-invalid", "true");

        Allure.step("Verify: 'Company type' field is marked as invalid");
        assertThat(addCompanyDialog.getCompanyTypeField()).hasAttribute("aria-invalid", "true");

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
        assertThat(companiesAndBusinessUnitsPage
                .getSelectCompany().getSelectCompanyField()).hasValue(company.companyName());

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

        TestUtils.deleteCompany(getApiRequestContext(), company.companyName());
    }
}
