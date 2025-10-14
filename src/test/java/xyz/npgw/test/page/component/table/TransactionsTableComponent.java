package xyz.npgw.test.page.component.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.common.entity.Transaction;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.dialog.transactions.CancelTransactionDialog;
import xyz.npgw.test.page.dialog.transactions.RefundTransactionDialog;
import xyz.npgw.test.page.dialog.transactions.TransactionDetailsDialog;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

public class TransactionsTableComponent<CurrentPageT> extends BaseTableComponent<CurrentPageT> {

    private final String refundTransactionButtonSelector;
    private final Locator cardTypeImage;

    public TransactionsTableComponent(Page page, CurrentPageT currentPage) {
        super(page, currentPage);

        this.refundTransactionButtonSelector = "[data-testid='RefundTransactionButton']";
        this.cardTypeImage = locator("img[alt='logo']");
    }

//    @Override
//    protected CurrentPageT getCurrentPage() {
//        return currentPage;
//    }

    @Step("Click on the first transaction from the table")
    public TransactionDetailsDialog clickOnFirstTransaction() {
        getFirstRowCell("NPGW reference").click();

        return new TransactionDetailsDialog(getPage());
    }

    @Step("Click on the transaction NPGW reference")
    public TransactionDetailsDialog clickOnTransaction(int index) {
        getRows().getByRole(AriaRole.LINK).nth(index).click();

        return new TransactionDetailsDialog(getPage());
    }

    public String getFirstRowReference() {
        return getFirstRowCell("NPGW reference").innerText();
    }

    public List<LocalDateTime> getAllCreationDates() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return getColumnValuesFromAllPages("Creation Date (GMT)", s -> LocalDateTime.parse(s, formatter));
    }

    public List<Double> getAllAmounts() {
        return getColumnValuesFromAllPages("Amount", s -> Double.parseDouble(s.replaceAll("[^\\d.]", "")));
    }

    public boolean isBetween(String dateFrom, String dateTo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime dateTimeFrom = LocalDate.parse(dateFrom, formatter).atStartOfDay();
        LocalDateTime dateTimeTo = LocalDate.parse(dateTo, formatter).plusDays(1).atStartOfDay();

        return getAllCreationDates()
                .stream()
                .allMatch(date -> date.isAfter(dateTimeFrom) && date.isBefore(dateTimeTo));
    }

    public String getFirstRowCardType() {
        getFirstRowCell("Card type").getByRole(AriaRole.IMG).hover();

        return getRoot().locator("[data-slot='content']").textContent();
    }

    public List<String> getAllTransactionsStatusList() {
        getPaginationItems().last().waitFor();

        return getColumnValuesFromAllPages("Status");
    }

    public List<Boolean> getRefundButtonVisibilityFromAllPages() {
        selectRowsPerPageOption("100");
        goToFirstPageIfNeeded();
        List<Boolean> results = new ArrayList<>();
        do {
            List<Locator> cells = getColumnCells("Actions");
            for (Locator cell : cells) {
                cell.waitFor();
                boolean isVisible = cell.locator(refundTransactionButtonSelector).isVisible();
                results.add(isVisible);
            }
        } while (goToNextPage());

        return results;
    }

    public List<String> getCardTypeColumnValues() {
        Locator imgs = getRows()
                .locator(columnSelector("Card type"))
                .locator(cardTypeImage);

        return IntStream.range(0, imgs.count())
                .mapToObj(i -> detectCardName(imgs.nth(i).getAttribute("src")))
                .toList();
    }

    public String getCardTypeValue(Locator row) {
        return detectCardName(row.locator(cardTypeImage).getAttribute("src"));
    }

    private String detectCardName(String src) {
        String decodedSrc = URLDecoder.decode(src, StandardCharsets.UTF_8);

        return decodedSrc.contains("fill:#224DBA") ? "VISA"
                : decodedSrc.contains("fill:#EB001B") ? "MASTERCARD"
                : "UNKNOWN";
    }

    public List<String> getCardTypeColumnValuesAllPages() {
        return collectAllPages(this::getCardTypeColumnValues);
    }

    @Step("Click 'Refund transaction'")
    public RefundTransactionDialog clickRefundTransaction(Locator row) {
        row.locator(refundTransactionButtonSelector).click();

        return new RefundTransactionDialog(getPage());
    }

    @Step("Click Refund button for transaction '{transactionId}'")
    public RefundTransactionDialog clickRefundTransaction(String transactionId) {
        getRawByTransactionId(transactionId).getByTestId("RefundTransactionButton").click();

        return new RefundTransactionDialog(getPage());
    }

    private List<String> getRowData(Locator row) {
        List<String> rowData = new ArrayList<>();
        rowData.add(getCell(row, "Creation Date (GMT)").innerText().trim());
        rowData.add(getCell(row, "Type").innerText().trim());
        rowData.add(getCell(row, "NPGW reference").innerText().trim());
        rowData.add(getCell(row, "Business unit reference").innerText().trim());
        rowData.add(getCell(row, "Amount").innerText().trim());
        rowData.add(getCell(row, "Currency").innerText().trim());
        rowData.add(getCardTypeValue(row));
        rowData.add(getCell(row, "Status").innerText().trim());

        return rowData;
    }

    public Locator getRefundButton(Locator row) {
        return row.locator(refundTransactionButtonSelector);
    }

    private List<List<String>> getAllTableRows() {
        return collectAllPages(() -> {
            List<List<String>> rowsData = new ArrayList<>();
            List<Locator> rows = getRows().all();
            for (Locator row : rows) {
                rowsData.add(getRowData(row));
            }
            return rowsData;
        });
    }

    @Step("Get all transactions displayed in the UI table")
    public List<Transaction> getAllTransactions() {
        return getAllTableRows().stream()
                .map(TestUtils::mapToTransaction)
                .toList();
    }

    @Step("Click 'Cancel' button for transaction {transactionId}")
    public CancelTransactionDialog clickCancelTransactionButton(String transactionId) {
        getRawByTransactionId(transactionId).getByTestId("CancelTransactionButton").click();

        return new CancelTransactionDialog(getPage());
    }

    public Locator getRawByTransactionId(String transactionId) {
        do {
            if (locator("tr[data-key]").all().stream()
                    .anyMatch(x -> {
                        String key = x.getAttribute("data-key");
                        return key != null && key.contains(transactionId);
                    })) {
                return locator("tr[data-key*='%s']".formatted(transactionId));
            }
        } while (goToNextPage());

        throw new NoSuchElementException(
                "Row with data-key containing '" + transactionId + "' not found on any page."
        );
    }

    @Step("Get cell for column '{columnHeader}' in transaction {transactionId}")
    public Locator getCellByTransactionId(String transactionId, String columnHeader) {
        return getCell(getRawByTransactionId(transactionId), columnHeader);
    }

    @Step("Click 'NPGW reference' for transaction {transactionId} to open details")
    public TransactionDetailsDialog clickTransactionId(String transactionId) {
        getRawByTransactionId(transactionId).locator(columnSelector("NPGW reference")).click();

        return new TransactionDetailsDialog(getPage());
    }

    public String getCardTypeByTransactionId(String transactionId) {
        return getCardTypeValue(getRawByTransactionId(transactionId));
    }

    public Locator getRefundButtonByTransactionId(String transactionId) {
        return getCellByTransactionId(transactionId, "Actions")
                .locator(refundTransactionButtonSelector);
    }
}
