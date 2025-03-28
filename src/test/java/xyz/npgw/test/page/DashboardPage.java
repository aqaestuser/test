package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.AuthenticatedPage;

public final class DashboardPage extends AuthenticatedPage {

    private final Locator logOutButton = button("Log out");
    private final Locator transactionsLink = linkByName("Transactions");


    public DashboardPage(Page page) {
        super(page);
    }

    @Step("Press 'Log out' button")
    public LoginPage clickLogOutButton() {
        logOutButton.click();
        return new LoginPage(getPage());
    }

    @Step("Click Transactions Link")
    public TransactionsPage clickTransactionsLink() {
        transactionsLink.click();
        return new TransactionsPage(getPage());
    }
}
