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
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CompaniesAndBusinessUnitsTest extends BaseTest {

    private static final String COMPANY_NAME = "%s company to delete".formatted(RUN_ID);
    private static final String COMPANY_DELETION_BLOCKED_NAME = "%s deletion-blocked company".formatted(RUN_ID);

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME);
        TestUtils.createCompany(getApiRequestContext(), COMPANY_DELETION_BLOCKED_NAME);
        TestUtils.createBusinessUnit(getApiRequestContext(), COMPANY_DELETION_BLOCKED_NAME, "Business unit 1");
    }

    @Test
    @TmsLink("691")
    @Epic("System/Companies and business units")
    @Feature("Settings")
    @Description("The company info block can be hidden and shown via settings.")
    public void testToggleCompanyInfoVisibilityViaSettings() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .clickSettings()
                .checkHideCompanyInfo();

        Allure.step("Verify: company info block is hidden after selecting 'Hide' in settings");
        assertThat(companiesAndBusinessUnitsPage.getCompanyInfoBlock()).isHidden();

        companiesAndBusinessUnitsPage
                .checkShowCompanyInfo();

        Allure.step("Verify: company info block is visible again after selecting 'Show' in settings");
        assertThat(companiesAndBusinessUnitsPage.getCompanyInfoBlock()).isVisible();
    }

    // TODO unstable - company not found sometimes
    @Test(priority = 1)
    @TmsLink("723")
    @Epic("System/Companies and business units")
    @Feature("Delete Company")
    @Description("Verify that company can be deleted")
    public void testDeleteCompany() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .clickDeleteSelectedCompany()
                .clickDeleteButton();

        Allure.step("Verify: the success alert appears after deleting the company");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("SUCCESSCompany was deleted successfully");

//        Allure.step("Verify: the deleted company is no longer present in the dropdown list");
//        assertTrue(companiesAndBusinessUnitsPage.getSelectCompany().isCompanyAbsentInDropdown(COMPANY_NAME));
    }

    @Test
    @TmsLink("728")
    @Epic("System/Companies and business units")
    @Feature("Delete Company")
    @Description("Verify that company cannot be deleted if there are associated business units")
    public void testCannotDeleteCompanyWithAssociatedBusinessUnit() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(COMPANY_DELETION_BLOCKED_NAME)
                .clickDeleteSelectedCompany()
                .clickDeleteButton();

        Allure.step("Verify: error message is shown when trying to delete a company with business unit");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("ERRORCompany could not be deleted: there are still merchants associated with it");

    }

    @Test(priority = 2)
    @TmsLink("743")
    @Epic("System/Companies and business units")
    @Feature("Delete Company")
    @Description("Verify that company cannot be deleted if there are users assigned to it")
    public void testCannotDeleteCompanyWithAssignedUser() {
        String email = "%s.admin123@email.com".formatted(TestUtils.now());

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSelectCompany().selectCompany(COMPANY_DELETION_BLOCKED_NAME)
                .clickAddUserButton()
                .fillEmailField(email)
                .fillPasswordField("Qwerty123!")
                .checkCompanyAdminRadiobutton()
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(COMPANY_DELETION_BLOCKED_NAME)
                .clickDeleteSelectedCompany()
                .clickDeleteButton();

        Allure.step("Verify: error message is shown when trying to delete a company with users");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("ERRORCompany could not be deleted: there are still users associated with it");
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
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .clickOnResetFilterButton();

        Allure.step("Verify: Ensure the prompt appears when no company is selected");
        assertThat(companiesAndBusinessUnitsPage.getPageContent())
                .hasText("Select company name to view merchants");

        Allure.step("Verify: the 'Company' input field is empty after reset");
        assertThat(companiesAndBusinessUnitsPage.getSelectCompany().getSelectCompanyField()).isEmpty();
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_DELETION_BLOCKED_NAME);
        super.afterClass();
    }
}
