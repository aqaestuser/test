package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BasePageWithHeader;

public class TransactionsPage extends BasePageWithHeader {

    private final Locator currencySelector = button("ALL Currency");
    private final Locator applyDataIcon = locator("button:nth-child(9)");
    private final Locator currencyColumnHeader = columnHeader("Currency");
    private final Locator rowsPerPageButton = button("Rows Per Page");
    private final Locator rowsPerPageOptions = dialog();
    private final Locator nextPageButton = button("next page button");
    private final Locator paginationItemTwoActiveButton = button("pagination item 2 active");

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
    public TransactionsPage clickApplyDataIcon() {
        applyDataIcon.click();

        return this;
    }

    public Locator getCurrencyColumnHeader() {
        return currencyColumnHeader;
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
}
