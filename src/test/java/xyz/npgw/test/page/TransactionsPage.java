package xyz.npgw.test.page;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import org.testng.Assert;
import xyz.npgw.test.common.util.ResponseUtils;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.common.trait.SelectBusinessUnitTrait;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;
import xyz.npgw.test.page.common.trait.SelectDateRangeTrait;
import xyz.npgw.test.page.common.trait.SelectStatusTrait;
import xyz.npgw.test.page.common.trait.TransactionsTableTrait;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class TransactionsPage extends HeaderPage<TransactionsPage> implements TransactionsTableTrait,
        SelectDateRangeTrait<TransactionsPage>,
        SelectCompanyTrait<TransactionsPage>,
        SelectBusinessUnitTrait<TransactionsPage>,
        SelectStatusTrait<TransactionsPage> {

    private final Locator businessUnitSelector = getByTextExact("Business unit").locator("../../..");
    private final Locator currencySelector = getByLabelExact("Currency");
    private final Locator cardTypeSelector = getByLabelExact("Card type");
    private final Locator cardTypeValue = getByRole(AriaRole.BUTTON, "Card type");
    private final Locator resetFilterButton = getByTestId("ResetFilterButtonTransactionsPage");
    private final Locator refreshDataButton = locator("[data-icon='arrows-rotate']");
    private final Locator settingsButton = getByTestId("SettingsButtonTransactionsPage");
    private final Locator downloadButton = getByTestId("ExportToFileuttonTransactionsPage");
    private final Locator amountButton = getByRole(AriaRole.BUTTON, "Amount");
    private final Locator amountFromField = getByLabelExact("From").locator("..");
    private final Locator amountToField = getByLabelExact("To").locator("..");
    private final Locator clearAmountFromButton = amountFromField.locator("//button[@aria-label='clear input']");
    private final Locator clearAmountToButton = amountToField.locator("//button[@aria-label='clear input']");
    private final Locator amountFromInputField = amountFromField.locator("//input[@type='text']");
    private final Locator amountToInputField = amountToField.locator("//input[@type='text']");
    private final Locator amountFromIncreaseArrow = amountFromField.locator("//button[@aria-label='Increase From']");
    private final Locator amountFromDecreaseArrow = amountFromField.locator("//button[@aria-label='Decrease From']");
    private final Locator amountToIncreaseArrow = amountToField.locator("//button[@aria-label='Increase To']");
    private final Locator amountToDecreaseArrow = amountToField.locator("//button[@aria-label='Decrease To']");
    private final Locator amountApplyButton = getByRole(AriaRole.BUTTON, "Apply");
    private final Locator amountClearButton = getByTextExact("Clear");
    private final Locator amountAppliedClearButton = getByLabelExact("close chip");
    private final Locator amountErrorMessage = locator("[data-slot='error-message']");
    private final Locator cardTypeOptions = locator("ul[data-slot='listbox']").getByRole(AriaRole.OPTION);
    private final Locator settingsVisibleColumns = getPage().getByRole(AriaRole.CHECKBOX);
    private final Locator amountEditButton = locator("svg[data-icon='pencil']");
    private final Locator downloadCsvOption = getByRole(AriaRole.MENUITEM, "CSV");
    private final Locator downloadExcelOption = getByRole(AriaRole.MENUITEM, "EXCEL");
    private final Locator downloadPdfOption = getByRole(AriaRole.MENUITEM, "PDF");
    private final Locator dialog = locator("[role='dialog']");
    private final Locator dropdownMenuContent = locator("[data-slot='content'][data-open='true']");

    public TransactionsPage(Page page) {
        super(page);
    }

    public Locator amountApplied(String amount) {
        return getByTextExact(amount);
    }

    @Step("Click Currency Selector")
    public TransactionsPage clickCurrencySelector() {
        currencySelector.click();

        return this;
    }

    @Step("Select currency {value} from dropdown menu")
    public TransactionsPage selectCurrency(String value) {
        Locator option = getByRole(AriaRole.OPTION, value);
        option.waitFor();
        option.click();
        dropdownMenuContent.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return this;
    }

    @Step("Click 'Refresh Data' button")
    public TransactionsPage clickRefreshDataButton() {
        refreshDataButton.click();

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
        amountApplyButton.click();
        dropdownMenuContent.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

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

    @Step("Click 'Card type' selector")
    public TransactionsPage clickCardTypeSelector() {
        cardTypeSelector.click();

        return this;
    }

    public List<String> getCardTypeOptions() {
        return cardTypeOptions.all().stream().map(Locator::innerText).toList();
    }

    @Step("Click on the Settings button")
    public TransactionsPage clickSettingsButton() {
        ResponseUtils.clickAndWaitForText(getPage(), settingsButton, "Visible columns");

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

    @Step("Click 'Download' button")
    public TransactionsPage clickDownloadButton() {
        downloadButton.click();

        return this;
    }

    public boolean isFileAvailableAndNotEmpty(String fileType) {
        Download download = getPage().waitForDownload(() -> getByRole(AriaRole.MENUITEM, fileType).click());

        int length = 0;
        try (InputStream inputStream = download.createReadStream()) {
            length = inputStream.readAllBytes().length;
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        return length > 0;
    }

    @Step("Click 'Reset filter' button")
    public TransactionsPage clickResetFilterButton() {
        resetFilterButton.click();
        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));

        return this;
    }

    @Step("Select Card type '{value}' from dropdown menu")
    public TransactionsPage selectCardType(String value) {
        cardTypeSelector.click();
        getByRole(AriaRole.OPTION, value).click();

        return this;
    }

    public String getRequestData() {
        AtomicReference<String> data = new AtomicReference<>("");
        getPage().waitForResponse(response -> {
            if (response.url().contains("/transaction/history")) {
                data.set(response.request().postData());
                return true;
            }
            return false;
        }, refreshDataButton::click);
        return data.get();
    }
}
