package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.opentest4j.AssertionFailedError;
import org.testng.annotations.Test;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.dto.Company;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.page.AddCompanyDialog;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.systemadministration.CompaniesAndBusinessUnitsPage;
import xyz.npgw.test.utils.CompanyUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

public class AddCompanyDialogTest extends BaseTest {

    private static final String COMPANY_NAME = "CompanyName";
    private static final String COMPANY_TYPE = "CompanyType";

    private void deleteCompany(String name) {
        getApiRequestContext().delete(ProjectProperties.getBaseUrl() + "/portal-v1/company/"
                + URLEncoder.encode(name, StandardCharsets.UTF_8));
    }

    @Test
    @TmsLink("160")
    @Epic("Companies and business units")
    @Feature("Add Company")
    @Description("Verify that the 'Add Company' window displays the correct title in the header.")
    public void testVerifyAddCompanyWindowTitle() {
        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton();

        Allure.step("Verify: the header contains the expected title text");
        assertThat(addCompanyDialog.getAddCompanyDialogHeader()).hasText("Add company");
    }

    @Test
    @TmsLink("189")
    @Epic("Companies and business units")
    @Feature("Add Company")
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
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton();

        Allure.step("Verify: all placeholders are correct for each field");
        assertEquals(addCompanyDialog.getAllFieldPlaceholders(), expectedPlaceholders);
    }

    @Test(dataProvider = "getInvalidCompanyNameLengths", dataProviderClass = TestDataProvider.class)
    @TmsLink("191")
    @Epic("Companies and business units")
    @Feature("Add Company")
    @Description("Error message is shown for company name is shorter than 4 or longer than 100 characters.")
    public void testVerifyErrorMessageForInvalidCompanyNameLength(String name) {
        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton()
                .fillCompanyNameField(name)
                .fillCompanyTypeField(COMPANY_TYPE)
                .clickCreateButtonAndTriggerError();

        Allure.step("Verify: error message for invalid company name: '{name}' is displayed");
        assertThat(addCompanyDialog.getAlertMessage()).containsText(
                "Invalid companyName: '%s'. It must contain between 4 and 100 characters".formatted(name));
    }

    @Test(dataProvider = "getEmptyRequiredFields", dataProviderClass = TestDataProvider.class)
    @TmsLink("206")
    @Epic("Companies and business units")
    @Feature("Add Company")
    @Description("'Create' button is disabled when required fields are not filled.")
    public void testCreateButtonDisabledWhenRequiredFieldsAreEmpty(String name, String type) {
        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton()
                .fillCompanyNameField(name)
                .fillCompanyTypeField(type);

        Allure.step("Verify: 'Create' button is disabled when required fields are not filled.");
        assertThat(addCompanyDialog.getCreateButton()).isDisabled();
    }

    @Test
    @TmsLink("184")
    @Epic("Companies and business units")
    @Feature("Add Company")
    @Description("Verify that clicking the Close button successfully closes the 'Add Company' dialog.")
    public void testVerifyCloseAddCompanyDialogWhenCloseButtonIsClicked() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton()
                .clickCloseButton();

        Allure.step("Verify: the 'Add Company' dialog is no longer visible");
        assertThat(companiesAndBusinessUnitsPage.getAddCompanyDialog()).isHidden();
    }

    @Test(dataProvider = "getCompanyNameInvalidSpecialCharacters", dataProviderClass = TestDataProvider.class)
    @TmsLink("215")
    @Epic("Companies and business units")
    @Feature("Add Company")
    @Description("Error is displayed when trying to create a company with special characters in the name.")
    public void testErrorIsDisplayedWhenCreatingCompanyWithSpecialCharacters(String character) {
        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton()
                .fillCompanyNameField("Company" + character)
                .fillCompanyTypeField(COMPANY_TYPE)
                .clickCreateButtonAndTriggerError();

        Allure.step("Verify: error message is displayed about invalid characters in the company name");
        assertThat(addCompanyDialog.getAlertMessage()).containsText(
                ("Invalid companyName: 'Company%s'. "
                        + "It may only contain letters, digits, ampersands, hyphens, commas, periods, and spaces")
                        .formatted(character));
    }

    @Test(dataProvider = "getInvalidCompanyNamesByLengthAndChar", dataProviderClass = TestDataProvider.class)
    @TmsLink("261")
    @Epic("Companies and business units")
    @Feature("Add Company")
    @Description("Error message when trying to create a company with invalid length and special characters.")
    public void testErrorForInvalidCompanyNameLengthAndCharacters(String name, String character) {
        String fullName = name + character;

        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton()
                .fillCompanyNameField(fullName)
                .fillCompanyTypeField(COMPANY_TYPE)
                .clickCreateButtonAndTriggerError();
        System.out.println(addCompanyDialog.getAlertMessage().allInnerTexts());

        Allure.step("Verify: error message for invalid length and character in company name");
        assertThat(addCompanyDialog.getAlertMessage()).containsText(
                ("Invalid companyName: '%s'. It must contain between 4 and 100 characters "
                        + "Invalid companyName: '%s'. It may only contain letters, digits, ampersands, "
                        + "hyphens, commas, periods, and spaces").formatted(fullName, fullName)
        );
    }

    @Test
    @TmsLink("223")
    @Epic("Companies and business units")
    @Feature("Add Company")
    @Description("Company can be added by filling out required fields")
    public void testAddCompanyByFillRequiredFields() {
        deleteCompany(COMPANY_NAME);

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton()
                .fillCompanyNameField(COMPANY_NAME)
                .fillCompanyTypeField(COMPANY_TYPE)
                .clickCreateButton();

        Allure.step("Verify: company creation success message is displayed");
        assertThat(companiesAndBusinessUnitsPage.getAlertMessage()).containsText(
                "Company was created successfully");
    }

    @Test(dependsOnMethods = "testAddCompanyByFillRequiredFields")
    @TmsLink("224")
    @Epic("Companies and business units")
    @Feature("Add Company")
    @Description("Added company appears in the 'Select company' dropdown list")
    public void testVerifyCompanyPresenceInDropdown() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickSelectCompanyDropdown()
                .clickCompanyInDropdown(COMPANY_NAME);

        Allure.step("Verify: company is present in the 'Select company' field");
        assertThat(companiesAndBusinessUnitsPage.getSelectCompanyInput()).hasValue(COMPANY_NAME);
    }

    @Test(dependsOnMethods = "testVerifyCompanyPresenceInDropdown")
    @TmsLink("232")
    @Epic("Companies and business units")
    @Feature("Add Company")
    @Description("Error is displayed when trying to create a company with an already existing name")
    public void testAddCompanyWithSameName() {
        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton()
                .fillCompanyNameField(COMPANY_NAME)
                .fillCompanyTypeField(COMPANY_TYPE)
                .clickCreateButtonAndTriggerError();

        Allure.step("Verify: error message is displayed for duplicate company name");
        assertThat(addCompanyDialog.getAlertMessage()).containsText(
                "Company with name {%s} already exists.".formatted(COMPANY_NAME));
    }

    @Test(expectedExceptions = AssertionFailedError.class)
    @TmsLink("227")
    @Epic("Companies and business units")
    @Feature("Add Company")
    @Description("Company creation with Cyrillic symbols")
    public void testAddCompanyWithCyrillicSymbols() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
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
                .fillCompanyFaxField("84952235611")
                .clickCreateButton();

        Allure.step("Verify: success message is displayed");
        assertThat(companiesAndBusinessUnitsPage.getAlertMessage()).hasText(
                "SUCCESSCompany was created successfully");
    }

    @Test
    @TmsLink("228")
    @Epic("Companies and business units")
    @Feature("Add Company")
    @Description("Company creation with Latin symbols")
    public void testAddCompanyWithAllFilledFields() {
        final String companyName = "Google";
        deleteCompany(companyName);

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
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
        assertThat(companiesAndBusinessUnitsPage.getAlertMessage()).hasText(
                "SUCCESSCompany was created successfully");
    }

    @Test
    @TmsLink("246")
    @Epic("Companies and business units")
    @Feature("Add Company")
    @Description("Validates successful company creation and correct field persistence (e2e).")
    public void testAddCompanyEndToEndTest() throws IOException {
        Company company = CompanyUtils.readCompanyInformationFromJson("jsonfiles/company.json");

        deleteCompany(company.getCompanyName());

        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton();

        Allure.step("Verify: 'Add company' dialog is displayed");
        assertThat(addCompanyDialog.getAddCompanyDialogHeader()).hasText("Add company");

        Allure.step("Verify: 'Create' button is disabled before filling required fields");
        assertThat(addCompanyDialog.getCreateButton()).isDisabled();

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = addCompanyDialog
                .fillCompanyNameField(company.getCompanyName())
                .fillCompanyTypeField(company.getCompanyType())
                .fillCompanyDescriptionField(company.getDescription())
                .fillCompanyWebsiteField(company.getWebsite())
                .fillCompanyPrimaryContactField(company.getPrimaryContact())
                .fillCompanyEmailField(company.getCompanyEmail())
                .fillCompanyCountryField(company.getCountry())
                .fillCompanyStateField(company.getState())
                .fillCompanyZipField(company.getZip())
                .fillCompanyCityField(company.getCity())
                .fillCompanyPhoneField(company.getPhone())
                .fillCompanyMobileField(company.getMobile())
                .fillCompanyFaxField(company.getFax())
                .setApiActiveCheckbox(company.isApiActive())
                .setPortalActiveCheckbox(company.isPortalActive())
                .clickCreateButton();

        Allure.step("Verify: success message is displayed after company creation");
        assertThat(companiesAndBusinessUnitsPage.getAlertMessage())
                .hasText("SUCCESSCompany was created successfully");

        companiesAndBusinessUnitsPage
                .clickSelectCompanyDropdown()
                .clickCompanyInDropdown(company.getCompanyName());

        Allure.step("Verify: selected company is shown in the input field");
        assertThat(companiesAndBusinessUnitsPage.getSelectCompanyInput())
                .hasValue(company.getCompanyName());

        Allure.step("Verify: description field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getDescriptionFromCompanyInfoSection())
                .hasValue(company.getDescription());

        Allure.step("Verify: website field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getWebsiteFromCompanyInfoSection())
                .hasValue(company.getWebsite());

        Allure.step("Verify: primary contact field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getPrimaryContactFromCompanyInfoSection())
                .hasValue(company.getPrimaryContact());

        Allure.step("Verify: email field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getEmailFromCompanyInfoSection())
                .hasValue(company.getCompanyEmail());

        Allure.step("Verify: phone field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getPhoneFromCompanyInfoSection())
                .hasValue(company.getPhone());

        Allure.step("Verify: mobile field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getMobileFromCompanyInfoSection())
                .hasValue(company.getMobile());

        Allure.step("Verify: fax field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getFaxFromCompanyInfoSection())
                .hasValue(company.getFax());

        Allure.step("Verify: country field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getCountryFromCompanyInfoSection())
                .hasValue(company.getCountry());

        Allure.step("Verify: state field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getStateFromCompanyInfoSection())
                .hasValue(company.getState());

        Allure.step("Verify: ZIP code field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getZipFromCompanyInfoSection())
                .hasValue(company.getZip());

        Allure.step("Verify: city field is correctly filled");
        assertThat(companiesAndBusinessUnitsPage.getCityFromCompanyInfoSection())
                .hasValue(company.getCity());

        Allure.step("Verify: 'API active' checkbox is checked");
        assertThat(companiesAndBusinessUnitsPage.getApiActiveCheckboxFromCompanyInfoSection())
                .isChecked();

        Allure.step("Verify: 'Portal active' checkbox is checked");
        assertThat(companiesAndBusinessUnitsPage.getPortalActiveCheckboxFromCompanyInfoSection())
                .isChecked();
    }
}
