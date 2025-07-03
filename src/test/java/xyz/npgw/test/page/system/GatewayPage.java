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
    private final Locator merchantFirstRowValue = locator("[data-key='00merchantId']");
    private final Locator acquirerFirstRowValue = locator("[data-key='00acquirerCode']");
    private final Locator acquirerConfigFirstRowValue = locator("[data-key='00acquirerConfig']");
    private final Locator acquirerStatusFirstRowValue = locator("[data-key='00isActive']");
    private final Locator acquirerPriorityFirstRowValue = locator("[data-key='00priority']");
    private final Locator acquirerCurrencyFirstRowValue = locator("[data-key='00currencyList']");
    private final Locator acquirerNameFirstRowValue = locator("[data-key='00acquirerDisplayName']");
    @Getter(AccessLevel.NONE)
    private final Locator currencyDropdown = locator("div[data-slot='listbox']");
    private final Locator currencyOptions = currencyDropdown.getByRole(AriaRole.OPTION);
    private final Locator resetFilterButton = locator("[data-icon='xmark']");
    private final Locator addBusinessUnitAcquirerButton = getByTestId("AddMerchantAcquirerButton");
    private final Locator moveBusinessUnitAcquirerDownButton = getByTestId("MoveMerchantAcquirerDownButton");
    private final Locator moveBusinessUnitAcquirerUpButton = getByTestId("MoveMerchantAcquirerUpButton");

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

    @Step("Click 'Add merchant acquirer button'")
    public AddMerchantAcquirerDialog clickAddBusinessUnitAcquirerButton() {
        addBusinessUnitAcquirerButton.click();

        return new AddMerchantAcquirerDialog(getPage());
    }

    @Step("Click on MoveMerchantAcquirerDownButton to move them down with less priority")
    public GatewayPage clickMoveBusinessUnitAcquirerDownButton(int row) {
        moveBusinessUnitAcquirerDownButton.nth(row).click();

        return this;
    }

    @Step("Click on MoveMerchantAcquirerUpButton to move them up with more  priority")
    public GatewayPage clickMoveBusinessUnitAcquirerUpButton(int row) {
        moveBusinessUnitAcquirerUpButton.nth(row).click();

        return this;
    }

    @Step("Click 'Refresh data' button")
    public GatewayPage clickRefreshDataButton() {
        getByTestId("ApplyFilterButtonsMerchantsPage").click();

        return this;
    }
}
