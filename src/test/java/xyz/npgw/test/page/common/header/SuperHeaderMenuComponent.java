package xyz.npgw.test.page.common.header;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import xyz.npgw.test.page.ReportsPage;
import xyz.npgw.test.page.dashboard.SuperDashboardPage;
import xyz.npgw.test.page.system.SuperTeamPage;
import xyz.npgw.test.page.transactions.SuperTransactionsPage;

public class SuperHeaderMenuComponent<CurrentPageT> extends BaseHeaderMenuComponent<CurrentPageT> {

    private final Locator systemAdministrationButton = getByRole(AriaRole.LINK, "System administration");
    private final Locator reportsButton = getByRole(AriaRole.LINK, "Reports");

    public SuperHeaderMenuComponent(Page page, CurrentPageT currentPage) {
        super(page, currentPage);
    }

    @Step("Click on 'Dashboard' in the Header")
    public SuperDashboardPage clickDashboardLink() {
        getDashboardButton().click();

        return new SuperDashboardPage(getPage());
    }

    @Step("Click on 'Transactions' in the Header")
    public SuperTransactionsPage clickTransactionsLink() {
        clickAndWaitForTable(getTransactionsButton());

        return new SuperTransactionsPage(getPage());
    }

    @Step("Click on 'System administration' in the Header")
    public SuperTeamPage clickSystemAdministrationLink() {
        clickAndWaitForTable(systemAdministrationButton);
        getPage().waitForLoadState(LoadState.NETWORKIDLE);

        return new SuperTeamPage(getPage());
    }

    @Step("Click 'Logo' button")
    public SuperDashboardPage clickLogoButton() {
        getLogo().click();

        return new SuperDashboardPage(getPage());
    }

    @Step("Click on 'Reports' in the Header")
    public ReportsPage clickReportsLink() {
        clickAndWaitForTable(reportsButton);

        return new ReportsPage(getPage());
    }
}
