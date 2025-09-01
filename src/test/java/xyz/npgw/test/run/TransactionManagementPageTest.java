package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTestForSingleLogin;
import xyz.npgw.test.page.dashboard.SuperDashboardPage;
import xyz.npgw.test.page.dialog.adjustment.AddAdjustmentDialog;
import xyz.npgw.test.page.system.SuperTransactionManagementPage;
import xyz.npgw.test.page.transactions.SuperTransactionsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static xyz.npgw.test.common.Constants.BUSINESS_UNIT_FOR_TEST_RUN;
import static xyz.npgw.test.common.Constants.COMPANY_NAME_FOR_TEST_RUN;
import static xyz.npgw.test.common.Constants.ONE_DATE_FOR_TABLE;

public class TransactionManagementPageTest extends BaseTestForSingleLogin {

    @Ignore("Invalid key=value pair (missing equal-sign) in Authorization header ERROR with Create button")
    @Test
    @TmsLink("874")
    @Epic("System/Transaction management")
    @Feature("Add adjustment")
    @Description("Verify possibility to add adjustment with Transaction adjustment")
    public void testAddTransactionAdjustment() {
        SuperTransactionManagementPage page = new SuperTransactionManagementPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickTransactionManagementTab()
                .clickAddAdjustmentButton()
                .getTable().clickTransaction()
                .clickCreateButton();

        assertThat(page.getTransactionsTable()).containsText("id.transaction.");
    }

    @Ignore("There is no longer a default transaction")
    @Test
    @TmsLink("873")
    @Epic("System/Transaction management")
    @Feature("Add adjustment")
    @Description("Close button with no changes performed")
    public void testClickCloseButtonWithNoChanges() {
        SuperTransactionManagementPage page = new SuperTransactionManagementPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickTransactionManagementTab()
                .clickAddAdjustmentButton()
                .getTable().clickTransaction()
                .clickCloseButton();

        assertThat(page.getTransactionsTable()).containsText("No rows to display.");
    }

    @Test
    @TmsLink("884")
    @Epic("System/Transaction management")
    @Feature("Add adjustment")
    @Description("Create button is disabled if nothing is selected")
    public void testCreateButtonIsDisabledByDefault() {
        AddAdjustmentDialog page = new SuperTransactionManagementPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickTransactionManagementTab()
                .clickAddAdjustmentButton();

        assertThat(page.getCreateButton()).isDisabled();
    }

    @Ignore("getFirstRowCell(NPGW reference) is not there as for now)")
    @Test
    @TmsLink("886")
    @Epic("System/Transaction management")
    @Feature("Add adjustment")
    @Description("Search by NPGW reference within Add adjustment dialog and check placeholders")
    public void testPlaceholdersAndSearchNpgwInAddAdjustment() {
        String referenceFromTable = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(ONE_DATE_FOR_TABLE)
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .getTable().getFirstRowReference();

        AddAdjustmentDialog addAdjustmentDialog = new SuperTransactionsPage(getPage())
                .getHeader().clickSystemAdministrationLink()
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
