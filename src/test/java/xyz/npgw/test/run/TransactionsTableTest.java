package xyz.npgw.test.run;

import com.google.gson.Gson;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.entity.CardType;
import xyz.npgw.test.common.entity.Currency;
import xyz.npgw.test.common.entity.Status;
import xyz.npgw.test.common.entity.Transaction;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.dialog.transactions.RefundTransactionDialog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static xyz.npgw.test.common.Constants.BUSINESS_UNIT_FOR_TEST_RUN;
import static xyz.npgw.test.common.Constants.COMPANY_NAME_FOR_TEST_RUN;

public class TransactionsTableTest extends BaseTest {

    private static final String MERCHANT_TITLE = "%s test transaction table merchant".formatted(RUN_ID);
    private static final String[] COLUMNS_HEADERS = {
            "Creation Date (GMT)",
            "Business unit ID",
            "NPGW reference",
            "Business unit reference",
            "Amount",
            "Currency",
            "Card type",
            "Status",
            "Actions"};
    private static final String[] SETTINGS_COLUMNS = Arrays.copyOf(COLUMNS_HEADERS, COLUMNS_HEADERS.length - 1);

    private BusinessUnit businessUnit;

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        businessUnit = TestUtils.createBusinessUnit(getApiRequestContext(), getCompanyName(), MERCHANT_TITLE);
    }

    @Test
    @TmsLink("311")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("Filtering transactions by Amount")
    public void testFilterTransactionsByAmount() {
        String amountFrom = "10.12";
        String amountTo = "500.34";

        List<String> amountValues = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(TestUtils.lastBuildDate(getApiRequestContext()))
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .clickAmountButton()
                .fillAmountFromField(amountFrom)
                .fillAmountToField(amountTo)
                .clickAmountApplyButton()
                .getTable().getColumnValues("Amount");

        Allure.step("Verify: Amount column has values within the specified amount range");
        assertFalse(amountValues.isEmpty());
        assertTrue(amountValues.stream()
                .map(Double::parseDouble)
                .allMatch(value -> value >= Double.parseDouble(amountFrom) && value <= Double.parseDouble(amountTo)));
    }

    @Test(dataProvider = "getCardType", dataProviderClass = TestDataProvider.class)
    @TmsLink("673")
    @Epic("Transactions")
    @Feature("Filter")
    @Description("Filtering transactions by Card type displays only matching entries in the table.")
    public void testFilterByCardType(String cardType) {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        Allure.step("Verify: transaction page table has data");
        assertThat(transactionsPage.getTable().getNoRowsToDisplayMessage()).isHidden();

        List<String> cardTypeList = transactionsPage.selectCardType(cardType)
                .getTable().getCardTypeColumnValuesAllPages();

        Allure.step("Verify: all entries in the 'Card type' column match the selected filter");
        assertTrue(cardTypeList.stream().allMatch(value -> value.equals(cardType)));
    }

    @Test
    @TmsLink("677")
    @Epic("Transactions")
    @Feature("Filter")
    @Description("Filtering transactions by date range")
    public void testFilteringTransactionsByDateRange() {
        String startDate = "01-06-2025";
        String endDate = "05-06-2025";

        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(startDate, endDate)
                .clickRefreshDataButton();

        Allure.step("Verify: Transactions can be filtered by date range");
        assertTrue(transactionsPage.getTable().isBetween(startDate, endDate));
    }

    @Test(dataProvider = "getStatus", dataProviderClass = TestDataProvider.class)
    @TmsLink("679")
    @Epic("Transactions")
    @Feature("Filter")
    @Description("Compare number of transactions with selected statuses in the table before and after filter")
    public void testFilterByStatus(String status) {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(TestUtils.lastBuildDate(getApiRequestContext()))
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        Allure.step("Verify: transaction page table has data");
        assertThat(transactionsPage.getTable().getNoRowsToDisplayMessage()).isHidden();

        int statusesCount = transactionsPage
                .getTable().countValues("Status", status);

        int filteredTransactionCount = transactionsPage
                .getSelectStatus().select(status)
                .getTable().countValues("Status", status);

        int totalFilteredRows = transactionsPage.getTable().countAllRows();

        Allure.step("Verify: All transactions with selected statuses are shown after filter.");
        assertEquals(statusesCount, filteredTransactionCount);

        Allure.step("Verify: Only transactions with selected statuses are shown after filter.");
        assertEquals(totalFilteredRows, filteredTransactionCount);
    }

    @Test(dataProvider = "getCurrency", dataProviderClass = TestDataProvider.class)
    @TmsLink("319")
    @Epic("Transactions")
    @Feature("Filter")
    @Description("Filtering transactions by Currency")
    public void testFilterTransactionsByCurrency(String currency) {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(TestUtils.lastBuildDate(getApiRequestContext()))
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        Allure.step("Verify: transaction page table has data");
        assertThat(transactionsPage.getTable().getNoRowsToDisplayMessage()).isHidden();

        List<String> currencyValues = transactionsPage.clickCurrencySelector()
                .selectCurrency(currency)
                .getTable().getColumnValuesFromAllPages("Currency");

        Allure.step("Verify: Filter displays the selected currency");
        assertThat(transactionsPage.getCurrencySelector()).containsText(currency);

        Allure.step("Verify: All values in the Currency column match the selected currency");
        assertTrue(currencyValues.stream().allMatch(value -> value.equals(currency)));
    }

    @Test
    @TmsLink("682")
    @Epic("Transactions")
    @Feature("Filter")
    @Description("Verify that transactions are present in the table when a currency filter is applied on the last page")
    public void testTableDisplayWhenCurrencyFilterAppliedWhileOnLastPage() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        Allure.step("Verify: Transactions are present in the table");
        assertThat(transactionsPage.getTable().getRows()).not().hasCount(0);

        transactionsPage.getTable().goToLastPage();

        transactionsPage.clickCurrencySelector().selectCurrency("EUR");

        Allure.step("Verify: Transactions are still present then filter is applied on the last page");
        assertThat(transactionsPage.getTable().getRows()).not().hasCount(0);
    }

    @Test
    @TmsLink("559")
    @Epic("Transactions")
    @Feature("Table sorting")
    @Description("'Creation Date' column sorts descending by default and ascending on click.")
    public void testSortByCreationDate() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        List<LocalDateTime> actualDates = transactionsPage
                .getTable().getAllCreationDates();

        Allure.step("Verify: transactions are sorted by creation date in descending order by default");
        assertEquals(actualDates, actualDates.stream().sorted(Comparator.reverseOrder()).toList());

        transactionsPage.getTable().clickSortIcon("Creation Date (GMT)");

        Allure.step("Verify: transactions are sorted by creation date in ascending after clicking the sort icon");
        assertEquals(transactionsPage.getTable().getAllCreationDates(), actualDates.stream().sorted().toList());
    }

    @Test
    @TmsLink("659")
    @Epic("Transactions")
    @Feature("Table sorting")
    @Description("'Amount' column sorts ascending on first click and descending on second click.")
    public void testSortByAmount() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .getTable().selectRowsPerPageOption("100")
                .getTable().clickSortIcon("Amount");

        List<Double> actualAmount = transactionsPage
                .getTable().getAllAmounts();

        Allure.step("Verify: transactions are sorted by amount in ascending order after first click");
        assertEquals(actualAmount, actualAmount.stream().sorted().toList());

        transactionsPage.getTable().clickSortIcon("Amount");

        Allure.step("Verify: transactions are sorted by amount in descending order after second click");
        assertEquals(transactionsPage.getTable().getAllAmounts(),
                actualAmount.stream().sorted(Comparator.reverseOrder()).toList());
    }

    @Test
    @TmsLink("106")
    @Epic("Transactions")
    @Feature("Pagination")
    @Description("Displaying the default number of rows on the RowsPerPage selector")
    public void testCountSelectorRows() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        Allure.step("Verify: default row count - 25");
        assertThat(transactionsPage.getTable().getRowsPerPage()).containsText("25");
    }

    @Test
    @TmsLink("127")
    @Epic("Transactions")
    @Feature("Pagination")
    @Description("Displaying rows per page options when clicking on the RowsPerPage selector")
    public void testCountOptionsSelectorRows() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
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
                .getSelectDateRange().setDateRangeFields(TestUtils.lastBuildDate(getApiRequestContext()))
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .getTable().selectRowsPerPageOption("10")
                .getTable().clickNextPageButton();

        Allure.step("Verify: button 2 is active");
        assertThat(transactionsPage.getTable().getActivePageButton()).hasText("2");
    }

    @Test
    @TmsLink("350")
    @Epic("Transactions")
    @Feature("Settings")
    @Description("Verify full lists of column headers in table and visible columns from Settings")
    public void testCheckUncheckAllVisibleColumns() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .clickSettingsButton()
                .checkAllCheckboxInSettings()
                .clickRefreshDataButton();

        Allure.step("Verify: All column headers except 'Actions' are displayed in the Settings");
        assertThat(transactionsPage.getColumns()).hasText(SETTINGS_COLUMNS);

        Allure.step("Verify: All column headers are displayed in the transactions table");
        assertThat(transactionsPage.getTable().getColumnHeaders()).hasText(COLUMNS_HEADERS);

        transactionsPage
                .clickSettingsButton()
                .uncheckAllCheckboxInSettings()
                .clickRefreshDataButton();

        Allure.step("Verify: Only 'Actions' column is displayed in the transactions table header");
        assertThat(transactionsPage.getTable().getColumnHeaders()).hasText("Actions");
    }

    @Test
    @TmsLink("358")
    @Epic("Transactions")
    @Feature("Settings")
    @Description("Check/Uncheck Visible columns in the Settings and verify table column headers")
    public void testCheckUncheckOneVisibleColumn() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .clickSettingsButton()
                .checkAllCheckboxInSettings();

        for (String item : SETTINGS_COLUMNS) {
            transactionsPage
                    .uncheckVisibleColumn(item)
                    .clickRefreshDataButton();

            Allure.step("Verify: Only one column header is NOT displayed in the Transactions");
            assertThat(transactionsPage.getTable().getColumnHeaders())
                    .hasText(Arrays.stream(COLUMNS_HEADERS).filter(s -> !s.equals(item)).toArray(String[]::new));

            transactionsPage
                    .clickSettingsButton()
                    .checkVisibleColumn(item);
        }

        transactionsPage
                .uncheckAllCheckboxInSettings();

        for (String item : SETTINGS_COLUMNS) {
            transactionsPage
                    .checkVisibleColumn(item)
                    .clickRefreshDataButton();

            Allure.step("Verify: Only two column headers are displayed in the transactions table");
            assertThat(transactionsPage.getTable().getColumnHeaders()).hasText(new String[]{item, "Actions"});

            transactionsPage
                    .clickSettingsButton()
                    .uncheckVisibleColumn(item);
        }
    }

    @Test
    @TmsLink("712")
    @Epic("Transactions")
    @Feature("Filter")
    @Description("Filter transactions by business unit")
    public void testFilterTransactionsByBusinessUnit() {
        getPage().route("**/status*", route -> {
            if (route.request().postData().contains(businessUnit.merchantId())) {
                List<Transaction> transactionList = new ArrayList<>();
                transactionList.add(new Transaction("2025-06-02T04:18:09.047146423Z",
                        "12345", "", 100,
                        Currency.USD, CardType.VISA, Status.FAILED));
                route.fulfill(new Route.FulfillOptions().setBody(new Gson().toJson(transactionList)));
                return;
            }
            route.fallback();
        });

        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectCompany().selectCompany(getCompanyName())
                .getSelectBusinessUnit().selectBusinessUnit(MERCHANT_TITLE);

        Allure.step("Verify mock transaction is displayed");
        assertThat(transactionsPage.getTable().getFirstRowCell("NPGW reference")).hasText("12345");
    }

    @Test
    @TmsLink("818")
    @Epic("Transactions")
    @Feature("Actions")
    @Description("Refund button is visible only for transactions with status 'SUCCESS'")
    public void testRefundButtonVisibility() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(TestUtils.lastBuildDate(getApiRequestContext()))
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        List<String> statuses = transactionsPage
                .getTable().getAllTransactionsStatusList();

        List<Boolean> refundVisible = transactionsPage
                .getTable().getRefundButtonVisibilityFromAllPages();

        assertFalse(statuses.isEmpty(), "Statuses list should not be empty");

        for (int i = 0; i < statuses.size(); i++) {
            String status = statuses.get(i).trim();
            boolean isVisible = refundVisible.get(i);

            if ("SUCCESS".equals(status)) {
                Allure.step("Verify: refund button is visible at row " + i + " with status: " + status);
                assertTrue(isVisible);
            } else {
                Allure.step("Verify: refund button is NOT visible at row " + i + " with status: " + status);
                assertFalse(isVisible);
            }
        }
    }

    @Test
    @TmsLink("863")
    @Epic("Transactions")
    @Feature("Actions")
    @Description("All refundable transactions display correct refund dialog text and pre-filled refund amount.")
    public void testRefundDialogDisplaysCorrectTextAndAmount() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(TestUtils.lastBuildDate(getApiRequestContext()))
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        do {
            List<Locator> currentRows = transactionsPage.getTable().getRows().all();

            for (Locator row : currentRows) {
                Locator refundButton = transactionsPage.getTable().getRefundButton(row);

                if (refundButton.count() > 0 && refundButton.isVisible()) {
                    String amount = transactionsPage.getTable().getCell(row, "Amount").innerText().trim();
                    String currency = transactionsPage.getTable().getCell(row, "Currency").innerText().trim();

                    RefundTransactionDialog refundTransactionDialog = transactionsPage
                            .getTable().clickRefundTransaction(row);

                    Allure.step("Verify: refund dialog header is correct");
                    assertThat(refundTransactionDialog.getDialogHeader()).hasText("Refund transaction");

                    Allure.step("Verify: refund message contains correct max amount and currency");
                    assertThat(refundTransactionDialog.getRefundMessage(
                            String.format("Enter amount up to %s %s to refund?", amount, currency))).isVisible();

                    Allure.step("Verify: refund amount input is pre-filled with correct value");
                    assertThat(refundTransactionDialog.getAmountToRefundInput()).hasValue(amount);

                    Allure.step("Verify: increase amount button to refund is disabled");
                    assertThat(refundTransactionDialog.getIncreaseAmountToRefundButton()).isDisabled();

                    refundTransactionDialog.clickCloseIcon();
                }
            }

        } while (transactionsPage.getTable().goToNextPage());
    }

    @Test
    @TmsLink("880")
    @Epic("Transactions")
    @Feature("Export table data")
    @Description("The transaction table data on the UI matches the exported CSV file data.")
    public void testTransactionTableMatchesDownloadedCsv() throws IOException {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(TestUtils.lastBuildDate(getApiRequestContext()))
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        List<List<String>> uiRows = new ArrayList<>();
        do {
            List<Locator> rows = transactionsPage.getTable().getRows().all();
            for (Locator row : rows) {
                uiRows.add(transactionsPage.getTable().getRowData(row));
            }
        } while (transactionsPage.getTable().goToNextPage());

        Download download = getPage().waitForDownload(
                new Page.WaitForDownloadOptions().setTimeout(ProjectProperties.getDefaultTimeout() * 6),
                () -> transactionsPage.clickExportTableDataToFileButton().selectCsv());

        Path targetPath = Paths.get("downloads", "transactions-export.csv");
        Files.createDirectories(targetPath.getParent());
        download.saveAs(targetPath);

        List<List<String>> rowsFromCsv = transactionsPage
                .readCsv(targetPath);
        List<String> csvHeader = rowsFromCsv.remove(0);

        Allure.step("Verify: CSV headers match the UI headers");
        assertThat(transactionsPage.getTable().getColumnHeaders())
                .hasText(Stream.concat(csvHeader.stream(), Stream.of("Actions")).toArray(String[]::new));

        Allure.step("Verify the row count between UI and CSV");
        assertEquals(uiRows.size(), rowsFromCsv.size());

        IntStream.range(0, uiRows.size()).forEach(i -> {
            List<String> uiRow = uiRows.get(i);
            List<String> csvRow = rowsFromCsv.get(i);

            IntStream.range(0, uiRow.size()).forEach(j -> {
                String uiCell = uiRow.get(j);
                String csvCell = csvRow.get(j);

                Allure.step("Verify: cell values match between UI and CSV", () ->
                        assertEquals(uiCell, csvCell, String.format("Mismatch at row %d, column %d", i + 1, j + 1)));
            });
        });
    }

    @Test
    @TmsLink("957")
    @Epic("Transactions")
    @Feature("Export table data")
    @Description("The transaction table data on the UI matches the exported PDF file data.")
    public void testTransactionTableMatchesDownloadedPdf() throws IOException {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(TestUtils.lastBuildDate(getApiRequestContext()))
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        List<List<String>> uiRows = new ArrayList<>();
        do {
            List<Locator> rows = transactionsPage.getTable().getRows().all();
            for (Locator row : rows) {
                uiRows.add(transactionsPage.getTable().getRowData(row));
            }
        } while (transactionsPage.getTable().goToNextPage());

        Download download = getPage().waitForDownload(
                new Page.WaitForDownloadOptions().setTimeout(ProjectProperties.getDefaultTimeout() * 6),
                () -> transactionsPage.clickExportTableDataToFileButton().selectPdf());

        Path targetPath = Paths.get("downloads", "transactions-export.pdf");
        Files.createDirectories(targetPath.getParent());
        download.saveAs(targetPath);

        String pdfText;
        try (PDDocument document = Loader.loadPDF(targetPath.toFile())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfText = pdfStripper.getText(document);
        }

        List<String> pdfRows = transactionsPage.readPdf(pdfText).stream()
                .map(t -> String.join(" | ",
                        t.createdOn(),
                        t.transactionId(),
                        t.externalTransactionId(),
                        String.format("%.2f", t.amount()),
                        t.currency().toString(),
                        t.paymentDetails().cardType().toString(),
                        t.status().toString()
                ))
                .collect(Collectors.toList());

        List<String> uiFormattedRows = uiRows.stream()
                .map(row -> String.join(" | ", row))
                .toList();

        Allure.step("Verify: cell values match between UI and PDF");
        assertEquals(uiFormattedRows, pdfRows);
    }

    @Test
    @TmsLink("978")
    @Epic("Transactions")
    @Feature("Settings")
    @Description("Verify that changing the order of visible columns in Settings affects the table.")
    public void testMoveVisibleColumns() {
        final String creationDate = SETTINGS_COLUMNS[0];
        final String amount = SETTINGS_COLUMNS[4];
        final String currency = SETTINGS_COLUMNS[5];
        final String status = SETTINGS_COLUMNS[7];

        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .clickTransactionsLink()
                .clickSettingsButton()
                .uncheckAllCheckboxInSettings()
                .checkVisibleColumn(creationDate)
                .checkVisibleColumn(amount)
                .checkVisibleColumn(currency)
                .checkVisibleColumn(status)
                .dragArrowsToFirstPosition(status)  // status...
                .dragArrowsToFirstPosition(currency) // currency status...
                .dragArrowsToFirstPosition(amount) // amount currency status...
                .dragArrowsToFirstPosition(creationDate) // creationDate amount currency status...
                .dragArrowsToLastPosition(currency) // creationDate amount status currency...
                .dragArrows(creationDate, status) // amount status creationDate currency...
                .dragArrows(amount, creationDate) // status creationDate amount currency...
                .clickRefreshDataButton();

        Allure.step("Verify: Selected column headers are displayed in the correct order in the transactions table.");
        assertThat(transactionsPage.getTable().getColumnHeaders())
                .hasText(new String[]{status, creationDate, amount, currency, "Actions"});
    }
}
