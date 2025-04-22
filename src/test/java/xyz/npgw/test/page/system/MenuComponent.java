package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
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

        return new AcquirersPage(getPage());
    }

    @Step("Click Gateway Tab")
    public GatewayPage clickGatewayTab() {
        gatewayTab.click();

        return new GatewayPage(getPage());
    }
}
