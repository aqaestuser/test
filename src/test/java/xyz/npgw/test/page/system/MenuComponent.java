package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BaseComponent;

public class MenuComponent extends BaseComponent {

    private final Locator companiesAndBusinessUnitsTabButton = tab("Companies and business units");
    private final Locator acquirersTab = tab("Acquirers");
    private final Locator gatewayTab = tab("Gateway");

    public MenuComponent(Page page) {
        super(page);
    }

    @Step("Click 'Companies and business units' Tab")
    public CompaniesAndBusinessUnitsPage clickCompaniesAndBusinessUnitsTab() {
        companiesAndBusinessUnitsTabButton.click();

        return new CompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click Acquirers Tab")
    public AcquirersPage clickAcquirersTab() {
        acquirersTab.click();
        textExact("Acquirer name")
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5678));

        return new AcquirersPage(getPage());
    }

    @Step("Click Gateway Tab")
    public GatewayPage clickGatewayTab() {
        gatewayTab.click();
        textExact("Business units list")
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5678));

        return new GatewayPage(getPage());
    }
}
