package xyz.npgw.test.page.component;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.LoginPage;
import xyz.npgw.test.page.ReportsPage;
import xyz.npgw.test.page.SystemAdministrationPage;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.base.BasePage;

public class HeaderComponent<T extends BasePage<T>> extends BaseComponent<T> {

    public HeaderComponent(Page page, T owner) {
        super(page, owner);
    }

    @Step("Click on 'Dashboard' menu in Header")
    public DashboardPage clickDashboardLink() {
        link("Dashboard").click();

        return new DashboardPage(getPage());
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
    public SystemAdministrationPage clickSystemAdministrationLink() {
        link("System administration").click();
        getPage().waitForLoadState(LoadState.NETWORKIDLE);

        return new SystemAdministrationPage(getPage());
    }

    @Step("Press 'Log out' button")
    public LoginPage clickLogOutButton() {
        button("Log out").click();

        return new LoginPage(getPage());
    }

    @Step("Click user profile icon")
    public HeaderComponent<T> clickUserProfileButton() {
        getPage().locator("ul button span").click();

        return this;
    }

    @Step("Press user profile 'Log out' button")
    public LoginPage clickUserProfileLogOutButton() {
        button("Log Out").click();

        return new LoginPage(getPage());
    }
}
