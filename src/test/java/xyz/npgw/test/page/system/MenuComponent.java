package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.common.util.ResponseUtils;
import xyz.npgw.test.page.base.BaseComponent;

public class MenuComponent extends BaseComponent {

    public MenuComponent(Page page) {
        super(page);
    }

    @Step("Click 'Companies and business units' tab")
    public CompaniesAndBusinessUnitsPage clickCompaniesAndBusinessUnitsTab() {
        tab("Companies and business units").click();

        return new CompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click 'Acquirers' tab")
    public AcquirersPage clickAcquirersTab() {
        ResponseUtils.clickAndWaitForText(getPage(), tab("Acquirers"), "Acquirer name");

        return new AcquirersPage(getPage());
    }

    @Step("Click 'Gateway' tab")
    public GatewayPage clickGatewayTab() {
        ResponseUtils.clickAndWaitForText(getPage(), tab("Gateway"), "Business units list");

        return new GatewayPage(getPage());
    }
}
