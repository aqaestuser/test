package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.opentest4j.AssertionFailedError;
import org.testng.annotations.Test;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.page.AddCompanyDialog;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.systemadministration.CompaniesAndBusinessUnitsPage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

public class AddCompanyDialogTest extends BaseTest {

    private static final String COMPANY_NAME = "CompanyName";

    private void deleteCompany(String name) {
        getRequest().delete(ProjectProperties.getBaseUrl() + "/portal-v1/company/"
                + URLEncoder.encode(name, StandardCharsets.UTF_8));
    }

    @Test
    @TmsLink("160")
    @Epic("Companies and business units")
    @Feature("Title Verification")
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
    @Feature("Placeholders Verification")
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
    @Feature("Company Name Length Validation")
    @Description("Error message is shown for company name is shorter than 4 or longer than 100 characters.")
    public void testVerifyErrorMessageForInvalidCompanyNameLength(String name) {
        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton()
                .fillCompanyNameField(name)
                .fillCompanyTypeField("Company type")
                .clickCreateButtonAndTriggerError();

        Allure.step("Verify: error message for invalid company name: '{name}' is displayed");
        assertThat(addCompanyDialog.getAlertMessage()).containsText(
                "Invalid companyName: '%s'. It must contain between 4 and 100 characters".formatted(name));
    }

    @Test(dataProvider = "getEmptyRequiredFields", dataProviderClass = TestDataProvider.class)
    @TmsLink("206")
    @Epic("Companies and business units")
    @Feature("Validation of Required Fields")
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
    @Feature("Close Button Functionality")
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
    @Feature("Company Name Validation")
    @Description("Error is displayed when trying to create a company with special characters in the name.")
    public void testErrorIsDisplayedWhenCreatingCompanyWithSpecialCharacters(String character) {
        AddCompanyDialog addCompanyDialog = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton()
                .fillCompanyNameField("Company" + character)
                .fillCompanyTypeField("Company type")
                .clickCreateButtonAndTriggerError();

        Allure.step("Verify: error message is displayed about invalid characters in the company name");
        assertThat(addCompanyDialog.getAlertMessage()).containsText(
                ("Invalid companyName: 'Company%s'. "
                        + "It may only contain letters, digits, ampersands, hyphens, commas, periods, and spaces")
                        .formatted(character));
    }

    @Test
    @TmsLink("223")
    @Feature("Company Creation")
    @Description("Company can be added by filling out required fields")
    public void testAddCompanyByFillRequiredFields() {
        deleteCompany(COMPANY_NAME);

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickAddCompanyButton()
                .fillCompanyNameField(COMPANY_NAME)
                .fillCompanyTypeField("Company type")
                .clickCreateButton();

        Allure.step("Verify: company creation success message is displayed");
        assertThat(companiesAndBusinessUnitsPage.getAlertMessage()).containsText(
                "Company was created successfully");
    }

    @Test(dependsOnMethods = "testAddCompanyByFillRequiredFields")
    @TmsLink("224")
    @Feature("Company Verification")
    @Description("Added company appears in the 'Select company' dropdown list")
    public void testVerifyCompanyPresenceInDropdown() {
        boolean isCompanyListedInDropdown = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTabButton()
                .clickSelectCompanyDropdown()
                .isCompanyInDropdown(COMPANY_NAME);

        Allure.step("Verify: company is present in the 'Select company' dropdown list");
        Assert.assertTrue(isCompanyListedInDropdown,
                "Expected company to be present in the dropdown, but it was not found.");
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
}
