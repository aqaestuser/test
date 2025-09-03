package xyz.npgw.test.page.component.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.system.SuperAcquirersPage;
import xyz.npgw.test.page.system.SuperCompaniesAndBusinessUnitsPage;
import xyz.npgw.test.page.system.SuperFraudControlPage;
import xyz.npgw.test.page.system.SuperGatewayPage;
import xyz.npgw.test.page.system.SuperTeamPage;
import xyz.npgw.test.page.system.SuperTransactionManagementPage;

import java.util.Objects;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SuperSystemMenuComponent extends BaseComponent {
    private final Locator teamTab = getByRole(AriaRole.TAB, "Team");
    private final Locator companiesAndBusinessUnitsTab = getByRole(AriaRole.TAB, "Companies and business units");
    private final Locator acquirersTab = getByRole(AriaRole.TAB, "Acquirers");
    private final Locator gatewayTab = getByRole(AriaRole.TAB, "Gateway");
    private final Locator fraudControlTab = getByRole(AriaRole.TAB, "Fraud control");
    private final Locator transactionManagementTab = getByRole(AriaRole.TAB, "Transaction management");

    public SuperSystemMenuComponent(Page page) {
        super(page);
    }

    @Step("Click 'Team' tab")
    public SuperTeamPage clickTeamTab() {
        clickTabIfNeededAndCheckIt(teamTab);

        return new SuperTeamPage(getPage());
    }

    @Step("Click 'Companies and business units' tab")
    public SuperCompaniesAndBusinessUnitsPage clickCompaniesAndBusinessUnitsTab() {
        clickTabIfNeededAndCheckIt(companiesAndBusinessUnitsTab);

        return new SuperCompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click 'Acquirers' tab")
    public SuperAcquirersPage clickAcquirersTab() {
        clickTabIfNeededAndCheckIt(acquirersTab);

        return new SuperAcquirersPage(getPage());
    }

    @Step("Click 'Gateway' tab")
    public SuperGatewayPage clickGatewayTab() {
        clickTabIfNeededAndCheckIt(gatewayTab);

        return new SuperGatewayPage(getPage());
    }

    @Step("click 'Fraud control' tab")
    public SuperFraudControlPage clickFraudControlTab() {
        clickTabIfNeededAndCheckIt(fraudControlTab);

        return new SuperFraudControlPage(getPage());
    }

    @Step("click 'Transaction management' tab")
    public SuperTransactionManagementPage clickTransactionManagementTab() {
        clickTabIfNeededAndCheckIt(transactionManagementTab);

        return new SuperTransactionManagementPage(getPage());
    }


    protected void clickTabIfNeededAndCheckIt(Locator tab) {
        tab.waitFor();
        if (Objects.equals(tab.getAttribute("aria-selected"), "false")) {
            tab.click();
            assertThat(tab).hasAttribute("data-selected", "true");
        }
        getPage().waitForLoadState(LoadState.NETWORKIDLE);
    }
}
