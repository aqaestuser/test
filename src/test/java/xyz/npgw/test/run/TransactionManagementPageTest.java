package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.dialog.adjustment.AddAdjustmentDialog;
import xyz.npgw.test.page.system.TransactionManagementPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static xyz.npgw.test.common.Constants.BUSINESS_UNIT_FOR_TEST_RUN;
import static xyz.npgw.test.common.Constants.COMPANY_NAME_FOR_TEST_RUN;

public class TransactionManagementPageTest extends BaseTest {

    @Ignore("Invalid key=value pair (missing equal-sign) in Authorization header ERROR with Create button")
    @Test
    @TmsLink("874")
    @Epic("System/Transaction Management")
    @Feature("Add adjustment")
    @Description("Verify possibility to add adjustment with Transaction adjustment")
    public void testAddTransactionAdjustment() {
        TransactionManagementPage page = new TransactionManagementPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickTransactionManagementTab()
                .clickAddAdjustmentButton()
                .getTable().clickTransaction()
                .clickCreateButton();

        assertThat(page.getTransactionsTable()).containsText("id.transaction.");
    }

    @Ignore("There is no longer a default transaction")
    @Test
    @TmsLink("873")
    @Epic("System/Transaction Management")
    @Feature("Add adjustment")
    @Description("Close button with no changes performed")
    public void testClickCloseButtonWithNoChanges() {
        TransactionManagementPage page = new TransactionManagementPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickTransactionManagementTab()
                .clickAddAdjustmentButton()
                .getTable().clickTransaction()
                .clickCloseButton();

        assertThat(page.getTransactionsTable()).containsText("No rows to display.");
    }

    @Test
    @TmsLink("884")
    @Epic("System/Transaction Management")
    @Feature("Add adjustment")
    @Description("Create button is disabled if nothing is selected")
    public void testCreateButtonIsDisabledByDefault() {
        AddAdjustmentDialog page = new TransactionManagementPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickTransactionManagementTab()
                .clickAddAdjustmentButton();

        assertThat(page.getCreateButton()).hasAttribute("data-disabled", "true");
    }

    @Ignore("getFirstRowCell(NPGW reference) is not there as for now)")
    @Test
    @TmsLink("886")
    @Epic("System/Transaction Management")
    @Feature("Add adjustment")
    @Description("Search by NPGW reference within Add adjustment dialog and check placeholders")
    public void testPlaceholdersAndSearchNpgwInAddAdjustment() {
        String referenceFromTable = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(TestUtils.lastBuildDate(getApiRequestContext()))
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .getTable().getFirstRowReference();

        AddAdjustmentDialog addAdjustmentDialog = new TransactionsPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickTransactionManagementTab()
                .clickAddAdjustmentButton();

        Allure.step("Verify: check placeholder in the 'NPGW reference' search field.");
        assertThat(addAdjustmentDialog.getNpgwReferenceFieldLabel()).hasText("NPGW reference");

        Allure.step("Verify: check placeholder in the 'Business unit reference' search field.");
        assertThat(addAdjustmentDialog.getBuReferenceFieldLabel()).hasText("Business unit reference");

        addAdjustmentDialog.fillNpgwReferenceInput(referenceFromTable);

        Allure.step("Verify: The located reference matches the one entered in the search field.");
        assertThat(addAdjustmentDialog.getTable().getFirstRowCell("NPGW reference")).hasText(referenceFromTable);
    }
}
