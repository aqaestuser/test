package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.AccessLevel;
import lombok.Getter;
import xyz.npgw.test.page.base.BasePageWithHeaderAndTable;

import java.util.List;

@Getter
public class TransactionsPage extends BasePageWithHeaderAndTable {

    private final Locator rowsPerPageButton = button("Rows Per Page");
    private final Locator rowsPerPageOptions = dialog();
    @Getter(AccessLevel.NONE)
    private final Locator nextPageButton = button("next page button");
    private final Locator paginationItemTwoActiveButton = button("pagination item 2 active");
    private final Locator dateRangePicker = group("DateRange");
    private final Locator businessUnitSelector = textExact("Business unit").locator("../../..");
    private final Locator currencySelector = labelExact("Currency");
    private final Locator paymentMethodSelector = labelExact("Payment method");
    private final Locator statusSelector = labelExact("Status");
    private final Locator amountButton = button("Amount");
    private final Locator resetFilterButton = getByTestId("ResetFilterButtonTransactionsPage");
    private final Locator applyDataButton = getByTestId("ApplyFilterButtonTransactionsPage");
    private final Locator settingsButton = getByTestId("SettingsButtonTransactionsPage");
    private final Locator downloadButton = getByTestId("ExportToFileuttonTransactionsPage");
    private final Locator statusSelectorOptions = listboxByRole().locator(optionByRole());
   // private final Locator allDefaultStatusSelector = optionByName("ALL");
    private final Locator activeOption = listboxByRole().locator("[aria-selected='true']");

    public TransactionsPage(Page page) {
        super(page);
    }

    @Step("Click Currency Selector")
    public TransactionsPage clickCurrencySelector() {
        currencySelector.click();

        return this;
    }

    @Step("Click Options Currency {value}")
    public TransactionsPage clickCurrency(String value) {
        optionLabelByExactText(value).click();

        return this;
    }

    @Step("Click Icon Apply Data")
    public TransactionsPage clickApplyDataButton() {
        applyDataButton.click();

        return this;
    }

    public boolean getTableRow(String value) {
        for (Locator item : getPage().locator("tbody tr").all()) {
            if (!item.locator("td").nth(5).textContent().equals(value)) {
                return false;
            }
        }
        return true;
    }

    @Step("Click Button Rows Per Page")
    public TransactionsPage clickRowsPerPageButton() {
        rowsPerPageButton.click();

        return this;
    }

    @Step("Click next Page Button")
    public TransactionsPage clickNextPageButton() {
        nextPageButton.click();

        return this;
    }

    @Step("Click Status Selector")
    public TransactionsPage clickStatusSelector() {
        statusSelector.click();

        return this;
    }

    public List<String> getStatusSelectorOptions() {
        return statusSelectorOptions.all().stream().map(Locator::innerText).toList();
    }
}
