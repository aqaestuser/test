package xyz.npgw.test.page.dialog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.systemadministration.AcquirersPage;

import java.util.List;

public class AddAcquirerDialog extends BaseDialog {

    @Getter
    private final Locator addAcquirerDialogHeader = locator("section header");
    private final Locator allFieldPlaceholders = locator("[data-slot='input']:not([placeholder='Search...'])");
    @Getter
    private final Locator statusSwitch = locator("div[role='radiogroup']");
    @Getter
    private final Locator allowedCurrenciesCheckboxes = locator("div[role='group']");
    @Getter
    private final Locator selectCountryPlaceholder = labelExact("Select country");
    @Getter
    private final Locator selectTimezone = textExact("Select timezone");

    public AddAcquirerDialog(Page page) {
        super(page);
    }

    public List<String> getAllFieldPlaceholders() {
        allFieldPlaceholders.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        return allFieldPlaceholders.all().stream().map(l -> l.getAttribute("placeholder")).toList();
    }

    @Step("Click 'Close' button")
    public AcquirersPage clickCloseButton() {
        getCloseButton().click();

        return new AcquirersPage(getPage());
    }

    @Step("Click '{option}' radiobutton")
    public AddAcquirerDialog clickStatusRadiobutton(String option) {
        labelExact(option).click();

        return this;
    }

    public Locator getStatusRadiobutton(String value) {
        return statusSwitch.locator("label:has(input[value='" + value.toUpperCase() + "'])");
    }
}
