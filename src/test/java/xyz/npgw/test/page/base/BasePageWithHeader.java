package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.*;
import xyz.npgw.test.page.component.Header;

public abstract class BasePageWithHeader extends BasePage {

    private final Header header;

    public BasePageWithHeader(Page page) {
        super(page);
        header = new Header(getPage());
    }

    @Step("Click Dashboard Link")
    public DashboardPage clickDashboardLink() {
        return header.clickDashboardLink();
    }

    @Step("Click System Administration Link")
    public SystemAdministrationPage clickSystemAdministrationLink() {
        return header.clickSystemAdministrationLink();
    }

    @Step("Click Reports Link")
    public ReportsPage clickReportsLink() {
        return header.clickReportsLink();
    }

    @Step("Click Transactions Link")
    public TransactionsPage clickTransactionsLink() {
        return header.clickTransactionsLink();
    }

    @Step("Click Logout Link")
    public LoginPage clickLogOutButton() {
        return header.clickLogOutButton();
    }
}
