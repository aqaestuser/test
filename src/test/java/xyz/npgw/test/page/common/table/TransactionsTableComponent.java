package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.dialog.transactions.RefundTransactionDialog;
import xyz.npgw.test.page.dialog.transactions.TransactionDetailsDialog;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TransactionsTableComponent extends BaseTableComponent<TransactionsPage> {

    private final String refundTransactionButtonSelector;
    private final Locator cardTypeImage;
    private final Locator npgwReference;

    public TransactionsTableComponent(Page page) {
        super(page);

        this.refundTransactionButtonSelector = "[data-testid='RefundTransactionButton']";
        this.cardTypeImage = locator("img[alt='logo']");
        this.npgwReference = getRows().getByRole(AriaRole.LINK);
    }

    @Override
    protected TransactionsPage getCurrentPage() {
        return new TransactionsPage(getPage());
    }

    @Step("Click on the first transaction from the table")
    public TransactionDetailsDialog clickOnFirstTransaction() {
        getFirstRowCell("NPGW reference").click();

        return new TransactionDetailsDialog(getPage());
    }

    @Step("Click on the transaction NPGW reference")
    public TransactionDetailsDialog clickOnTransaction(int index) {
        npgwReference.nth(index).click();

        return new TransactionDetailsDialog(getPage());
    }

    public String getFirstRowReference() {
        return getFirstRowCell("NPGW reference").innerText();
    }

    public List<LocalDateTime> getAllCreationDates() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return getColumnValuesFromAllPages("Creation Date (GMT)", s -> LocalDateTime.parse(s, formatter));
    }

    public List<Double> getAllAmounts() {
        return getColumnValuesFromAllPages("Amount", s -> Double.parseDouble(s.replaceAll("[^\\d.]", "")));
    }

    public boolean isBetween(String dateFrom, String dateTo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
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

    public List<String> getRowData(Locator row) {
        List<String> rowData = new ArrayList<>();
        rowData.add(getCell(row, "Creation Date (GMT)").innerText().trim());
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
}
