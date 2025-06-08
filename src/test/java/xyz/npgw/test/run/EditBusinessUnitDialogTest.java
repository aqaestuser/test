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
import xyz.npgw.test.page.dialog.merchant.EditBusinessUnitDialog;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EditBusinessUnitDialogTest extends BaseTest {

    private static final String COMPANY_NAME = "%s company for bu edit".formatted(RUN_ID);
    private static final String MERCHANT_TITLE = "%s new bu for edit".formatted(RUN_ID);

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME);
        TestUtils.createBusinessUnit(getApiRequestContext(), COMPANY_NAME, MERCHANT_TITLE);
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
        EditBusinessUnitDialog editBusinessUnitDialog = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getTable().clickEditBusinessUnitButton(MERCHANT_TITLE);

        Allure.step("Verify: the header contains the expected title text");
        assertThat(editBusinessUnitDialog.getDialogHeader()).hasText("Edit business unit");

        Allure.step("Verify: Company name is pre-filled correctly");
        assertThat(editBusinessUnitDialog.getCompanyNameField()).hasValue(COMPANY_NAME);

        Allure.step("Verify: Company name field is read-only");
        assertThat(editBusinessUnitDialog.getCompanyNameField()).hasAttribute("aria-readonly", "true");

        Allure.step("Verify: all labels are correct for each field");
        assertThat(editBusinessUnitDialog.getFieldLabel()).hasText(new String[]{"Company name", "Business unit name"});

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = editBusinessUnitDialog
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
    @TmsLink("722")
    @Epic("System/Companies and business units")
    @Feature("Delete business unit")
    @Description("Verify that business unit can be deleted")
    public void testDeleteBusinessUnit() {
        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getTable().clickDeleteBusinessUnitButton(MERCHANT_TITLE)
                .clickDeleteButton();

        Allure.step("Verify: the header contains the expected title text");
        assertThat(companiesAndBusinessUnitsPage.getAlert().getMessage())
                .hasText("SUCCESSBusiness unit was deleted successfully");
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        super.afterClass();
    }
}
