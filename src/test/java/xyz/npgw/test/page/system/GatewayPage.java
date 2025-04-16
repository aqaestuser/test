package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;

public class GatewayPage extends BaseSystemPage {

    private final Locator currencyLabel = labelExact("Currency");
    @Getter
    private final Locator currencyValue = locator("div[data-slot='innerWrapper'] span");
    private final Locator currencyDropdown = locator("div[data-slot='listbox']");
    @Getter
    private final Locator currencyOptions = option(currencyDropdown);

    public GatewayPage(Page page) {
        super(page);
    }

    @Step("Click Currency value")
    public GatewayPage clickCurrencyValue() {
        currencyLabel.waitFor();
        currencyValue.click();

        return this;
    }

    @Step("Select Currency '{currency}'")
    public GatewayPage selectCurrency(String currency) {
        currencyOptions
                .filter(new Locator.FilterOptions().setHasText(currency))
                .click();
        currencyDropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return this;
    }
}
