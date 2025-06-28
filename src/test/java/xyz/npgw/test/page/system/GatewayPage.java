package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.AccessLevel;
import lombok.Getter;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.GatewayTableTrait;
import xyz.npgw.test.page.common.trait.SelectAcquirerTrait;
import xyz.npgw.test.page.common.trait.SelectBusinessUnitTrait;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;
import xyz.npgw.test.page.dialog.acquirer.AddMerchantAcquirerDialog;

@Getter
public class GatewayPage extends BaseSystemPage<GatewayPage> implements SelectCompanyTrait<GatewayPage>,
        SelectBusinessUnitTrait<GatewayPage>, SelectAcquirerTrait<GatewayPage>, AlertTrait<GatewayPage>,
        GatewayTableTrait {

    private final Locator currencyValue = locator("div[data-slot='innerWrapper'] span");
    private final Locator merchantValue = locator("[data-key='00merchantId']");
    private final Locator acquirerValue = locator("[data-key='00acquirerCode']");
    private final Locator acquirerConfigValue = locator("[data-key='00acquirerConfig']");
    private final Locator acquirerStatusValue = locator("[data-key='00isActive']");
    private final Locator acquirerPriorityValue = locator("[data-key='00priority']");
    private final Locator acquirerCurrencyValue = locator("[data-key='00currencyList']");
    @Getter(AccessLevel.NONE)
    private final Locator currencyDropdown = locator("div[data-slot='listbox']");
    private final Locator currencyOptions = currencyDropdown.getByRole(AriaRole.OPTION);
    private final Locator resetFilterButton = locator("[data-icon='xmark']");
    private final Locator addMerchantAcquirer = locator("[data-icon='circle-plus']");
    private final Locator addMerchantAcquirerButton = getByTestId("AddMerchantAcquirerButton");

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

    @Step("Click 'Reset filter' button")
    public GatewayPage clickResetFilterButton() {
        resetFilterButton.click();

        return this;
    }

    @Step("Click 'Add Merchant Acquirer' button")
    public AddMerchantAcquirerDialog clickAddMerchantAcquirer() {
        addMerchantAcquirer.click();

        return new AddMerchantAcquirerDialog(getPage());
    }

    @Step("Click 'Add merchant acquirer button'")
    public AddMerchantAcquirerDialog clickAddMerchantAcquirerButton() {
        addMerchantAcquirerButton.click();

        return new AddMerchantAcquirerDialog(getPage());
    }
}
