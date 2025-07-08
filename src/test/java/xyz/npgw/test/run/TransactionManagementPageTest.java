package xyz.npgw.test.run;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.system.TransactionManagementPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TransactionManagementPageTest extends BaseTest {

    @Ignore("Invalid key=value pair (missing equal-sign) in Authorization header ERROR with Create button")
    @Test
    @TmsLink("874")
    @Epic("System/TransactionManagement")
    @Feature("Add adjustment")
    @Description("Verify possibility to add adjustment with Transaction adjustment")
    public void testAddTransactionAdjustment() {
        TransactionManagementPage page = new TransactionManagementPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickTransactionManagementTab()
                .clickAddAdjustmentButton()
                .clickOnTheTransaction()
                .clickOnCreateButton();

        assertThat(page.getTransactionsTable()).containsText("id.transaction.");
    }

    @Test
    @TmsLink("873")
    @Epic("System/TransactionManagement")
    @Feature("Add adjustment")
    @Description("Close button with no changes performed")
    public void testClickCloseButtonWithNoChanges() {
        TransactionManagementPage page = new TransactionManagementPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickTransactionManagementTab()
                .clickAddAdjustmentButton()
                .clickOnTheTransaction()
                .clickOnCloseButton();

        assertThat(page.getTransactionsTable()).containsText("No rows to display.");
    }
}
