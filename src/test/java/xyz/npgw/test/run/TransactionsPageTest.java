package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.AboutBlankPage;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.dialog.TransactionDetailsDialog;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TransactionsPageTest extends BaseTest {

    private static final String ADMIN_COMPANY_NAME = "A2 Company%s".formatted(RUN_ID);

    private static final List<String> COLUMNS_HEADERS = List.of(
            "Creation Date",
            "Business unit ID",
            "NPGW Reference",
            "Merchant Reference",
            "Amount",
            "Currency",
            "Card type",
            "Status");

    private static final String COMPANY_NAME = "Test request company%s".formatted(RUN_ID);
    private static final String MERCHANT_TITLE = "Test request merchant%s".formatted(RUN_ID);
    private BusinessUnit businessUnit;

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME);
        businessUnit = TestUtils.createBusinessUnit(getApiRequestContext(), COMPANY_NAME, MERCHANT_TITLE);
    }

    @Test
    @TmsLink("108")
    @Epic("Transactions")
    @Feature("Navigation")
    @Description("User navigate to 'Transactions page' after clicking on 'Transactions' link on the header")
    public void testNavigateToTransactionsPage() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
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
                .clickTransactionsLink()
                .clickCurrencySelector()
                .selectCurrency(currency)
                .clickRefreshDataButton();

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
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields("01-05-2025", "30-05-2025")
                .clickCurrencySelector()
                .selectCurrency(currency)
                .getTable().getColumnValuesFromAllPages("Currency", Function.identity());

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
                .clickTransactionsLink();

        Allure.step("Verify: default row count - 25");
        assertThat(transactionsPage.getTable().getRowsPerPage()).containsText("25");
    }

    @Test
    @TmsLink("127")
    @Epic("Transactions")
    @Feature("Selector rows options")
    @Description("Displaying rows per page options when clicking on Selector Rows")
    public void testCountOptionsSelectorRows() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getTable().clickRowsPerPageChevron();

        Allure.step("Verify: displaying all options when clicking on Selector Rows");
        assertThat(transactionsPage.getTable().getRowsPerPageOptions()).hasText(new String[]{"10", "25", "50", "100"});
    }

    @Test
    @TmsLink("130")
    @Epic("Transactions")
    @Feature("Pagination")
    @Description("Verifying that we can switch the page when we click next button")
    public void testPaginationNextButton() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields("01-04-2025", "31-05-2025")
                .getTable().clickNextPageButton();

        Allure.step("Verify: button 2 is active");
        assertThat(transactionsPage.getTable().getActivePageButton()).hasText("2");
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
                .clickTransactionsLink();

        Allure.step("Verify: DataRange picker is visible");
        assertThat(transactionsPage.getSelectDateRange().getDateRangeField()).isVisible();

        Allure.step("Verify: Business Unit selector is visible");
        assertThat(transactionsPage.getBusinessUnitSelector()).isVisible();

        Allure.step("Verify: Currency selector is visible");
        assertThat(transactionsPage.getCurrencySelector()).isVisible();

        Allure.step("Verify: Payment method selector is visible");
        assertThat(transactionsPage.getCardTypeSelector()).isVisible();

        Allure.step("Verify: Status selector is visible");
        assertThat(transactionsPage.getSelectStatus().getStatusSelector()).isVisible();

        Allure.step("Verify: Amount button is visible");
        assertThat(transactionsPage.getAmountButton()).isVisible();

        Allure.step("Verify: Reset filter button is visible");
        assertThat(transactionsPage.getResetFilterButton()).isVisible();

        Allure.step("Verify: Apply data button is visible");
        assertThat(transactionsPage.getRefreshDataButton()).isVisible();

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
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectStatus().clickSelector();

        Allure.step("Verify: Selector Status Options are visible");
        assertThat(transactionsPage.getSelectStatus().getStatusOptions()).hasText(new String[]{
                "ALL",
                "INITIATED",
                "PENDING",
                "SUCCESS",
                "FAILED",
                "CANCELLED",
                "EXPIRED"
        });

        Allure.step("Verify: Default selected option in status selector is 'ALL'");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).containsText("ALL");
    }

    @Test
    @TmsLink("263")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("Choose amount popup functionality")
    public void testChooseAmountPopUp() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
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
        assertThat(transactionsPage.amountApplied("Amount: 101 - 4999")).isVisible();

        transactionsPage
                .clickAmountAppliedClearButton();

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
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields("01-04-2025", "01-05-2025")
                .clickAmountButton()
                .fillAmountFromField(String.valueOf(amountFrom))
                .fillAmountToField(String.valueOf(amountTo))
                .clickAmountApplyButton()
                .getTable().getColumnValues("Amount");

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
    public void testTheVisibilityOfTheCardTypeOptions() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .clickCardTypeSelector();

        Allure.step("Verify: Payment Method Options are visible");
        assertEquals(transactionsPage.getCardTypeOptions(), List.of("ALL", "VISA", "MASTERCARD"));

        Allure.step("Verify: Default selected option in Payment Method Options is 'ALL'");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).containsText("ALL");
    }

    @Test
    @TmsLink("340")
    @Epic("Transactions")
    @Feature("Data range")
    @Description("Error message is displayed when start date is after end date.")
    public void testErrorMessageForReversedDateRange() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields("01-04-2025", "01-04-2024")
                .clickRefreshDataButton();

        Allure.step("Verify: error message is shown for invalid date range");
        assertThat(transactionsPage.getSelectDateRange().getErrorMessage())
                .hasText("Start date must be before end date.");
    }

    @Test
    @TmsLink("350")
    @Epic("Transactions")
    @Feature("Transactions table header")
    @Description("Verify full lists of column headers in table and visible columns from Settings")
    public void testCheckUncheckAllVisibleColumns() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .clickSettingsButton()
                .checkAllCheckboxInSettings();

        List<String> visibleColumnsLabels = transactionsPage
                .getVisibleColumnsLabels();

        List<String> headersList = transactionsPage
                .clickRefreshDataButton()
                .getTable().getColumnHeadersText();

        List<String> headersListAfterUncheckAllVisibleColumns = transactionsPage
                .clickSettingsButton()
                .uncheckAllCheckboxInSettings()
                .clickRefreshDataButton()
                .getTable().getColumnHeadersText();

        Allure.step("Verify: All column headers are displayed in the Settings");
        assertEquals(visibleColumnsLabels, COLUMNS_HEADERS);

        Allure.step("Verify: All column headers are displayed in the transactions table");
        assertEquals(headersList, COLUMNS_HEADERS);

        Allure.step("Verify: Column headers are not displayed in the transactions table "
                + "after it's unchecking in the Settings");
        assertEquals(headersListAfterUncheckAllVisibleColumns.size(), 0);
    }

    @Test
    @TmsLink("359")
    @Epic("Transactions")
    @Feature("Settings")
    @Description("Check/Uncheck Visible columns in the Settings and verify table column headers")
    public void testCheckUncheckOneVisibleColumn() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .clickSettingsButton()
                .checkAllCheckboxInSettings();

        COLUMNS_HEADERS.forEach(item -> {
            List<String> headersListAfterUncheckOne = transactionsPage
                    .uncheckVisibleColumn(item)
                    .clickRefreshDataButton()
                    .getTable().getColumnHeadersText();

            Allure.step("Verify: Only one column header is NOT displayed in the Transactions. And it's - '{item}'");
            assertTrue((headersListAfterUncheckOne.size() == COLUMNS_HEADERS.size() - 1)
                    && !headersListAfterUncheckOne.contains(item));

            transactionsPage
                    .clickSettingsButton()
                    .checkVisibleColumn(item);
        });

        transactionsPage
                .uncheckAllCheckboxInSettings();

        COLUMNS_HEADERS.forEach(item -> {
            List<String> headersListAfterCheckOnlyOne = transactionsPage
                    .checkVisibleColumn(item)
                    .clickRefreshDataButton()
                    .getTable().getColumnHeadersText();

            Allure.step("Verify: Only one column header is displayed in the transactions table. And it's - '{item}'");
            assertTrue((headersListAfterCheckOnlyOne.size() == 1) && headersListAfterCheckOnlyOne.contains(item));

            transactionsPage
                    .clickSettingsButton()
                    .uncheckVisibleColumn(item);
        });
    }

    @Test
    @TmsLink("354")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("Edit Amount")
    public void testEditAmount() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .clickAmountButton()
                .fillAmountFromField("500")
                .fillAmountToField("10000")
                .clickAmountApplyButton()
                .clickAmountEditButton()
                .fillAmountFromField("500")
                .fillAmountToField("10300");

        Allure.step("Verify: Edited amount is visible");
        assertThat(transactionsPage.amountApplied("Amount: 500 - 10300")).isVisible();
    }

    @Test
    @TmsLink("355")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("Reset Amount Values")
    public void testResetAmountValues() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .clickAmountButton()
                .fillAmountFromField("500")
                .fillAmountToField("10000")
                .clickAmountApplyButton()
                .clickAmountAppliedClearButton()
                .clickAmountButton();

        Allure.step("Verify: From Amount is zero");
        assertThat(transactionsPage.getAmountFromInputField()).hasValue("0.00");

        Allure.step("Verify: To Amount is zero");
        assertThat(transactionsPage.getAmountToInputField()).hasValue("0.00");

        transactionsPage
                .clickAmountApplyButton();

        Allure.step("Verify: Applied amount is visible");
        assertThat(transactionsPage.amountApplied("Amount: 0.00 - 0.00")).isVisible();
    }

    @Test
    @TmsLink("356")
    @Epic("Transactions")
    @Feature("Export table data")
    @Description("The presence of the dropdown options export table data to  file")
    public void testPresenceOfDownloadFilesOptions() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .clickDownloadButton();

        Allure.step("Verify: CVC option is visible");
        assertThat(transactionsPage.getDownloadCsvOption()).isVisible();

        Allure.step("Verify: EXCEL option is visible");
        assertThat(transactionsPage.getDownloadExcelOption()).isVisible();

        Allure.step("Verify: PDF option is visible");
        assertThat(transactionsPage.getDownloadPdfOption()).isVisible();
    }


    @Test(dataProvider = "getMenuItemName", dataProviderClass = TestDataProvider.class)
    @TmsLink("357")
    @Epic("Transactions")
    @Feature("Export table data")
    @Description("Download files: PDF, Excel, CVS")
    public void testDownloadFiles(String menuItemName) {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .clickDownloadButton();

        Allure.step("Verify: that files can be downloaded");
        Assert.assertTrue(transactionsPage.isFileAvailableAndNotEmpty(menuItemName));
    }

    @Test
    @TmsLink("520")
    @Epic("Transactions")
    @Feature("Business unit")
    @Description("Verify that the Company admin can see all the company's business units in the Business unit "
            + "dropdown list")
    public void testTheVisibilityOfTheAvailableBusinessUnitOptions(@Optional("UNAUTHORISED") String userRole) {
        String[] businessUnitNames = new String[]{"Business unit 1", "Business unit 2", "Business unit 3",
                "Business unit 4"};
        String companyAdminEmail = "companyAdmin@gmail.com";
        String companyAdminPassword = "CompanyAdmin1!";
        TestUtils.deleteUser(getApiRequestContext(), companyAdminEmail);
//        TestUtils.deleteCompany(getApiRequestContext(), ADMIN_COMPANY_NAME);
        TestUtils.createCompany(getApiRequestContext(), ADMIN_COMPANY_NAME);
        TestUtils.createCompanyAdmin(
                getApiRequestContext(), ADMIN_COMPANY_NAME, companyAdminEmail, companyAdminPassword);
        Arrays.stream(businessUnitNames).forEach(businessUnitName -> TestUtils.createBusinessUnit(
                getApiRequestContext(), ADMIN_COMPANY_NAME, businessUnitName));

        TransactionsPage transactionsPage = new AboutBlankPage((getPage()))
                .navigate("/login")
                .login(companyAdminEmail, companyAdminPassword)
                .clickTransactionsLink()
                .getSelectBusinessUnit().clickSelectBusinessUnitPlaceholder();

        Allure.step("Verify: Company's business units are visible");
        assertThat(transactionsPage.getSelectBusinessUnit().getDropdownOptionList()).hasText(businessUnitNames);
    }

    @Test(dataProvider = "getCurrency", dataProviderClass = TestDataProvider.class)
    @TmsLink("567")
    @Epic("Transactions")
    @Feature("Reset filter button")
    @Description("Verify, that 'Reset filter' button change 'Currency' to default value ( ALL)")
    public void testResetCurrency(String currency) {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink();

        Allure.step("Verify: Filter displays 'ALL' by default");
        assertThat(transactionsPage.getCurrencySelector()).containsText("ALL");

        transactionsPage
                .clickCurrencySelector()
                .selectCurrency(currency);

        Allure.step("Verify: Filter displays the selected currency");
        assertThat(transactionsPage.getCurrencySelector()).containsText(currency);

        transactionsPage
                .clickResetFilterButton();

        Allure.step("Verify: Filter displays 'ALL' after applying 'Reset filter' button ");
        assertThat(transactionsPage.getCurrencySelector()).containsText("ALL");
    }

    @Test
    @TmsLink("620")
    @Epic("Transactions")
    @Feature("Refresh data")
    @Description("Verify the request to server contains all the information from the filter")
    public void testRequestToServer() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(MERCHANT_TITLE)
                .getSelectDateRange().setDateRangeFields("01-05-2025", "07-05-2025")
                .clickCurrencySelector()
                .selectCurrency("USD")
                .selectCardType("VISA")
                .clickAmountButton()
                .fillAmountFromField("500")
                .fillAmountToField("10000")
                .clickAmountApplyButton();

        Allure.step("Verify: merchant ID is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains(businessUnit.merchantId()));

        Allure.step("Verify: dateFrom is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains("2025-05-01T00:00:00.000Z"));

        Allure.step("Verify: dateTo is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains("2025-05-07T23:59:59.999Z"));

        Allure.step("Verify: currency is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains("USD"));

        Allure.step("Verify: paymentMethod is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains("VISA"));

        Allure.step("Verify:amountFrom is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains("500"));

        Allure.step("Verify: amountTo is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains("10000"));
    }

    // TODO bug - status isn't sent to server
    @Test(expectedExceptions = AssertionError.class)
    @TmsLink("621")
    @Epic("Transactions")
    @Feature("Refresh data")
    @Description("Verify the status is sent to the server")
    public void testStatusRequestServer() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(MERCHANT_TITLE)
                .getSelectStatus().clickSelector()
                .getSelectStatus().clickValue("SUCCESS")
                .getSelectStatus().clickSelector();

        Allure.step("Verify: status is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains("SUCCESS"));
    }

    @Test(dataProvider = "getCardType", dataProviderClass = TestDataProvider.class)
    @TmsLink("598")
    @Epic("Transactions")
    @Feature("Reset filter button")
    @Description("Verify, that 'Reset filter' button change 'Card Type' to default value ( ALL)")
    public void testResetPaymentMethod(String getCardType) {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink();

        Allure.step("Verify: Filter displays 'ALL' by default");
        assertThat(transactionsPage.getCardTypeValue()).containsText("ALL");

        transactionsPage
                .selectCardType(getCardType);

        Allure.step("Verify: Filter displays the selected payment method");
        assertThat(transactionsPage.getCardTypeValue()).containsText(getCardType);

        transactionsPage
                .clickResetFilterButton();

        Allure.step("Verify: Filter displays 'ALL' after applying 'Reset filter' button");
        assertThat(transactionsPage.getCardTypeValue()).containsText("ALL");
    }

    @Test(dataProvider = "getStatus", dataProviderClass = TestDataProvider.class)
    @TmsLink("639")
    @Epic("Transactions")
    @Feature("Reset filter button")
    @Description("Verify, that 'Reset filter' button change 'Status' to default value ( ALL)")
    public void testResetStatus(String status) {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink();

        Allure.step("Verify: Filter displays 'ALL' by default");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).hasText("ALL");

        transactionsPage
                .getSelectStatus().selectTransactionStatuses(status);

        Allure.step("Verify: Filter displays the selected Status");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).hasText(status);

        transactionsPage
                .clickResetFilterButton();

        Allure.step("Verify: Filter displays 'ALL' after applying 'Reset filter' button");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).hasText("ALL");
    }

    @Test(dataProvider = "getMultiStatus2", dataProviderClass = TestDataProvider.class)
    @TmsLink("655")
    @Epic("Transactions")
    @Feature("Reset filter button")
    @Description("Verify, that 'Reset filter' button change 'Status' (two options are checked) to default value ( ALL)")
    public void testResetMultiStatus(String status1, String status2) {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink();

        Allure.step("Verify: Filter displays 'ALL' by default");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).hasText("ALL");

        transactionsPage.getSelectStatus().selectTransactionStatuses(status1, status2);

        Allure.step("Verify: Filter displays the selected Status");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).hasText(status1 + ", " + status2);

        transactionsPage.clickResetFilterButton();

        Allure.step("Verify: Filter displays 'ALL' after applying 'Reset filter' button");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).hasText("ALL");
    }

    @Test(dataProvider = "getCurrency", dataProviderClass = TestDataProvider.class)
    @TmsLink("657")
    @Epic("Transactions")
    @Feature("Currency")
    @Description("Compare number of transactions with same currency in the table before and after filter")
    public void testDisplayAllFilteredByCurrencyRows(String currency) {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields("28-05-2025", "31-05-2025");

        int currencyCount = transactionsPage.getTable().countValues("Currency", currency);
        transactionsPage.getTable().goToFirstPageIfNeeded();

        int filteredTransactionCount = transactionsPage
                .clickCurrencySelector().selectCurrency(currency)
                .getTable().countValues("Currency", currency);

        int totalFilteredRows = transactionsPage.getTable().countAllRows();

        Allure.step("Verify: All transactions with selected currency are shown after filter.");
        assertEquals(currencyCount, filteredTransactionCount);

        Allure.step("Verify: Only transactions with selected currency are shown after filter.");
        assertEquals(totalFilteredRows, filteredTransactionCount);

    }


    @Test
    @TmsLink("638")
    @Epic("Transactions")
    @Feature("Transaction details")
    @Description("Check that after click on transactions in column NPGW reference user see transaction details")
    public void testCheckTransactionDetails() {
        TransactionDetailsDialog transactionDetailsDialog = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getTable().clickOnTransaction();

        assertThat(transactionDetailsDialog.getDialogHeader()).hasText("Transaction Details");
        assertThat(transactionDetailsDialog.getDialog()).containsText("Status");
        assertTrue(transactionDetailsDialog.getStatusField().isVisible());
        assertThat(transactionDetailsDialog.getDialog()).containsText("Amount");
        assertTrue(transactionDetailsDialog.getAmountField().isVisible());
        assertThat(transactionDetailsDialog.getDialog()).containsText("Merchant reference");
        assertTrue(transactionDetailsDialog.getMerchantReferenceField().isVisible());
        assertThat(transactionDetailsDialog.getDialog()).containsText("Card details");
        assertTrue(transactionDetailsDialog.getCardDetailsField().isVisible());
        assertThat(transactionDetailsDialog.getCardDetailsField()).containsText("Payment method");
        assertThat(transactionDetailsDialog.getCardDetailsField()).containsText("Card type");
        assertThat(transactionDetailsDialog.getCardDetailsField()).containsText("Card holder");
        assertThat(transactionDetailsDialog.getCardDetailsField()).containsText("Card number");
        assertThat(transactionDetailsDialog.getCardDetailsField()).containsText("Expiry date");
    }

    @Test
    @TmsLink("559")
    @Epic("Transactions")
    @Feature("Transaction sorting")
    @Description("'Creation Date' column sorts ascending by default and descending on click.")
    public void testSortCreationData() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink();

        List<LocalDateTime> actualDates = transactionsPage
                .getTable().getAllCreationDates();

        Allure.step("Verify: transactions are sorted by creation date in ascending order by default");
        assertEquals(actualDates, actualDates.stream().sorted().toList());

        transactionsPage
                .getTable().clickSortIcon("Creation Date");

        Allure.step(
                "Verify: transactions are sorted by creation date in descending order after clicking the sort icon");
        assertEquals(transactionsPage.getTable().getAllCreationDates(),
                actualDates.stream().sorted(Comparator.reverseOrder()).toList());
    }

    @Test
    @TmsLink("659")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("'Amount' column sorts ascending on first click and descending on second click.")
    public void testSortAmount() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getTable().selectRowsPerPageOption("100")
                .getTable().clickSortIcon("Amount");

        List<Double> actualAmount = transactionsPage
                .getTable().getAllAmounts();

        Allure.step("Verify: transactions are sorted by amount in ascending order after first click");
        assertEquals(actualAmount, actualAmount.stream().sorted().toList());

        transactionsPage
                .getTable().clickSortIcon("Amount");

        Allure.step("Verify: transactions are sorted by amount in descending order after second click");
        assertEquals(transactionsPage.getTable().getAllAmounts(),
                actualAmount.stream().sorted(Comparator.reverseOrder()).toList());
    }

    @Test(dataProvider = "getCardType", dataProviderClass = TestDataProvider.class)
    @TmsLink("673")
    @Epic("Transactions")
    @Feature("Payment method")
    @Description("Filtering transactions by Card type displays only matching entries in the table.")
    public void testFilterByCardType(String cardType) {
        List<String> cardTypeList = new DashboardPage(getPage())
                .clickTransactionsLink()
                .selectCardType(cardType)
                .getTable().getColumnValuesFromAllPages("Card type", Function.identity());

        Allure.step("Verify: all entries in the 'Card type' column match the selected filter");
        assertTrue(cardTypeList.stream().allMatch(value -> value.equals(cardType)));
    }

    @Test
    @TmsLink("668")
    @Epic("Transactions")
    @Feature("Reset filter button")
    @Description("Verify, that 'Reset filter' button change 'Amount' to default value ( AMOUNT)")
    public void testResetAmount() {

        final String amountFrom = "10";
        final String amountTo = "20";
        final String chosenAmount = "Amount: " + amountFrom + " - " + amountTo;

        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink();

        Allure.step("Verify: Filter 'Amount' displays 'Amount' by default");
        assertThat(transactionsPage.getAmountButton()).isVisible();
        assertThat(transactionsPage.getAmountButton()).hasText("Amount");

        transactionsPage.clickAmountButton()
                .fillAmountFromField(amountFrom)
                .fillAmountToField(amountTo)
                .clickAmountApplyButton();

        Allure.step("Verify: Filter 'Amount' displays 'Amount: {amountFrom}- {amountTo}'");
        assertThat(transactionsPage.amountApplied(chosenAmount)).isVisible();
        assertThat(transactionsPage.amountApplied(chosenAmount)).hasText(chosenAmount);

        transactionsPage.clickResetFilterButton();

        Allure.step("Verify: Filter 'Amount' displays 'Amount' by default");
        assertThat(transactionsPage.getAmountButton()).isVisible();
        assertThat(transactionsPage.getAmountButton()).hasText("Amount");
    }

    // TODO bug - status isn't sent to server
    @Test(expectedExceptions = AssertionError.class,
            dataProvider = "getMultiStatus2", dataProviderClass = TestDataProvider.class)
    @TmsLink("679")
    @Epic("Transactions")
    @Feature("Status")
    @Description("Compare number of transactions with selected statuses in the table before and after filter")
    public void testDisplayAllFilteredByStatusRows(String firstStatus, String secondStatus) {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields("27-05-2025", "31-05-2025");

        int statusesCount = transactionsPage
                .getTable().countValues("Status", firstStatus, secondStatus);

        transactionsPage.getTable().goToFirstPageIfNeeded();

        int filteredTransactionCount  = transactionsPage
                .getSelectStatus().selectTransactionStatuses(firstStatus, secondStatus)
                .getTable().countValues("Status", firstStatus, secondStatus);

        int totalFilteredRows = transactionsPage.getTable().countAllRows();

        Allure.step("Verify: All transactions with selected statuses are shown after filter.");
        assertEquals(statusesCount, filteredTransactionCount);

        Allure.step("Verify: Only transactions with selected statuses are shown after filter.");
        assertEquals(totalFilteredRows, filteredTransactionCount);
    }

    // TODO bug - transactions isn't present in the table when a currency filter is applied on the last page
    @Test(expectedExceptions = AssertionError.class)
    @TmsLink("682")
    @Epic("Transactions")
    @Feature("Pagination")
    @Description("Verify that transactions are present in the table when a currency filter is applied on the last page")
    public void testTableDisplayWhenCurrencyFilterAppliedWhileOnLastPage() {
        String euro = "EUR";

        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields("26-05-2025", "31-05-2025");

        int numberWithEuroInTable = transactionsPage.getTable().countValues("Currency", euro);
        transactionsPage.getTable().goToLastPageIfNeeded();
        transactionsPage.clickCurrencySelector().selectCurrency(euro);

        Allure.step("Verify: Transactions are present in the table");
        assertTrue(numberWithEuroInTable > 0 && !transactionsPage.getTable().isTableEmpty());
    }

    @Test
    @TmsLink("677")
    @Epic("Transactions")
    @Feature("Date range")
    @Description("Filtering transactions by date range")
    public void testFilteringTransactionsByDataRange() {
        String startDate = "01-06-2025";
        String endDate = "05-06-2025";

        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(startDate, endDate)
                .clickRefreshDataButton();

        Allure.step("Verify: Transactions can be filtered by date range");
        Assert.assertTrue(transactionsPage.getTable().isBetween(startDate, endDate));
    }

    @Test
    @TmsLink("687")
    @Epic("Transactions")
    @Feature("Reset filter button")
    @Description("Verify, that 'Reset filter' clean 'Company' input field")
    public void testResetCompany() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink();

        Allure.step("Verify: the 'Company' input field is empty by default");
        assertThat(transactionsPage.getSelectCompany().getSelectCompanyField()).isEmpty();

        transactionsPage.getSelectCompany().clickSelectCompanyDropdownChevron()
                        .getSelectCompany().clickSelectCompanyField()
                        .getSelectCompany().selectFirstCompany();

        String firstCompanyName = transactionsPage.getSelectCompany().firstCompanyName();

        Allure.step("Verify: selected company is displayed in the 'Company' input field");
        assertThat(transactionsPage.getSelectCompany().getSelectCompanyField()).hasValue(firstCompanyName);

        transactionsPage.clickResetFilterButton();

        Allure.step("Verify: the 'Company' input field is empty after reset");
        assertThat(transactionsPage.getSelectCompany().getSelectCompanyField()).isEmpty();

    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteBusinessUnit(getApiRequestContext(), COMPANY_NAME, businessUnit);
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        super.afterClass();
    }
}
