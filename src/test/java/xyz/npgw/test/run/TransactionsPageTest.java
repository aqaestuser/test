package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Ignore;
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
    @Description("User navigate to 'Transactions page' after clicking on 'Transactions' link on the header")
    public void testNavigateToTransactionsPage() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader()
                .clickTransactionsLink();

        Allure.step("Verify: Transactions Page URL");
        assertThat(transactionsPage.getPage()).hasURL(Constants.TRANSACTIONS_PAGE_URL);

        Allure.step("Verify: Transactions Page Title");
        assertThat(transactionsPage.getPage()).hasTitle(Constants.TRANSACTIONS_URL_TITLE);
    }

    @Ignore
    @Test
    @TmsLink("128")
    @Epic("Transactions")
    @Feature("Displaying currency filter: EUR")
    @Description("Displaying the currency filter on the screen when selecting EUR.")
    public void testFilterByCurrency() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader()
                .clickTransactionsLink()
                .clickCurrencySelector()
                .clickCurrency("EUR")
                .clickApplyDataButton();

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
                .getHeader()
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
                .getHeader()
                .clickTransactionsLink()
                .clickRowsPerPageButton();

        Allure.step("Verify: displaying all options when clicking on Selector Rows");
        assertThat(transactionsPage.getRowsPerPageOptions()).hasText("102550100");
    }

    @Ignore
    @Test
    @TmsLink("130")
    @Epic("Transactions")
    @Feature("Pagination")
    @Description("Verifying that we can switch the page when we click next button")
    public void testPaginationNextButton() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader()
                .clickTransactionsLink()
                .clickNextPageButton();

        Allure.step("Verify: button 2 is active");
        assertThat(transactionsPage.getPaginationItemTwoActiveButton()).isVisible();
    }

    @Test
    @TmsLink("181")
    @Epic("Transactions")
    @Feature("Panel")
    @Description("Verify that on Transactions Page after clicking on Transactions user can see Panel:"
            + " Date range, Business unit, Currency, Payment method, Status, Amount, Reset filter, "
            + "Apply data, Download file, Settings.")
    public void testTheVisibilityOfTheControlPanelElementsOnTheTransactionsPage() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader()
                .clickTransactionsLink();

        Allure.step("Verify: DataRange picker is visible");
        assertThat(transactionsPage.getDateRangePicker()).isVisible();

        Allure.step("Verify: Business Unit selector is visible");
        assertThat(transactionsPage.getBusinessUnitSelector()).isVisible();

        Allure.step("Verify: Currency selector is visible");
        assertThat(transactionsPage.getCurrencySelector()).isVisible();

        Allure.step("Verify: Payment method selector is visible");
        assertThat(transactionsPage.getPaymentMethodSelector()).isVisible();

        Allure.step("Verify: Status selector is visible");
        assertThat(transactionsPage.getStatusSelector()).isVisible();

        Allure.step("Verify: Amount button is visible");
        assertThat(transactionsPage.getAmountButton()).isVisible();

        Allure.step("Verify: Reset filter button is visible");
        assertThat(transactionsPage.getResetFilterButton()).isVisible();

        Allure.step("Verify: Reset filter button is visible");
        assertThat(transactionsPage.getResetFilterButton()).isVisible();

        Allure.step("Verify: Apply data button is visible");
        assertThat(transactionsPage.getApplyDataButton()).isVisible();

        Allure.step("Verify: Settings button is visible");
        assertThat(transactionsPage.getSettingsButton()).isVisible();

        Allure.step("Verify: Download button is visible");
        assertThat(transactionsPage.getDownloadButton()).isVisible();
    }
}
