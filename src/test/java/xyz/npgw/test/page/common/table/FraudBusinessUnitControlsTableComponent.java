package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.control.ActivateBusinessUnitControlActivityDialog;
import xyz.npgw.test.page.dialog.control.DeactivateBusinessUnitControlActivityDialog;
import xyz.npgw.test.page.dialog.control.DeleteBusinessUnitControlDialog;
import xyz.npgw.test.page.system.FraudControlPage;

public class FraudBusinessUnitControlsTableComponent extends BaseTableComponent<FraudControlPage> {

    public FraudBusinessUnitControlsTableComponent(Page page) {
        super(page, page.getByLabel("transactions table")
                .or(page.getByRole(AriaRole.GROUP)
                        .filter(new Locator.FilterOptions().setHasText("Rows Per Page"))).last());
    }

    @Override
    protected FraudControlPage getCurrentPage() {
        return new FraudControlPage(getPage());
    }

    @Step("Click 'Move business unit control down' icon")
    public FraudControlPage clickMoveBusinessUnitAcquirerDownButton(String priority) {
        getRow(priority).locator("//*[@data-icon='circle-arrow-down']").click();

        return getCurrentPage();
    }

    @Step("Click 'Move business unit control up' icon")
    public FraudControlPage clickMoveBusinessUnitAcquirerUpButton(String priority) {
        getRow(priority).locator("//*[@data-icon='circle-arrow-up']").click();

        return getCurrentPage();
    }

    @Step("Click 'Deactivate business unit control' icon")
    public DeactivateBusinessUnitControlActivityDialog clickDeactivateBusinessUnitControlIcon(String priority) {
        getRow(priority).locator("//*[@data-icon='ban']/..").click();

        return new DeactivateBusinessUnitControlActivityDialog(getPage());
    }

    @Step("Click 'Activate business unit control' icon")
    public ActivateBusinessUnitControlActivityDialog clickActivateBusinessUnitControlIcon(String priority) {
        getRow(priority).locator("//*[@data-icon='check']/..").click();

        return new ActivateBusinessUnitControlActivityDialog(getPage());
    }

    @Step("Click 'Delete business unit control' icon")
    public DeleteBusinessUnitControlDialog clickDeleteBusinessUnitControlIcon(String priority) {
        getRow(priority).getByTestId("DeleteControlButton").click();

        return new DeleteBusinessUnitControlDialog(getPage());
    }
}
