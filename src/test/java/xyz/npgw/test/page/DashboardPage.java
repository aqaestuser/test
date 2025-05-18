package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.AccessLevel;
import lombok.Getter;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.DateRangePickerTrait;
import xyz.npgw.test.page.common.trait.SelectBusinessUnitTrait;

@Getter
public final class DashboardPage extends HeaderPage implements DateRangePickerTrait<DashboardPage>,
        AlertTrait<DashboardPage>, SelectBusinessUnitTrait<DashboardPage> {

    @Getter(AccessLevel.NONE)
    private final Locator refreshDataButton = locator("[data-icon='arrows-rotate']");
    private final Locator yAxisLabels = locator(".apexcharts-yaxis-label tspan");
    private final Locator xAxisTexts = locator(".apexcharts-xaxis tspan");
    private final Locator currencyLegendLabels = locator("span.apexcharts-legend-text");

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
