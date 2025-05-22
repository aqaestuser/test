package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.dialog.merchant.EditBusinessUnitDialog;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EditBusinessUnitDialogTest extends BaseTest {

    @Test
    @TmsLink("387")
    @TmsLink("501")
    @TmsLink("515")
    @TmsLink("528")
    @Epic("System/Companies and business units")
    @Feature("Edit business unit")
    @Description("Verify that all elements of dialog are displayed properly")
    public void testElementsOfEditBusinessUnitDialog() {
        String companyName = "CompanyForBuEdit";
        String buName = "NewBUForEdit";

        TestUtils.createCompany(getApiRequestContext(), companyName);
        TestUtils.deleteAllByMerchantTitle(getApiRequestContext(), companyName, buName);
        TestUtils.createMerchantTitleIfNeeded(getApiRequestContext(), companyName, buName);

        EditBusinessUnitDialog editBusinessUnitDialog = new DashboardPage(getPage())
                .refreshDashboard()
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .getSelectCompany().selectCompany(companyName)
                .getTable().clickEditBusinessUnitButton(buName);

        Allure.step("Verify: the header contains the expected title text");
        assertThat(editBusinessUnitDialog.getDialogHeader()).hasText("Edit business unit");

        Allure.step("Verify: Company name is pre-filled correctly");
        assertThat(editBusinessUnitDialog.getCompanyNameField()).hasValue(companyName);

        Allure.step("Verify: Company name field is read-only");
        assertThat(editBusinessUnitDialog.getCompanyNameField()).hasAttribute("aria-readonly", "true");

        Allure.step("Verify: all labels are correct for each field");
        assertThat(editBusinessUnitDialog.getFieldLabel()).hasText(new String[]{"Company name", "Business unit name"});

        CompaniesAndBusinessUnitsPage companiesAndBusinessUnitsPage = editBusinessUnitDialog.clickCloseButton();

        Allure.step("Verify: Dialog 'Edit business unit' is not displayed after clicking on the 'Close' button");
        assertThat(companiesAndBusinessUnitsPage.getEditBusinessUnitDialog()).isHidden();

        companiesAndBusinessUnitsPage
                .getTable().clickEditBusinessUnitButton(buName)
                .clickCloseIcon();

        Allure.step("Verify: Dialog 'Edit business unit' is not displayed after clicking on the 'Close' icon");
        assertThat(companiesAndBusinessUnitsPage.getEditBusinessUnitDialog()).isHidden();

        TestUtils.deleteAllByMerchantTitle(getApiRequestContext(), companyName, buName);
        TestUtils.deleteCompany(getApiRequestContext(), companyName);
    }
}
