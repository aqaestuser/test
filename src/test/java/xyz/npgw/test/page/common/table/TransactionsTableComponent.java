package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.dialog.TransactionDetailsDialog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionsTableComponent extends BaseTableComponent<TransactionsPage> {
    private final Locator cardLogo = locator("td").getByRole(AriaRole.IMG);
    @Getter
    private final Locator cardTypeModal = locator("[data-slot='content']");

    public TransactionsTableComponent(Page page) {
        super(page);
    }

    @Override
    protected TransactionsPage getCurrentPage() {
        return new TransactionsPage(getPage());
    }

    @Step("Click on transaction in column 'NPGW reference'")
    public TransactionDetailsDialog clickOnFirstTransaction() {
        getFirstRowCell("NPGW reference").click();

        return new TransactionDetailsDialog(getPage());
    }

    public List<LocalDateTime> getAllCreationDates() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        return getColumnValuesFromAllPages("Creation Date", s -> LocalDateTime.parse(s, formatter));
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

    private Locator getCardLogo() {
        return cardLogo;
    }

    public void hoverCardLogo() {
        getCardLogo().nth(0).hover();
    }
}
