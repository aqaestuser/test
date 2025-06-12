package xyz.npgw.test.page.common;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.common.util.ResponseUtils;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.system.AcquirersPage;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;
import xyz.npgw.test.page.system.GatewayPage;

import java.time.LocalTime;

public class MenuComponent extends BaseComponent {

    public MenuComponent(Page page) {
        super(page);
    }

    @Step("Click 'Companies and business units' tab")
    public CompaniesAndBusinessUnitsPage clickCompaniesAndBusinessUnitsTab() {
        getByRole(AriaRole.TAB, "Companies and business units").click();
        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));

        return new CompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click 'Acquirers' tab")
    public AcquirersPage clickAcquirersTab() {
        ResponseUtils.clickAndWaitForText(getPage(),
                getByRole(AriaRole.TAB, "Acquirers"), "Acquirer name");

        return new AcquirersPage(getPage());
    }

    @Step("Click 'Gateway' tab")
    public GatewayPage clickGatewayTab() {
        ResponseUtils.clickAndWaitForText(getPage(),
                getByRole(AriaRole.TAB, "Gateway"), "Priority");

        return new GatewayPage(getPage());
    }
}
