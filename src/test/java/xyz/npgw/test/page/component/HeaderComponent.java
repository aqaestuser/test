package xyz.npgw.test.page.component;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.LoginPage;
import xyz.npgw.test.page.ReportsPage;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.systemadministration.TeamPage;

public class HeaderComponent extends BaseComponent {

    @Getter
    private final Locator logo = labelExact("NPGW");

    public HeaderComponent(Page page) {
        super(page);
    }

    @Step("Click on 'Transactions' menu in Header")
    public TransactionsPage clickTransactionsLink() {
        link("Transactions").click();

        return new TransactionsPage(getPage());
    }

    @Step("Click on 'Reports' menu in Header")
    public ReportsPage clickReportsLink() {
        link("Reports").click();

        return new ReportsPage(getPage());
    }

    @Step("Click on 'System administration' menu in Header")
    public TeamPage clickSystemAdministrationLink() {
        link("System administration").click();
        getPage().waitForLoadState(LoadState.NETWORKIDLE);

        return new TeamPage(getPage());
    }

    @Step("Press 'Log out' button")
    public LoginPage clickLogOutButton() {
        button("Log out").click();

        return new LoginPage(getPage());
    }

    @Step("Click on 'Dashboard' menu in Header")
    public DashboardPage clickDashboardLink() {
        link("Dashboard").click();

        return new DashboardPage(getPage());
    }
}
