package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.dialog.TransactionDetailsDialog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TransactionsTableComponent extends BaseTableComponent<TransactionsPage> {

    private final Locator refundTransactionButton = getByTestId("RefundTransactionButton");

    public TransactionsTableComponent(Page page) {
        super(page);
    }

    private final Locator npgwReference = getRows().getByRole(AriaRole.LINK);

    @Override
    protected TransactionsPage getCurrentPage() {
        return new TransactionsPage(getPage());
    }

    @Step("Click on the first transaction from the table")
    public TransactionDetailsDialog clickOnFirstTransaction() {
        getFirstRowCell("NPGW Reference").click();

        return new TransactionDetailsDialog(getPage());
    }

    @Step("Click on the transaction NPGW Reference")
    public TransactionDetailsDialog clickOnTransaction(int index) {
        npgwReference.nth(index).click();

        return new TransactionDetailsDialog(getPage());
    }

    public int getNumberOfTransactionOnCurrentPage() {
        return npgwReference.count();
    }

    public List<LocalDateTime> getAllCreationDates() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

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

        return locator("[data-slot='content']").textContent();
    }

    public List<String> getAllTransactionsStatusList() {
        getByRole(AriaRole.BUTTON, "next page button").waitFor();

        return getColumnValuesFromAllPages("Status", Function.identity());
    }

    public List<Boolean> getRefundButtonVisibilityFromAllPages() {
        selectRowsPerPageOption("100");
        goToFirstPageIfNeeded();

        List<Boolean> results = new ArrayList<>();
        do {
            List<Locator> cells = getColumnCells("Actions");
            for (Locator cell : cells) {
                boolean isVisible = cell.locator(refundTransactionButton).isVisible();
                results.add(isVisible);
            }
        } while (goToNextPage());

        return results;
    }

}
