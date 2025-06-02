package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.common.util.ResponseUtils;
import xyz.npgw.test.page.base.BaseComponent;

import java.util.regex.Pattern;

public class MenuComponent extends BaseComponent {

    private final Locator companiesAndBusinessUnitsTabButton = tab("Companies and business units");
    private final Locator acquirersTab = tab("Acquirers");
    private final Locator gatewayTab = tab("Gateway");

    public MenuComponent(Page page) {
        super(page);
    }

    @Step("Click 'Companies and business units' Tab")
    public CompaniesAndBusinessUnitsPage clickCompaniesAndBusinessUnitsTab() {
        ResponseUtils.clickAndWaitForResponse(
                getPage(),
                companiesAndBusinessUnitsTabButton,
                Pattern.compile(".*/Merchants-.*\\.css$"),
                1500);

        return new CompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click Acquirers Tab")
    public AcquirersPage clickAcquirersTab() {
        ResponseUtils.clickAndWaitForText(getPage(), acquirersTab, "Acquirer name");

        return new AcquirersPage(getPage());
    }

    @Step("Click Gateway Tab")
    public GatewayPage clickGatewayTab() {
//        ResponseUtils.clickAndWaitForText(getPage(), gatewayTab, "Business units list");

        ResponseUtils.clickAndWaitForResponse(
                getPage(),
                gatewayTab,
                Pattern.compile(".*/Gateway-.*\\.css$"),
                1500);

        return new GatewayPage(getPage());
    }
}
