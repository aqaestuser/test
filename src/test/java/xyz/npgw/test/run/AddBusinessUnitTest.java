package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import net.datafaker.Faker;
import org.opentest4j.AssertionFailedError;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.entity.Company;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.dialog.merchant.AddBusinessUnitDialog;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AddBusinessUnitTest extends BaseTest {

    private static final String COMPANY_NAME = "CompanyName";

    @Test
    @TmsLink("213")
    @Epic("System/Companies and business units")
    @Feature("Add business unit")
    @Description("Verify 'Add business unit' button activation once some company is selected")
    public void testVerifyAvailabilityOfBusinessUnitButton() {
        Company company = new Company(new Faker());

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(company.companyName())
                .fillCompanyTypeField(company.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getSelectCompany().selectCompany(company.companyName());

        Allure.step("Verify: 'Add business unit' button is available");
        assertThat(companiesAndBusinessUnitsPage.getAddBusinessUnitButton()).isEnabled();
        Allure.step("Verify: 'Edit selected company' button is available");
        assertThat(companiesAndBusinessUnitsPage.getEditCompanyButton()).isEnabled();

        TestUtils.deleteCompany(getApiRequestContext(), company.companyName());
    }

    @Test
    @TmsLink("214")
    @Epic("System/Companies and business units")
    @Feature("Add business unit")
    @Description("Verify 'Add business unit' button is disabled if 'Select company' filter's field is cleaned")
    public void testVerifyAddBusinessUnitButtonDefaultState() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab();

        Allure.step("Verify: 'Add business unit' button is disabled once no destination company is selected");
        assertThat(companiesAndBusinessUnitsPage.getAddBusinessUnitButton()).isDisabled();
    }

    @Test
    @TmsLink("238")
    @Epic("System/Companies and business units")
    @Feature("Add business unit")
    @Description("Verify that 'Company name' field is prefilled and impossible to change")
    public void testCompanyNameFieldDefaultState() {
        Company company = new Company(new Faker());

        AddBusinessUnitDialog addBusinessUnitDialog = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(company.companyName())
                .fillCompanyTypeField(company.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getSelectCompany().selectCompany(company.companyName())
                .clickOnAddBusinessUnitButton();

        Allure.step("Verify: Company name field is read-only and prefilled created company");
        assertThat(addBusinessUnitDialog.getCompanyNameField()).hasValue(company.companyName());

        Allure.step("Verify: 'Company name' field is non-editable");
        assertThat(addBusinessUnitDialog.getCompanyNameField()).hasAttribute("aria-readonly", "true");

        TestUtils.deleteCompany(getApiRequestContext(), company.companyName());
    }

    @Test
    @TmsLink("241")
    @Epic("System/Companies and business units")
    @Feature("Add business unit")
    @Description("Verify that a new business unit wasn't added once click 'Close' button")
    public void testCloseButtonAndDiscardChanges() {
        Company company = new Company(new Faker());

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(company.companyName())
                .fillCompanyTypeField(company.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getSelectCompany().selectCompany(company.companyName())
                .clickOnAddBusinessUnitButton()
                .clickCloseButton();

        Allure.step("Verify: The table is empty and 'No rows to display.' is displayed");
        assertThat(companiesAndBusinessUnitsPage.getMerchantsTable()).containsText("No rows to display.");

        TestUtils.deleteCompany(getApiRequestContext(), company.companyName());
    }

    @Ignore
    @Test
    @TmsLink("218")
    @Epic("Companies and business units")
    @Feature("Add business unit")
    @Description("Add a new business unit with 'Add business unit' button")
    public void testAddNewMerchants() {
        Company company = new Company(new Faker());

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(company.companyName())
                .fillCompanyTypeField(company.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getSelectCompany().selectCompany(company.companyName())
                .clickOnAddBusinessUnitButton()
                .fillBusinessUnitNameField(company.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone();

        Allure.step("Verify: New business unit name appears in the list");
        assertThat(companiesAndBusinessUnitsPage.getBusinessUnitNameData()).hasText(company.companyType());

        Allure.step("Verify: Merchant ID is displayed");
        assertThat(companiesAndBusinessUnitsPage.getMerchantIdData()).containsText("id.merchant");

        TestUtils.deleteMerchantByName(getApiRequestContext(), company.companyName(), company.companyType());
        TestUtils.deleteCompany(getApiRequestContext(), company.companyName());
    }

    @Test
    @TmsLink("480")
    @Epic("Companies and business units")
    @Feature("Reset filter")
    @Description("Verify default filter state was applied once reset")
    public void testResetAppliedFilter() {
        Company company = new Company(new Faker());

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(company.companyName())
                .fillCompanyTypeField(company.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getSelectCompany().selectCompany(company.companyName())
                .clickOnResetFilterButton();

        Allure.step("Verify: Ensure the prompt appears when no company is selected");
        assertThat(companiesAndBusinessUnitsPage.getPageContent())
                .containsText("Select company name to view merchants");

        TestUtils.deleteCompany(getApiRequestContext(), company.companyName());
    }

    @Test(expectedExceptions = AssertionFailedError.class)
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
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .clickOnAddBusinessUnitButton();

        Allure.step("Verify: 'Add business unit' dialog is opened");
        assertThat(addBusinessUnitDialog.getGetAddMerchantDialogHeader()).hasText("Add business unit");

        Allure.step("Verify: Company name is pre-filled correctly");
        assertThat(addBusinessUnitDialog.getCompanyNameField()).hasValue(COMPANY_NAME);

        addBusinessUnitDialog.clickCreateButtonAndTriggerError();

        Allure.step("Verify: Validation error is shown when merchant name is not filled");
        assertThat(addBusinessUnitDialog
                .getAlert().getAlertMessage())
                .containsText("Enter merchant name");

        companiesAndBusinessUnitsPage = addBusinessUnitDialog
                .fillBusinessUnitNameField(businessUnit.merchantTitle())
                .clickCreateButton();

        Allure.step("Verify: Success alert is shown after business unit is added");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getAlertMessage()).hasText(
                "SUCCESSBusiness unit was created successfully");

        Allure.step("Verify: Selected company is preserved after creation");
        assertThat(companiesAndBusinessUnitsPage
                .getSelectCompany().getSelectCompanyField()).hasValue(COMPANY_NAME);

        Allure.step("Verify: New business unit name appears in the list");
        assertThat(companiesAndBusinessUnitsPage.getBusinessUnitNameData()).hasText(businessUnit.merchantTitle());

        Allure.step("Verify: Merchant ID is displayed");
        assertThat(companiesAndBusinessUnitsPage.getMerchantIdData()).containsText("id.merchant");

        TestUtils.deleteMerchantByName(getApiRequestContext(), COMPANY_NAME, businessUnit.merchantTitle());
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
    }
}
