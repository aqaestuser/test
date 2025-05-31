package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.AccessLevel;
import lombok.Getter;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;

@Getter
public class GatewayPage extends BaseSystemPage<GatewayPage> implements SelectCompanyTrait<GatewayPage> {

    private final Locator businessUnitsListHeader = getByTextExact("Business units list");
    private final Locator businessUnitsList = locator("div[data-slot='base'] li");
    private final Locator companyDropdown = locator("div[data-slot='content']");
    private final Locator currencyValue = locator("div[data-slot='innerWrapper'] span");
    @Getter(AccessLevel.NONE)
    private final Locator currencyDropdown = locator("div[data-slot='listbox']");
    private final Locator currencyOptions = currencyDropdown.getByRole(AriaRole.OPTION);
    private final Locator businessUnitsBlock = locator("div[label='Business units list']");

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
