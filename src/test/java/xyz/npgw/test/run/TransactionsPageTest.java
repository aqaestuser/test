package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.TransactionsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertTrue;

public class TransactionsPageTest extends BaseTest {

    @Test
    @TmsLink("108")
    @Epic("Transactions")
    @Feature("Navigation")
    @Description("User navigate to 'Transactions page' after clicking on 'Transactions' menu on the header")
    public void testNavigateToTransactionsPage() {
        TransactionsPage transaction = new DashboardPage(getPage())
                .clickTransactionsLink();

        Allure.step("Verify: Transactions Page URL");
        assertThat(transaction.getPage()).hasURL(Constants.TRANSACTIONS_PAGE_URL);

        Allure.step("Verify: Transactions Page Title");
        assertThat(transaction.getPage()).hasTitle(Constants.TRANSACTIONS_URL_TITLE);
    }

    @Test
    @TmsLink("128")
    @Epic("Transactions")
    @Feature("Displaying currency filter: EUR")
    @Description("Displaying the currency filter on the screen when selecting EUR.")
    public void testFilterByCurrency() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .clickCurrencySelector()
                .clickCurrency("EUR")
                .clickApplyDataIcon();

        Allure.step("Verify: currency filter - EUR");
        assertTrue(transactionsPage.getTableRow("EUR"));
    }

    @Test
    @TmsLink("106")
    @Epic("Transactions")
    @Feature("Number of lines per page field")
    @Description("Displaying the number of rows on the screen when selecting Selector Rows.")
    public void testCountSelectorRows() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink();

        Allure.step("Verify: default row count - 25");
        assertThat(transactionsPage.getRowsPerPageButton()).containsText("25");
    }

    @Test
    @TmsLink("127")
    @Epic("Transactions")
    @Feature("Selector Rows options")
    @Description("Displaying options when clicking on Selector Rows")
    public void testCountOptionsSelectorRows() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .clickRowsPerPageButton();

        Allure.step("Verify: displaying all options when clicking on Selector Rows");
        assertThat(transactionsPage.getRowsPerPageOptions()).hasText("102550100");
    }

    @Test
    @TmsLink("130")
    @Epic("Transactions")
    @Feature("Pagination")
    @Description("Verifying that we can switch the page when we click next button")
    public void testPaginationNextButton() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .clickNextPageButton();

        Allure.step("Verify: button 2 is active");
        assertThat(transactionsPage.getPaginationItemTwoActiveButton()).isVisible();
    }
}
