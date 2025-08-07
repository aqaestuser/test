package xyz.npgw.test.page.common.header;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dashboard.UserDashboardPage;
import xyz.npgw.test.page.transactions.UserTransactionsPage;

public class UserHeaderMenuComponent<CurrentPageT> extends BaseHeaderMenuComponent<CurrentPageT> {

    public UserHeaderMenuComponent(Page page, CurrentPageT currentPage) {
        super(page, currentPage);
    }

    @Step("Click on 'Dashboard' in the Header")
    public UserDashboardPage clickDashboardLink() {
        getDashboardButton().click();

        return new UserDashboardPage(getPage());
    }

    @Step("Click on 'Transactions' in the Header")
    public UserTransactionsPage clickTransactionsLink() {
        clickAndWaitForTable(getTransactionsButton());

        return new UserTransactionsPage(getPage());
    }

    @Step("Click 'Logo' button")
    public UserDashboardPage clickLogoButton() {
        getLogo().click();

        return new UserDashboardPage(getPage());
    }
}
