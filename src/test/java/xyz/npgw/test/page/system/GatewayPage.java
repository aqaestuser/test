package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;

public class GatewayPage extends BaseSystemPage<GatewayPage> implements SelectCompanyTrait<GatewayPage> {

    @Getter
    private final Locator businessUnitsListHeader = getByTextExact("Business units list");
    @Getter
    private final Locator businessUnitsList = locator("div[data-slot='base'] li");
    @Getter
    private final Locator companyDropdown = locator("div[data-slot='content']");
    @Getter
    private final Locator currencyValue = locator("div[data-slot='innerWrapper'] span");
    private final Locator currencyDropdown = locator("div[data-slot='listbox']");
    @Getter
    private final Locator currencyOptions = currencyDropdown.getByRole(AriaRole.OPTION);

    public GatewayPage(Page page) {
        super(page);
    }

    @Step("Click Currency value")
    public GatewayPage clickCurrencyValue() {
        currencyValue.click();

        return this;
    }

    @Step("Select Currency '{currency}'")
    public GatewayPage selectCurrency(String currency) {
        currencyOptions.filter(new Locator.FilterOptions().setHasText(currency)).click();
        currencyDropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return this;
    }
}
