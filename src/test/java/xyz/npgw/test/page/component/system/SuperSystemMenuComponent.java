package xyz.npgw.test.page.component.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.system.SuperAcquirersPage;
import xyz.npgw.test.page.system.SuperCompaniesAndBusinessUnitsPage;
import xyz.npgw.test.page.system.SuperFraudControlPage;
import xyz.npgw.test.page.system.SuperGatewayPage;
import xyz.npgw.test.page.system.SuperTeamPage;
import xyz.npgw.test.page.system.SuperTransactionManagementPage;

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
        clickAndCheckActive(teamTab);

        return new SuperTeamPage(getPage());
    }

    @Step("Click 'Companies and business units' tab")
    public SuperCompaniesAndBusinessUnitsPage clickCompaniesAndBusinessUnitsTab() {
        clickAndCheckActive(companiesAndBusinessUnitsTab);

        return new SuperCompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click 'Acquirers' tab")
    public SuperAcquirersPage clickAcquirersTab() {
        clickAndCheckActive(acquirersTab);

        return new SuperAcquirersPage(getPage());
    }

    @Step("Click 'Gateway' tab")
    public SuperGatewayPage clickGatewayTab() {
        clickAndCheckActive(gatewayTab);

        return new SuperGatewayPage(getPage());
    }

    @Step("click 'Fraud control' tab")
    public SuperFraudControlPage clickFraudControlTab() {
        clickAndCheckActive(fraudControlTab);

        return new SuperFraudControlPage(getPage());
    }

    @Step("click 'Transaction management' tab")
    public SuperTransactionManagementPage clickTransactionManagementTab() {
        clickAndCheckActive(transactionManagementTab);

        return new SuperTransactionManagementPage(getPage());
    }

    private void clickAndCheckActive(Locator button) {
        button.click();
        assertThat(button).hasAttribute("data-selected", "true");
    }
}
