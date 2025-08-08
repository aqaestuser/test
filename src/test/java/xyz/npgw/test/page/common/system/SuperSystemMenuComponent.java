package xyz.npgw.test.page.common.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.system.AcquirersPage;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;
import xyz.npgw.test.page.system.FraudControlPage;
import xyz.npgw.test.page.system.GatewayPage;
import xyz.npgw.test.page.system.SuperTeamPage;
import xyz.npgw.test.page.system.TransactionManagementPage;

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
    public CompaniesAndBusinessUnitsPage clickCompaniesAndBusinessUnitsTab() {
        clickAndCheckActive(companiesAndBusinessUnitsTab);

        return new CompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click 'Acquirers' tab")
    public AcquirersPage clickAcquirersTab() {
        clickAndCheckActive(acquirersTab);

        return new AcquirersPage(getPage());
    }

    @Step("Click 'Gateway' tab")
    public GatewayPage clickGatewayTab() {
        clickAndCheckActive(gatewayTab);

        return new GatewayPage(getPage());
    }

    @Step("click 'Fraud control' tab")
    public FraudControlPage clickFraudControlTab() {
        clickAndCheckActive(fraudControlTab);

        return new FraudControlPage(getPage());
    }

    @Step("click 'Transaction management' tab")
    public TransactionManagementPage clickTransactionManagementTab() {
        clickAndCheckActive(transactionManagementTab);

        return new TransactionManagementPage(getPage());
    }

    private void clickAndCheckActive(Locator button) {
        button.click();
        assertThat(button).hasAttribute("data-selected", "true");
    }
}
