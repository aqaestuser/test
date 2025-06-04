package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
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

    private static final String COMPANY_NAME = "%s CompanyName".formatted(RUN_ID);
    BusinessUnit businessUnit = new BusinessUnit("MerchantNameTest");
    Company company213 = new Company("Company 213%s".formatted(RUN_ID));
    Company company238 = new Company("Company 238%s".formatted(RUN_ID));
    Company company241 = new Company("Company 241%s".formatted(RUN_ID));
    Company company218 = new Company("Company 218%s".formatted(RUN_ID));
    Company company480 = new Company("Company 480%s".formatted(RUN_ID));

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME);
    }

    @Test
    @TmsLink("213")
    @Epic("System/Companies and business units")
    @Feature("Add business unit")
    @Description("Verify 'Add business unit' button activation once some company is selected")
    public void testVerifyAvailabilityOfBusinessUnitButton() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(company213.companyName())
                .fillCompanyTypeField(company213.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getSelectCompany().selectCompany(company213.companyName());

        Allure.step("Verify: 'Add business unit' button is available");
        assertThat(companiesAndBusinessUnitsPage.getAddBusinessUnitButton()).isEnabled();

        Allure.step("Verify: 'Edit selected company' button is available");
        assertThat(companiesAndBusinessUnitsPage.getEditCompanyButton()).isEnabled();
    }

    @Test
    @TmsLink("214")
    @Epic("System/Companies and business units")
    @Feature("Add business unit")
    @Description("Verify 'Add business unit' button is disabled if 'Select company' filter's field is cleaned")
    public void testVerifyAddBusinessUnitButtonDefaultState() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
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
        AddBusinessUnitDialog addBusinessUnitDialog = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(company238.companyName())
                .fillCompanyTypeField(company238.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getSelectCompany().selectCompany(company238.companyName())
                .clickOnAddBusinessUnitButton();

        Allure.step("Verify: Company name field is read-only and prefilled created company");
        assertThat(addBusinessUnitDialog.getCompanyNameField()).hasValue(company238.companyName());

        Allure.step("Verify: 'Company name' field is non-editable");
        assertThat(addBusinessUnitDialog.getCompanyNameField()).hasAttribute("aria-readonly", "true");
    }

    @Test
    @TmsLink("241")
    @Epic("System/Companies and business units")
    @Feature("Add business unit")
    @Description("Verify that a new business unit wasn't added once click 'Close' button")
    public void testCloseButtonAndDiscardChanges() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(company241.companyName())
                .fillCompanyTypeField(company241.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getSelectCompany().selectCompany(company241.companyName())
                .clickOnAddBusinessUnitButton()
                .clickCloseButton();

        Allure.step("Verify: The table is empty and 'No rows to display.' is displayed");
        assertThat(companiesAndBusinessUnitsPage.getMerchantsTable()).containsText("No rows to display.");
    }

    @Test
    @TmsLink("218")
    @Epic("Companies and business units")
    @Feature("Add business unit")
    @Description("Add a new business unit with 'Add business unit' button")
    public void testAddNewMerchants() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(company218.companyName())
                .fillCompanyTypeField(company218.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getSelectCompany().selectCompany(company218.companyName())
                .clickOnAddBusinessUnitButton()
                .fillBusinessUnitNameField(company218.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone();

        Allure.step("Verify: New business unit name appears in the list");
        assertThat(companiesAndBusinessUnitsPage.getTable().getFirstRowCell("Business unit name"))
                .hasText(company218.companyType());

        Allure.step("Verify: Merchant ID is displayed");
        assertThat(companiesAndBusinessUnitsPage.getTable().getFirstRowCell("Business unit ID"))
                .containsText("id.merchant");
    }

    @Test
    @TmsLink("480")
    @Epic("Companies and business units")
    @Feature("Reset filter")
    @Description("Verify default filter state was applied once reset")
    public void testResetAppliedFilter() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(company480.companyName())
                .fillCompanyTypeField(company480.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getSelectCompany().selectCompany(company480.companyName())
                .clickOnResetFilterButton();

        Allure.step("Verify: Ensure the prompt appears when no company is selected");
        assertThat(companiesAndBusinessUnitsPage.getPageContent())
                .containsText("Select company name to view merchants");
    }

    @Test
    @TmsLink("290")
    @Epic("System/Companies and business units")
    @Feature("Add business unit")
    @Description("Validates successful business unit addition to company (E2E test).")
    public void testAddBusinessUnitEndToEndTest() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab();

        Allure.step("Verify: 'Add business unit' button is disabled before selecting a company");
        assertThat(companiesAndBusinessUnitsPage.getAddBusinessUnitButton())
                .isDisabled();

        AddBusinessUnitDialog addBusinessUnitDialog = companiesAndBusinessUnitsPage
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .clickOnAddBusinessUnitButton();

        Allure.step("Verify: 'Add business unit' dialog is opened");
        assertThat(addBusinessUnitDialog.getGetAddMerchantDialogHeader())
                .hasText("Add business unit");

        Allure.step("Verify: Company name is pre-filled correctly");
        assertThat(addBusinessUnitDialog.getCompanyNameField())
                .hasValue(COMPANY_NAME);

        companiesAndBusinessUnitsPage = addBusinessUnitDialog
                .fillBusinessUnitNameField(businessUnit.merchantTitle())
                .clickCreateButton();

        Allure.step("Verify: Success alert is shown after business unit is added");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("SUCCESSBusiness unit was created successfully");

        Allure.step("Verify: Selected company is preserved after creation");
        assertThat(companiesAndBusinessUnitsPage.getSelectCompany().getSelectCompanyField())
                .hasValue(COMPANY_NAME);

        Allure.step("Verify: New business unit name appears in the list");
        assertThat(companiesAndBusinessUnitsPage.getTable().getFirstRowCell("Business unit name"))
                .hasText(businessUnit.merchantTitle());

        Allure.step("Verify: Merchant ID is displayed");
        assertThat(companiesAndBusinessUnitsPage.getTable().getFirstRowCell("Business unit ID"))
                .containsText("id.merchant");
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteCompany(getApiRequestContext(), company213.companyName());
        TestUtils.deleteCompany(getApiRequestContext(), company238.companyName());
        TestUtils.deleteCompany(getApiRequestContext(), company241.companyName());
        TestUtils.deleteAllByMerchantTitle(getApiRequestContext(), company218.companyName(), company218.companyType());
        TestUtils.deleteCompany(getApiRequestContext(), company218.companyName());
        TestUtils.deleteCompany(getApiRequestContext(), company480.companyName());
        TestUtils.deleteAllByMerchantTitle(getApiRequestContext(), COMPANY_NAME, businessUnit.merchantTitle());
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        super.afterClass();
    }
}
