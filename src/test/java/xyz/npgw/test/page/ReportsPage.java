package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.common.DateRangePickerTrait;
import xyz.npgw.test.page.common.HeaderPage;
import xyz.npgw.test.page.common.TableTrait;

public class ReportsPage extends HeaderPage implements TableTrait, DateRangePickerTrait<ReportsPage> {

    private final Locator refreshDataButton = locator("[data-icon='arrows-rotate']");

    public ReportsPage(Page page) {
        super(page);
    }

    @Step("Click 'Refresh data' button")
    public ReportsPage clickRefreshDataButton() {
        refreshDataButton.click();

        return this;
    }
}
