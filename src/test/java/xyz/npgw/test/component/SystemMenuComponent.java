package xyz.npgw.test.component;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.system.AcquirersPage;
import xyz.npgw.test.page.system.GatewayPage;

public class SystemMenuComponent extends BaseComponent {

    private final Locator acquirersTab = tab("Acquirers");
    private final Locator gatewayTab = tab("Gateway");

    public SystemMenuComponent(Page page) {
        super(page);
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
