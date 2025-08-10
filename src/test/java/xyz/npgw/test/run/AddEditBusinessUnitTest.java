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
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.entity.Company;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.dashboard.SuperDashboardPage;
import xyz.npgw.test.page.dialog.merchant.AddBusinessUnitDialog;
import xyz.npgw.test.page.dialog.merchant.EditBusinessUnitDialog;
import xyz.npgw.test.page.system.SuperCompaniesAndBusinessUnitsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AddEditBusinessUnitTest extends BaseTest {

    private static final String COMPANY_NAME = "%s CompanyName".formatted(RUN_ID);
    BusinessUnit businessUnit = new BusinessUnit("MerchantNameTest");
    Company company241 = new Company("%s company 241".formatted(RUN_ID));
    private static final String COMPANY_FOR_EDIT = "%s company for bu edit".formatted(RUN_ID);
    private static final String MERCHANT_TITLE = "%s new bu for edit".formatted(RUN_ID);
    private static final String MERCHANT_TITLE_EDITED = "%s edited bu".formatted(RUN_ID);
    private BusinessUnit businessUnitForEdit;

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME);
        TestUtils.createCompany(getApiRequestContext(), COMPANY_FOR_EDIT);
        businessUnitForEdit = TestUtils.createBusinessUnit(getApiRequestContext(), COMPANY_FOR_EDIT, MERCHANT_TITLE);
    }

    @Test
    @TmsLink("241")
    @Epic("System/Companies and business units")
    @Feature("Add business unit")
    @Description("Verify that a new business unit wasn't added once click 'Close' button")
    public void testCloseButtonAndDiscardChanges() {
        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
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

        TestUtils.deleteCompany(getApiRequestContext(), company241.companyName());
    }

    @Test
    @TmsLink("290")
    @Epic("System/Companies and business units")
    @Feature("Add business unit")
    @Description("Validates successful business unit addition to company (E2E test).")
    public void testAddBusinessUnitEndToEndTest() {
        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
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
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(COMPANY_FOR_EDIT)
                .getTable().clickEditBusinessUnitButton(MERCHANT_TITLE);

        Allure.step("Verify: the header contains the expected title text");
        assertThat(editBusinessUnitDialog.getDialogHeader()).hasText("Edit business unit");

        Allure.step("Verify: Company name is pre-filled correctly");
        assertThat(editBusinessUnitDialog.getCompanyNameField()).hasValue(COMPANY_FOR_EDIT);

        Allure.step("Verify: Company name field is read-only");
        assertThat(editBusinessUnitDialog.getCompanyNameField()).hasAttribute("aria-readonly", "true");

        Allure.step("Verify: all labels are correct for each field");
        assertThat(editBusinessUnitDialog.getFieldLabel()).hasText(new String[]{"Company name", "Business unit name"});

        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = editBusinessUnitDialog
                .clickCloseButton();

        Allure.step("Verify: Dialog 'Edit business unit' is not displayed after clicking on the 'Close' button");
        assertThat(companiesAndBusinessUnitsPage.getEditBusinessUnitDialog()).isHidden();

        companiesAndBusinessUnitsPage
                .getTable().clickEditBusinessUnitButton(MERCHANT_TITLE)
                .clickCloseIcon();

        Allure.step("Verify: Dialog 'Edit business unit' is not displayed after clicking on the 'Close' icon");
        assertThat(companiesAndBusinessUnitsPage.getEditBusinessUnitDialog()).isHidden();
    }

    @Test(priority = 1)
    @TmsLink("794")
    @Epic("System/Companies and business units")
    @Feature("Edit Business unit")
    @Description("Editing a business unit updates its name while preserving the same ID")
    public void testEditBusinessUnit() {
        String originalBusinessUnitId = businessUnitForEdit.merchantId();

        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(COMPANY_FOR_EDIT)
                .getTable().clickEditBusinessUnitButton(MERCHANT_TITLE)
                .fillBusinessUnitNameField(MERCHANT_TITLE_EDITED)
                .clickSaveChangesButton();

        Allure.step("Verify: the success alert is displayed with correct message");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("SUCCESSBusiness unit was updated successfully");

        Locator editedBusinessUnitIdLocator = companiesAndBusinessUnitsPage.getTable()
                .getCell(MERCHANT_TITLE_EDITED, "Business unit ID");

        Allure.step("Verify: the Business Unit ID remains the same after editing name");
        assertThat(editedBusinessUnitIdLocator).hasText(originalBusinessUnitId);
    }

    @Test(priority = 2)
    @TmsLink("722")
    @Epic("System/Companies and business units")
    @Feature("Delete business unit")
    @Description("Verify that business unit can be deleted")
    public void testDeleteBusinessUnit() {
        SuperCompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(COMPANY_FOR_EDIT)
                .getTable().clickDeleteBusinessUnitButton(MERCHANT_TITLE_EDITED)
                .clickDeleteButton();

        Allure.step("Verify: the header contains the expected title text");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("SUCCESSBusiness unit was deleted successfully");
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteCompany(getApiRequestContext(), company241.companyName());
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_FOR_EDIT);
        super.afterClass();
    }
}
