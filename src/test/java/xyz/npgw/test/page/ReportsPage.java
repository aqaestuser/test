package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.common.trait.ReportsTableTrait;
import xyz.npgw.test.page.common.trait.SelectBusinessUnitTrait;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;
import xyz.npgw.test.page.common.trait.SelectDateRangeTrait;
import xyz.npgw.test.page.dialog.reports.ReportsParametersDialog;

public class ReportsPage extends HeaderPage<ReportsPage> implements ReportsTableTrait,
        SelectDateRangeTrait<ReportsPage>,
        SelectCompanyTrait<ReportsPage>,
        SelectBusinessUnitTrait<ReportsPage> {

    private final Locator refreshDataButton = locator("[data-icon='arrows-rotate']");
    private final Locator generateReportButton = getByRole(AriaRole.BUTTON, "Generate report");
    private final Locator resetFilterButton = getByTestId("ResetFilterButtonReportsPage");

    public ReportsPage(Page page) {
        super(page);
    }

    @Step("Click 'Refresh data' button")
    public ReportsPage clickRefreshDataButton() {
        refreshDataButton.click();

        return this;
    }

    @Step("Click 'Generation report' button")
    public ReportsParametersDialog clickGenerateReportButton() {
        generateReportButton.click();

        return new ReportsParametersDialog(getPage());
    }

    @Step("Reload Reports page")
    public ReportsPage refreshReports() {
        getPage().reload();

        return this;
    }

    @Step("Click 'Reset filter' button")
    public ReportsPage clickResetFilterButton() {
        resetFilterButton.click();

        return this;
    }
}
