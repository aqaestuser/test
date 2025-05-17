package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.DateRangePickerTrait;
import xyz.npgw.test.page.common.trait.SelectBusinessUnitTrait;

public final class DashboardPage extends HeaderPage implements DateRangePickerTrait<DashboardPage>,
        AlertTrait<DashboardPage>, SelectBusinessUnitTrait<DashboardPage> {

    private final Locator refreshDataButton = locator("[data-icon='arrows-rotate']");

    public DashboardPage(Page page) {
        super(page);
    }

    @Step("Click 'Refresh data' button")
    public DashboardPage clickRefreshDataButton() {
        refreshDataButton.click();

        return this;
    }

    @Step("Reload dashboard page")
    public DashboardPage reloadDashboard() {
        getPage().reload();

        return this;
    }
}
