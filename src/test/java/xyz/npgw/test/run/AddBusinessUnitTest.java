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
    Company company241 = new Company("%s company 241".formatted(RUN_ID));

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME);
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

        Allure.step("Verify: 'Company name' field is non-editable");
        assertThat(addBusinessUnitDialog.getCompanyNameField()).hasAttribute("aria-readonly", "true");

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
        TestUtils.deleteCompany(getApiRequestContext(), company241.companyName());
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        super.afterClass();
    }
}
