package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BasePageWithHeaderAndTable;

public class TransactionsPage extends BasePageWithHeaderAndTable {

    private final Locator rowsPerPageButton = button("Rows Per Page");
    private final Locator rowsPerPageOptions = dialog();
    private final Locator nextPageButton = button("next page button");
    private final Locator paginationItemTwoActiveButton = button("pagination item 2 active");
    private final Locator dateRangePicker = group("DateRange");
    private final Locator businessUnitSelector = textExact("Business unit").locator("../../..");
    private final Locator currencySelector = labelExact("Currency");
    private final Locator paymentMethodSelector = labelExact("Payment method");
    private final Locator statusSelector = labelExact("Status");
    private final Locator amountButton = button("Amount");
    private final Locator resetFilterButton = locator("svg[data-icon='xmark']");
    private final Locator applyDataButton = locator("svg[data-icon='filter']");
    private final Locator settingsButton = locator("svg[data-icon='gear']");
    private final Locator downloadButton = locator("svg[data-icon='download']");

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
        optionByExactName(value).click();

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

    public Locator getRowsPerPageButton() {
        return rowsPerPageButton;
    }

    @Step("Click Button Rows Per Page")
    public TransactionsPage clickRowsPerPageButton() {
        rowsPerPageButton.click();

        return this;
    }

    public Locator getRowsPerPageOptions() {
        return rowsPerPageOptions;
    }

    @Step("Click next Page Button")
    public TransactionsPage clickNextPageButton() {
        nextPageButton.click();

        return this;
    }

    public Locator getPaginationItemTwoActiveButton() {
        return paginationItemTwoActiveButton;
    }

    public Locator getDateRangePicker() {
        return dateRangePicker;
    }

    public Locator getBusinessUnitSelector() {
        return businessUnitSelector;
    }

    public Locator getCurrencySelector() {
        return currencySelector;
    }

    public Locator getPaymentMethodSelector() {
        return paymentMethodSelector;
    }

    public Locator getStatusSelector() {
        return statusSelector;
    }

    public Locator getAmountButton() {
        return amountButton;
    }

    public Locator getResetFilterButton() {
        return resetFilterButton;
    }

    public Locator getApplyDataButton() {
        return applyDataButton;
    }

    public Locator getSettingsButton() {
        return settingsButton;
    }

    public Locator getDownloadButton() {
        return downloadButton;
    }
}
