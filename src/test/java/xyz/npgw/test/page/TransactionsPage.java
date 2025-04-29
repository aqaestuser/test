package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.AccessLevel;
import lombok.Getter;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.util.ResponseUtils;
import xyz.npgw.test.page.common.DateRangePickerTrait;
import xyz.npgw.test.page.common.HeaderPage;
import xyz.npgw.test.page.common.TableTrait;

import java.util.List;

@Getter
public class TransactionsPage extends HeaderPage implements TableTrait, DateRangePickerTrait<TransactionsPage> {

    private final Locator rowsPerPageButton = buttonByName("Rows Per Page");
    private final Locator rowsPerPageOptions = dialog();
    @Getter(AccessLevel.NONE)
    private final Locator nextPageButton = buttonByName("next page button");
    private final Locator paginationItemTwoActiveButton = buttonByName("pagination item 2 active");
    private final Locator businessUnitSelector = textExact("Business unit").locator("../../..");
    private final Locator currencySelector = labelExact("Currency");
    private final Locator paymentMethodSelector = labelExact("Payment method");
    private final Locator statusSelector = labelExact("Status");
    private final Locator amountButton = buttonByName("Amount");
    private final Locator resetFilterButton = getByTestId("ResetFilterButtonTransactionsPage");
    private final Locator refreshDataButton = locator("[data-icon='arrows-rotate']");
    private final Locator settingsButton = getByTestId("SettingsButtonTransactionsPage");
    private final Locator downloadButton = getByTestId("ExportToFileuttonTransactionsPage");
    private final Locator statusSelectorOptions = listboxByRole().locator(optionByRole());
    private final Locator activeOption = listboxByRole().locator("[aria-selected='true']");
    private final Locator amountFromField = labelExact("From").locator("..");
    private final Locator amountToField = labelExact("To").locator("..");
    private final Locator clearAmountFromButton = amountFromField.locator("//button[@aria-label='clear input']");
    private final Locator clearAmountToButton = amountToField.locator("//button[@aria-label='clear input']");
    private final Locator amountFromInputField = amountFromField.locator("//input[@type='text']");
    private final Locator amountToInputField = amountToField.locator("//input[@type='text']");
    private final Locator amountFromIncreaseArrow = amountFromField.locator("//button[@aria-label='Increase From']");
    private final Locator amountFromDecreaseArrow = amountFromField.locator("//button[@aria-label='Decrease From']");
    private final Locator amountToIncreaseArrow = amountToField.locator("//button[@aria-label='Increase To']");
    private final Locator amountToDecreaseArrow = amountToField.locator("//button[@aria-label='Decrease To']");
    private final Locator amountApplyButton = buttonByName("Apply");
    private final Locator amountClearButton = textExact("Clear");
    private final Locator amountAppliedClearButton = labelExact("close chip");
    private final Locator amountErrorMessage = locator("[data-slot='error-message']");
    private final Locator paymentMethodOptions = locator("ul[data-slot='listbox']").getByRole(AriaRole.OPTION);
    private final Locator settingsVisibleColumns = getPage().getByRole(AriaRole.CHECKBOX);
    private final Locator amountEditButton = locator("svg[data-icon='pencil']");

    public Locator amountApplied(String amount) {
        return textExact(amount);
    }

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
        optionByName(value).waitFor();
        ResponseUtils.clickAndWaitForResponse(getPage(), optionByName(value), Constants.TRANSACTION_HISTORY_ENDPOINT);

        return this;
    }

    @Step("Click 'Refresh Data' button")
    public TransactionsPage clickRefreshDataButton() {
        refreshDataButton.click();

        return this;
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

    @Step("Click 'Amount' button")
    public TransactionsPage clickAmountButton() {
        amountButton.click();

        return this;
    }

    @Step("Fill 'From' amount value")
    public TransactionsPage fillAmountFromField(String value) {
        amountFromInputField.click();
        amountFromInputField.clear();
        amountFromInputField.fill(value);
        amountFromField.press("Enter");

        return this;
    }

    @Step("Fill 'To' amount value")
    public TransactionsPage fillAmountToField(String value) {
        amountToInputField.click();
        amountToInputField.clear();
        amountToInputField.fill(value);
        amountToField.press("Enter");

        return this;
    }

    @Step("Clear 'From' amount input field ")
    public TransactionsPage clickClearAmountFromButton() {
        clearAmountFromButton.click();

        return this;
    }

    @Step("Clear 'To' amount input field ")
    public TransactionsPage clickClearAmountToButton() {
        clearAmountToButton.click();

        return this;
    }

    @Step("Click 'From' amount increase arrow ")
    public TransactionsPage clickAmountFromIncreaseArrow() {
        amountFromIncreaseArrow.click();

        return this;
    }

    @Step("Click 'From' amount decrease arrow ")
    public TransactionsPage clickAmountFromDecreaseArrow() {
        amountFromDecreaseArrow.click();

        return this;
    }

    @Step("Click 'To' amount increase arrow ")
    public TransactionsPage clickAmountToIncreaseArrow() {
        amountToIncreaseArrow.click();

        return this;
    }

    @Step("Click 'To' amount decrease arrow ")
    public TransactionsPage clickAmountToDecreaseArrow() {
        amountToDecreaseArrow.click();

        return this;
    }

    @Step("Click 'Apply' amount button")
    public TransactionsPage clickAmountApplyButton() {
        ResponseUtils.clickAndWaitForResponse(getPage(), amountApplyButton, Constants.TRANSACTION_HISTORY_ENDPOINT);

        return this;
    }

    @Step("Click 'Clear' amount button")
    public TransactionsPage clickAmountClearButton() {
        amountClearButton.click();

        return this;
    }

    @Step("Click clear applied amount button")
    public TransactionsPage clickAmountAppliedClearButton() {
        amountAppliedClearButton.click();

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

    @Step("Click Payment Method Selector")
    public TransactionsPage clickPaymentMethodSelector() {
        paymentMethodSelector.click();

        return this;
    }

    public List<String> getPaymentMethodOptions() {
        return paymentMethodOptions.all().stream().map(Locator::innerText).toList();
    }

    @Step("Click on the Settings button")
    public TransactionsPage clickSettingsButton() {
        settingsButton.click();
        textExact("Visible columns").waitFor();

        return this;
    }

    public List<String> getVisibleColumnsLabels() {

        return settingsVisibleColumns
                .all()
                .stream()
                .map(l -> l.getAttribute("aria-label"))
                .toList();
    }

    private void uncheckIfSelected(Locator checkbox) {
        if ((boolean) checkbox.evaluate("el => el.checked")) {
            checkbox.dispatchEvent("click");
        }
    }

    private void checkIfNotSelected(Locator checkbox) {
        if (!(boolean) checkbox.evaluate("el => el.checked")) {
            checkbox.dispatchEvent("click");
        }
    }


    @Step("Uncheck all 'Visible columns' in Settings")
    public TransactionsPage uncheckAllCheckboxInSettings() {
        settingsVisibleColumns
                .all()
                .forEach(this::uncheckIfSelected);

        return this;
    }

    @Step("Uncheck Visible column '{name}' in Settings")
    public TransactionsPage uncheckVisibleColumn(String name) {
        settingsVisibleColumns
                .all()
                .stream()
                .filter(l -> name.equals(l.getAttribute("aria-label")))
                .findFirst()
                .ifPresent(this::uncheckIfSelected);

        return this;
    }


    @Step("Check all 'Visible columns' in Settings")
    public TransactionsPage checkAllCheckboxInSettings() {
        settingsVisibleColumns
                .all()
                .forEach(this::checkIfNotSelected);

        return this;
    }

    @Step("Check visible column '{name}' in Settings")
    public TransactionsPage checkVisibleColumn(String name) {
        settingsVisibleColumns
                .all()
                .stream()
                .filter(l -> name.equals(l.getAttribute("aria-label")))
                .findFirst()
                .ifPresent(this::checkIfNotSelected);

        return this;
    }

    @Step("Click amount 'Edit' button")
    public TransactionsPage clickAmountEditButton() {
        amountEditButton.click();

        return this;
    }
}
