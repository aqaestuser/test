package xyz.npgw.test.page.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.system.AcquirersPage;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;
import xyz.npgw.test.page.system.FraudControlPage;
import xyz.npgw.test.page.system.GatewayPage;
import xyz.npgw.test.page.system.TeamPage;
import xyz.npgw.test.page.system.TransactionManagementPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MenuComponent extends BaseComponent {

    private final Locator teamTab = getByRole(AriaRole.TAB, "Team");
    private final Locator companiesAndBusinessUnitsTab = getByRole(AriaRole.TAB, "Companies and business units");
    private final Locator acquirersTab = getByRole(AriaRole.TAB, "Acquirers");
    private final Locator gatewayTab = getByRole(AriaRole.TAB, "Gateway");
    private final Locator fraudControlTab = getByRole(AriaRole.TAB, "Fraud control");
    private final Locator transactionManagementTab = getByRole(AriaRole.TAB, "Transaction management");

    public MenuComponent(Page page) {
        super(page);
    }

    @Step("Click 'Team' tab")
    public TeamPage clickTeamTab() {
        teamTab.click();
        assertThat(teamTab).hasAttribute("data-selected", "true");

        return new TeamPage(getPage());
    }

    @Step("Click 'Companies and business units' tab")
    public CompaniesAndBusinessUnitsPage clickCompaniesAndBusinessUnitsTab() {
        companiesAndBusinessUnitsTab.click();
        assertThat(companiesAndBusinessUnitsTab).hasAttribute("data-selected", "true");

        return new CompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click 'Acquirers' tab")
    public AcquirersPage clickAcquirersTab() {
        acquirersTab.click();
        assertThat(acquirersTab).hasAttribute("data-selected", "true");

        return new AcquirersPage(getPage());
    }

    @Step("Click 'Gateway' tab")
    public GatewayPage clickGatewayTab() {
        gatewayTab.click();
        assertThat(gatewayTab).hasAttribute("data-selected", "true");

        return new GatewayPage(getPage());
    }

    @Step("click 'Fraud control' tab")
    public FraudControlPage clickFraudControlTab() {
        fraudControlTab.click();
        assertThat(fraudControlTab).hasAttribute("data-selected", "true");

        return new FraudControlPage(getPage());
    }

    @Step("click 'Transaction management' tab")
    public TransactionManagementPage clickTransactionManagementTab() {
        transactionManagementTab.click();
        assertThat(transactionManagementTab).hasAttribute("data-selected", "true");

        return new TransactionManagementPage(getPage());
    }
}
