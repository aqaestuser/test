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
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.entity.CardType;
import xyz.npgw.test.common.entity.Currency;
import xyz.npgw.test.common.entity.Status;
import xyz.npgw.test.common.entity.Transaction;
import xyz.npgw.test.common.entity.Type;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.dashboard.SuperDashboardPage;
import xyz.npgw.test.page.dialog.transactions.RefundTransactionDialog;
import xyz.npgw.test.page.transactions.SuperTransactionsPage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static xyz.npgw.test.common.Constants.BUSINESS_UNIT_FOR_TEST_RUN;
import static xyz.npgw.test.common.Constants.CARD_TYPES;
import static xyz.npgw.test.common.Constants.COMPANY_NAME_FOR_TEST_RUN;
import static xyz.npgw.test.common.Constants.CURRENCY_OPTIONS;
import static xyz.npgw.test.common.Constants.ONE_DATE_FOR_TABLE;
import static xyz.npgw.test.common.Constants.TRANSACTION_STATUSES;

public class TransactionsTableTest extends BaseTest {

    private static final String MERCHANT_TITLE = "%s test transaction table merchant".formatted(RUN_ID);
    private static final String[] COLUMNS_HEADERS = {
            "Creation Date (GMT)",
            "Type",
            "Business unit ID",
            "NPGW reference",
            "Business unit reference",
            "Amount",
            "Currency",
            "Card type",
            "Status",
            "Actions"};
    private static final String[] SETTINGS_COLUMNS = Arrays.copyOf(COLUMNS_HEADERS, COLUMNS_HEADERS.length - 1);
    private static final String EXPORT_SUCCESS_MESSAGE = "SUCCESSExporting the data";

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

        List<String> amountValues = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(ONE_DATE_FOR_TABLE)
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

    @Test
    @TmsLink("673")
    @Epic("Transactions")
    @Feature("Filter")
    @Description("Filtering transactions by Card type displays only matching entries in the table.")
    public void testFilterByCardType() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        Allure.step("Verify: transaction page table has data");
        assertThat(transactionsPage.getTable().getNoRowsToDisplayMessage()).isHidden();

        for (String cardType : Arrays.copyOfRange(CARD_TYPES, 1, CARD_TYPES.length)) {
            List<String> cardTypeList = transactionsPage
                    .selectCardType(cardType)
                    .getTable().getCardTypeColumnValuesAllPages();

            Allure.step("Verify: all entries in the 'Card type' column match the selected filter");
            assertTrue(cardTypeList.stream().allMatch(value -> value.equals(cardType)));
        }
    }

    @Test
    @TmsLink("677")
    @Epic("Transactions")
    @Feature("Filter")
    @Description("Filtering transactions by date range")
    public void testFilteringTransactionsByDateRange() {
        String startDate = "01/06/2025";
        String endDate = "05/06/2025";

        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .getSelectDateRange().setDateRangeFields(startDate + " - " + endDate)
                .clickRefreshDataButton();

        Allure.step("Verify: Transactions can be filtered by date range");
        assertTrue(transactionsPage.getTable().isBetween(startDate, endDate));
    }

    @Test
    @TmsLink("679")
    @Epic("Transactions")
    @Feature("Filter")
    @Description("Compare number of transactions with selected statuses in the table before and after filter")
    public void testFilterByStatus() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(ONE_DATE_FOR_TABLE)
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        Allure.step("Verify: transaction page table has data");
        assertThat(transactionsPage.getTable().getNoRowsToDisplayMessage()).isHidden();

        List<String> defaultStatusList = transactionsPage
                .getTable().getColumnValuesFromAllPages("Status");

        for (String status : Arrays.copyOfRange(TRANSACTION_STATUSES, 1, TRANSACTION_STATUSES.length)) {
            List<String> statusListAfterFilter = transactionsPage
                    .getSelectStatus().select(status)
                    .getTable().getColumnValuesFromAllPages("Status");

            Allure.step("Verify: All transactions with selected statuses are shown after filter.");
            assertEquals(defaultStatusList.stream().filter(s -> s.equals(status)).toList(), statusListAfterFilter);

            Allure.step("Verify: Only transactions with selected statuses are shown after filter.");
            assertTrue(statusListAfterFilter.stream().allMatch(s -> s.equals(status)));
        }
    }

    @Test
    @TmsLink("319")
    @Epic("Transactions")
    @Feature("Filter")
    @Description("Filtering transactions by Currency")
    public void testFilterTransactionsByCurrency() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(ONE_DATE_FOR_TABLE)
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        Allure.step("Verify: transaction page table has data");
        assertThat(transactionsPage.getTable().getNoRowsToDisplayMessage()).isHidden();

        for (String currency : Arrays.copyOfRange(CURRENCY_OPTIONS, 1, CURRENCY_OPTIONS.length)) {
            List<String> currencyValues = transactionsPage
                    .getSelectCurrency().select(currency)
                    .getTable().getColumnValuesFromAllPages("Currency");

            Allure.step("Verify: All values in the Currency column match the selected currency");
            assertTrue(currencyValues.stream().allMatch(value -> value.equals(currency)));
        }
    }

    @Test
    @TmsLink("682")
    @Epic("Transactions")
    @Feature("Filter")
    @Description("Verify that transactions are present in the table when a currency filter is applied on the last page")
    public void testTableDisplayWhenCurrencyFilterAppliedWhileOnLastPage() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        Allure.step("Verify: Transactions are present in the table");
        assertThat(transactionsPage.getTable().getRows()).not().hasCount(0);

        transactionsPage.getTable().goToLastPage();

        transactionsPage.getSelectCurrency().select("EUR");

        Allure.step("Verify: Transactions are still present then filter is applied on the last page");
        assertThat(transactionsPage.getTable().getRows()).not().hasCount(0);
    }

    @Test
    @TmsLink("559")
    @Epic("Transactions")
    @Feature("Table sorting")
    @Description("'Creation Date' column sorts descending by default and ascending on click.")
    public void testSortByCreationDate() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(ONE_DATE_FOR_TABLE)
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        List<LocalDateTime> actualDates = transactionsPage
                .getTable().getAllCreationDates();

        Allure.step("Verify: transactions are sorted by creation date in descending order by default");
        assertEquals(actualDates, actualDates.stream().sorted(Comparator.reverseOrder()).toList());

        transactionsPage
                .getTable().clickColumnHeader("Creation Date (GMT)");

        Allure.step("Verify: transactions are sorted by creation date in ascending after clicking the sort icon");
        assertEquals(transactionsPage.getTable().getAllCreationDates(), actualDates.stream().sorted().toList());
    }

    @Test
    @TmsLink("659")
    @Epic("Transactions")
    @Feature("Table sorting")
    @Description("'Amount' column sorts ascending on first click and descending on second click.")
    public void testSortByAmount() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(ONE_DATE_FOR_TABLE)
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .getTable().clickColumnHeader("Amount");

        List<Double> actualAmount = transactionsPage
                .getTable().getAllAmounts();

        Allure.step("Verify: transactions are sorted by amount in ascending order after first click");
        assertEquals(actualAmount, actualAmount.stream().sorted().toList());

        transactionsPage
                .getTable().clickColumnHeader("Amount");

        Allure.step("Verify: transactions are sorted by amount in descending order after second click");
        assertEquals(transactionsPage.getTable().getAllAmounts(),
                actualAmount.stream().sorted(Comparator.reverseOrder()).toList());
    }

    @Test
    @TmsLink("127")
    @TmsLink("106")
    @Epic("Transactions")
    @Feature("Pagination")
    @Description("Displaying 'Rows per page' options when clicking on the RowsPerPage selector")
    public void testCountOptionsSelectorRows() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(ONE_DATE_FOR_TABLE)
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        Allure.step("Verify: default row count - 25");
        assertThat(transactionsPage.getTable().getRowsPerPage()).containsText("25");

        transactionsPage
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
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(ONE_DATE_FOR_TABLE)
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
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickSettingsButton()
                .checkAllCheckboxInSettings()
                .clickSettingsButton();

        Allure.step("Verify: All column headers except 'Actions' are displayed in the Settings");
        assertThat(transactionsPage.getColumns()).hasText(SETTINGS_COLUMNS);

        Allure.step("Verify: All column headers are displayed in the transactions table");
        assertThat(transactionsPage.getTable().getColumnHeaders()).hasText(COLUMNS_HEADERS);

        transactionsPage
                .clickSettingsButton()
                .uncheckAllCheckboxInSettings()
                .clickSettingsButton();

        Allure.step("Verify: Only 'Actions' column is displayed in the transactions table header");
        assertThat(transactionsPage.getTable().getColumnHeaders()).hasText("Actions");
    }

    @Test
    @TmsLink("358")
    @Epic("Transactions")
    @Feature("Settings")
    @Description("Check/Uncheck Visible columns in the Settings and verify table column headers")
    public void testCheckUncheckOneVisibleColumn() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickSettingsButton()
                .checkAllCheckboxInSettings();

        for (String item : SETTINGS_COLUMNS) {
            transactionsPage
                    .uncheckVisibleColumn(item)
                    .clickSettingsButton();

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
                    .clickSettingsButton();

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
                        Type.SALE,
                        "12345", "", 100,
                        Currency.USD, CardType.VISA, Status.FAILED));
                route.fulfill(new Route.FulfillOptions().setBody(new Gson().toJson(transactionList)));
                return;
            }
            route.fallback();
        });

        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
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
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(ONE_DATE_FOR_TABLE)
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
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(ONE_DATE_FOR_TABLE)
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        do {
            List<Locator> currentRows = transactionsPage
                    .getTable().getRows().all();

            for (Locator row : currentRows) {
                Locator refundButton = transactionsPage
                        .getTable().getRefundButton(row);

                if (refundButton.count() > 0 && refundButton.isVisible()) {
                    String amount = transactionsPage
                            .getTable().getCell(row, "Amount").innerText().trim();
                    String currency = transactionsPage
                            .getTable().getCell(row, "Currency").innerText().trim();

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
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(ONE_DATE_FOR_TABLE)
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .getTable().selectRowsPerPageOption("100");

        Download download = getPage().waitForDownload(
                new Page.WaitForDownloadOptions().setTimeout(ProjectProperties.getDefaultTimeout() * 6),
                () -> transactionsPage.clickExportTableDataToFileButton().selectCsv());

        Allure.step("Verify: success alert is shown after exporting");
        assertThat(transactionsPage.getAlert().getMessage()).hasText(EXPORT_SUCCESS_MESSAGE);

        Path targetPath = Paths.get("downloads", "transactions-export.csv");
        Files.createDirectories(targetPath.getParent());
        download.saveAs(targetPath);

        List<Transaction> uiTransactionList = transactionsPage
                .getTable().getAllTransactions();
        List<Transaction> csvTransactionList = transactionsPage
                .parseTransactionsFromCsvFile(targetPath);

        Allure.step("Verify: row count between UI and CSV");
        assertEquals(uiTransactionList.size(), csvTransactionList.size());

        Allure.step("Verify: the UI transaction list matches the CSV transaction list");
        assertEquals(uiTransactionList, csvTransactionList);
    }

    @Test
    @TmsLink("957")
    @Epic("Transactions")
    @Feature("Export table data")
    @Description("The transaction table data on the UI matches the exported PDF file data.")
    public void testTransactionTableMatchesDownloadedPdf() throws IOException {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(ONE_DATE_FOR_TABLE)
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .getTable().selectRowsPerPageOption("100");

        Download download = getPage().waitForDownload(
                new Page.WaitForDownloadOptions().setTimeout(ProjectProperties.getDefaultTimeout() * 6),
                () -> transactionsPage.clickExportTableDataToFileButton().selectPdf());

//          TODO if export takes longer than 3 seconds alert is closing immediately
//        Allure.step("Verify: success alert is shown after exporting");
//        assertThat(transactionsPage.getAlert().getMessage())
//                .hasText(EXPORT_SUCCESS_MESSAGE);

        Path targetPath = Paths.get("downloads", "transactions-export.pdf");
        Files.createDirectories(targetPath.getParent());
        download.saveAs(targetPath);

        String pdfText;
        try (PDDocument document = Loader.loadPDF(targetPath.toFile())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfText = pdfStripper.getText(document);
        }

        List<Transaction> uiTransactionList = transactionsPage
                .getTable().getAllTransactions();
        List<Transaction> pdfTransactionList = transactionsPage
                .parseTransactionsFromPdfText(pdfText);

        Allure.step("Verify: row count between UI and Excel");
        assertEquals(uiTransactionList.size(), pdfTransactionList.size());

        Allure.step("Verify: the UI transaction list matches the PDF transaction list");
        assertEquals(uiTransactionList, pdfTransactionList);
    }

    @Test
    @TmsLink("1011")
    @Epic("Transactions")
    @Feature("Export table data")
    @Description("The transaction table data on the UI matches the exported Excel file data.")
    public void testTransactionTableMatchesDownloadedExcel() throws IOException {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(ONE_DATE_FOR_TABLE)
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .getTable().selectRowsPerPageOption("100");

        Download download = getPage().waitForDownload(
                new Page.WaitForDownloadOptions().setTimeout(ProjectProperties.getDefaultTimeout() * 6),
                () -> transactionsPage.clickExportTableDataToFileButton().selectExcel());

        Allure.step("Verify: success alert is shown after exporting");
        assertThat(transactionsPage.getAlert().getMessage())
                .hasText(EXPORT_SUCCESS_MESSAGE);

        Path targetPath = Paths.get("downloads", "transactions-export.xlsx");
        Files.createDirectories(targetPath.getParent());
        download.saveAs(targetPath);

        List<Transaction> uiTransactionList = transactionsPage
                .getTable().getAllTransactions();
        List<Transaction> excelTransactionList = transactionsPage
                .parseTransactionsFromExcelFile(targetPath);

        Allure.step("Verify: row count between UI and Excel");
        assertEquals(uiTransactionList.size(), excelTransactionList.size());

        Allure.step("Verify: the UI transaction list matches the Excel transaction list");
        assertEquals(uiTransactionList, excelTransactionList);
    }

    @Ignore("Locator expected to have text: [Status, Creation Date (GMT), Amount, Currency, Actions]\n"
            + "\tReceived: [Currency, Status, Creation Date (GMT), Amount, Actions]")
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

        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
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
                .pressEscapeKey();

        Allure.step("Verify: Selected column headers are displayed in the correct order in the transactions table.");
        assertThat(transactionsPage.getTable().getColumnHeaders())
                .hasText(new String[]{status, creationDate, amount, currency, "Actions"});
    }
}
