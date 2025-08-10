package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.AccessLevel;
import lombok.Getter;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.component.header.SuperHeaderMenuTrait;
import xyz.npgw.test.page.component.select.SelectAcquirerTrait;
import xyz.npgw.test.page.component.select.SelectBusinessUnitTrait;
import xyz.npgw.test.page.component.select.SelectCompanyTrait;
import xyz.npgw.test.page.component.system.SuperSystemMenuTrait;
import xyz.npgw.test.page.component.table.BusinessUnitAcquirersTableTrait;
import xyz.npgw.test.page.dialog.gateway.AddBusinessUnitAcquirerDialog;

@Getter
public class SuperGatewayPage extends HeaderPage<SuperGatewayPage>
        implements SuperHeaderMenuTrait<SuperGatewayPage>,
                   SuperSystemMenuTrait,
                   SelectCompanyTrait<SuperGatewayPage>,
                   SelectBusinessUnitTrait<SuperGatewayPage>,
                   SelectAcquirerTrait<SuperGatewayPage>,
                   BusinessUnitAcquirersTableTrait {

    private final Locator currencyValue = locator("div[data-slot='innerWrapper'] span");
    @Getter(AccessLevel.NONE)
    private final Locator currencyDropdown = locator("div[data-slot='listbox']");
    private final Locator currencyOptions = currencyDropdown.getByRole(AriaRole.OPTION);

    public SuperGatewayPage(Page page) {
        super(page);
    }

    @Step("Click 'Add merchant acquirer button'")
    public AddBusinessUnitAcquirerDialog clickAddBusinessUnitAcquirerButton() {
        getByTestId("AddMerchantAcquirerButton").click();

        return new AddBusinessUnitAcquirerDialog(getPage());
    }

    @Step("Click Currency value")
    public SuperGatewayPage clickCurrencyValue() {
        currencyValue.click();

        return this;
    }

    @Step("Select Currency '{currency}'")
    public SuperGatewayPage selectCurrency(String currency) {
        currencyOptions.filter(new Locator.FilterOptions().setHasText(currency)).click();
        currencyDropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return this;
    }

    @Step("Click 'Reset filter' button")
    public SuperGatewayPage clickResetFilterButton() {
        getByTestId("ResetButtonTeamPage").click();

        return this;
    }

    @Step("Click 'Refresh data' button")
    public SuperGatewayPage clickRefreshDataButton() {
        getByTestId("ApplyFilterButtonsMerchantsPage").click();

        return this;
    }
}
