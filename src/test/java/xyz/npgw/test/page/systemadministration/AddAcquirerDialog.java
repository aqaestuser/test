package xyz.npgw.test.page.systemadministration;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.BasePage;

import java.util.List;

public class AddAcquirerDialog extends BasePage {

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
    @Getter
    private final Locator closeButton = textExact("Close");

    public AddAcquirerDialog(Page page) {
        super(page);
    }

    public List<String> getAllFieldPlaceholders() {
        allFieldPlaceholders.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        return allFieldPlaceholders.all().stream().map(l -> l.getAttribute("placeholder")).toList();
    }

    @Step("Click 'Close' button")
    public AcquirersPage clickCloseButton() {
        closeButton.click();

        return new AcquirersPage(getPage());
    }
}
