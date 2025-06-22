package xyz.npgw.test.page.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.system.AcquirersPage;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;
import xyz.npgw.test.page.system.GatewayPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MenuComponent extends BaseComponent {

    private final Locator companiesAndBusinessUnitsTab = getByRole(AriaRole.TAB, "Companies and business units");
    private final Locator acquirersTab = getByRole(AriaRole.TAB, "Acquirers");
    private final Locator gatewayTab = getByRole(AriaRole.TAB, "Gateway");

    public MenuComponent(Page page) {
        super(page);
    }

    @Step("Click 'Companies and business units' tab")
    public CompaniesAndBusinessUnitsPage clickCompaniesAndBusinessUnitsTab() {
        companiesAndBusinessUnitsTab.click();
        assertThat(companiesAndBusinessUnitsTab).hasAttribute("data-selected", "true");
//        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));

        return new CompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click 'Acquirers' tab")
    public AcquirersPage clickAcquirersTab() {
        acquirersTab.click();
        assertThat(acquirersTab).hasAttribute("data-selected", "true");
//        ResponseUtils.clickAndWaitForText(getPage(),
//                getByRole(AriaRole.TAB, "Acquirers"), "Acquirer name");

        return new AcquirersPage(getPage());
    }

    @Step("Click 'Gateway' tab")
    public GatewayPage clickGatewayTab() {
        gatewayTab.click();
        assertThat(gatewayTab).hasAttribute("data-selected", "true");
//        ResponseUtils.clickAndWaitForText(getPage(),
//                getByRole(AriaRole.TAB, "Gateway"), "Priority");

        return new GatewayPage(getPage());
    }
}
