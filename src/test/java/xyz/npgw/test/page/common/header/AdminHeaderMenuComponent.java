package xyz.npgw.test.page.common.header;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dashboard.AdminDashboardPage;
import xyz.npgw.test.page.system.AdminTeamPage;
import xyz.npgw.test.page.transactions.AdminTransactionsPage;

public class AdminHeaderMenuComponent<CurrentPageT> extends BaseHeaderMenuComponent<CurrentPageT> {

    private final Locator systemAdministrationButton = getByRole(AriaRole.LINK, "System administration");

    public AdminHeaderMenuComponent(Page page, CurrentPageT currentPage) {
        super(page, currentPage);
    }

    @Step("Click on 'Dashboard' in the Header")
    public AdminDashboardPage clickDashboardLink() {
        getDashboardButton().click();

        return new AdminDashboardPage(getPage());
    }

    @Step("Click on 'Transactions' in the Header")
    public AdminTransactionsPage clickTransactionsLink() {
        clickAndWaitForTable(getTransactionsButton());

        return new AdminTransactionsPage(getPage());
    }

    @Step("Click on 'System administration' in the Header")
    public AdminTeamPage clickSystemAdministrationLink() {
        clickAndWaitForTable(systemAdministrationButton);
        getPage().waitForLoadState(LoadState.NETWORKIDLE);

        return new AdminTeamPage(getPage());
    }

    @Step("Click 'Logo' button")
    public AdminDashboardPage clickLogoButton() {
        getLogo().click();

        return new AdminDashboardPage(getPage());
    }

}
