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
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.TransactionsPage;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
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

    @Test(dataProvider = "getCurrency", dataProviderClass = TestDataProvider.class)
    @TmsLink("128")
    @Epic("Transactions")
    @Feature("Currency")
    @Description("Displaying selected currency")
    public void testFilterDisplaysSelectedCurrency(String currency) {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader()
                .clickTransactionsLink()
                .clickCurrencySelector()
                .clickCurrency(currency)
                .clickApplyDataButton();

        Allure.step("Verify: Filter displays the selected currency");
        assertThat(transactionsPage.getCurrencySelector()).containsText(currency);
    }

    @Test(dataProvider = "getCurrency", dataProviderClass = TestDataProvider.class)
    @TmsLink("319")
    @Epic("Transactions")
    @Feature("Currency")
    @Description("Filtering transactions by Currency")
    public void testFilterTransactionsByCurrency(String currency) {
        List<String> currencyValues = new DashboardPage(getPage())
                .getHeader()
                .clickTransactionsLink()
                .clickCurrencySelector()
                .clickCurrency(currency)
                .getTable()
                .getColumnValues("Currency");

        Allure.step("Verify: All values in the Currency column match the selected currency");
        assertTrue(currencyValues.stream().allMatch(value -> value.equals(currency)));
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
    @Feature("Selector rows options")
    @Description("Displaying rows per page options when clicking on Selector Rows")
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

        Allure.step("Verify: Apply data button is visible");
        assertThat(transactionsPage.getApplyDataButton()).isVisible();

        Allure.step("Verify: Settings button is visible");
        assertThat(transactionsPage.getSettingsButton()).isVisible();

        Allure.step("Verify: Download button is visible");
        assertThat(transactionsPage.getDownloadButton()).isVisible();
    }

    @Test
    @TmsLink("229")
    @Epic("Transactions")
    @Feature("Status")
    @Description("Verify that user can see selector Status Options")
    public void testTheVisibilityOfTheStatusSelectorOptions() {

        List<String> options = List.of("ALL",
                "INITIATED",
                "PENDING",
                "SUCCESS",
                "FAILED",
                "CANCELLED",
                "EXPIRED");

        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader()
                .clickTransactionsLink()
                .clickStatusSelector();

        Allure.step("Verify: Selector Status Options are visible");
        assertEquals(transactionsPage.getStatusSelectorOptions(), options);
        Allure.step("Verify: Default selected option in status selector is 'ALL'");
        assertThat(transactionsPage.getActiveOption()).containsText("ALL");
    }

    @Ignore("fail after latest update")
    @Test
    @TmsLink("263")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("Choose amount popup functionality")
    public void testChooseAmountPopUp() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader()
                .clickTransactionsLink()
                .clickAmountButton()
                .fillAmountFromField("10")
                .fillAmountFromField("20")
                .clickAmountClearButton()
                .fillAmountFromField("100")
                .clickAmountFromIncreaseArrow()
                .clickAmountFromIncreaseArrow()
                .clickAmountFromDecreaseArrow()
                .clickClearAmountToButton()
                .fillAmountToField("5000")
                .clickAmountToIncreaseArrow()
                .clickAmountToDecreaseArrow()
                .clickAmountToDecreaseArrow()
                .clickAmountApplyButton();

        Allure.step("Verify: Applied amount is visible");
        assertThat(transactionsPage.getAmountApplied()).isVisible();

        transactionsPage.clickAmountAppliedClearButton();

        Allure.step("Verify: Amount button is visible after reset");
        assertThat(transactionsPage.getAmountButton()).isVisible();
    }

    @Test
    @TmsLink("311")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("Filtering transactions by Amount")
    public void testFilterTransactionsByAmount() {
        int amountFrom = 10;
        int amountTo = 500;

        List<String> amountValues = new DashboardPage(getPage())
                .getHeader()
                .clickTransactionsLink()
                .clickAmountButton()
                .fillAmountFromField(String.valueOf(amountFrom))
                .fillAmountToField(String.valueOf(amountTo))
                .clickAmountApplyButton()
                .getTable()
                .getColumnValues("Amount");

        Allure.step("Verify: Amount column has values within the specified amount range");
        assertTrue(amountValues.stream()
                .map(Integer::parseInt)
                .allMatch(value -> value >= amountFrom && value <= amountTo));
    }

    @Test
    @TmsLink("335")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("error message 'From should be lesser than To' appears")
    public void testErrorMessageByAmount() {

        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader()
                .clickTransactionsLink()
                .clickAmountButton()
                .fillAmountFromField("500")
                .fillAmountToField("10");

        Allure.step("Verify: error message 'From should be lesser than To' appears");
        assertThat(transactionsPage.getAmountErrorMessage()).hasText("\"From\" should be lesser than \"To");
    }

    @Test
    @TmsLink("342")
    @Epic("Transactions")
    @Feature("Status")
    @Description("Verify that user can see Payment Method Options")
    public void testTheVisibilityOfThePaymentMethodOptions() {

        List<String> options = List.of("ALL",
                "VISA",
                "Mastercard");

        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader()
                .clickTransactionsLink()
                .clickPaymentMethodSelector();

        Allure.step("Verify: Payment Method Options are visible");
        assertEquals(transactionsPage.getPaymentMethodOptions(), options);
        Allure.step("Verify: Default selected option in Payment Method Options is 'ALL'");
        assertThat(transactionsPage.getActiveOption()).containsText("ALL");
    }

    @Test
    @TmsLink("340")
    @Epic("Transactions")
    @Feature("Data range")
    @Description("Error message is displayed when start date is after end date.")
    public void testErrorMessageForReversedDateRange() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader()
                .clickTransactionsLink()
                .setStartDate("01-04-2025")
                .setEndDate("01-04-2024")
                .clickApplyDataButton();

        Allure.step("Verify: error message is shown for invalid date range");
        assertThat(transactionsPage.getDataRangeErrorMessage()).hasText("Start date must be before end date.");
    }
}
