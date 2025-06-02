package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import xyz.npgw.test.page.common.DateRangePickerTrait;
import xyz.npgw.test.page.common.HeaderPage;

public final class DashboardPage extends HeaderPage implements DateRangePickerTrait<DashboardPage> {

    private final Locator refreshDataButton = locator("[data-icon='arrows-rotate']");
    private final Locator successAlert = alert("SUCCESS");

    public DashboardPage(Page page) {
        super(page);
    }

    @Step("Click 'Refresh data' button")
    public DashboardPage clickRefreshDataButton() {
        refreshDataButton.click();

        return this;
    }

    public DashboardPage waitUntilAlertIsGone() {
        successAlert.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        successAlert.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        return this;
    }
}
