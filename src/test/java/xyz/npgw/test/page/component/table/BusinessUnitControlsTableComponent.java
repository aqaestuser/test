package xyz.npgw.test.page.component.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.control.ActivateBusinessUnitControlDialog;
import xyz.npgw.test.page.dialog.control.DeactivateBusinessUnitControlDialog;
import xyz.npgw.test.page.dialog.control.DeleteBusinessUnitControlDialog;
import xyz.npgw.test.page.system.SuperFraudControlPage;

public class BusinessUnitControlsTableComponent extends BaseTableComponent<SuperFraudControlPage> {

    public BusinessUnitControlsTableComponent(Page page, SuperFraudControlPage currentPage) {
        super(page, currentPage,
                page.getByText("Connected business unit controls", new Page.GetByTextOptions().setExact(true))
                .locator("../.."));
    }

    @Override
    protected SuperFraudControlPage getCurrentPage() {
        return new SuperFraudControlPage(getPage());
    }

    @Step("Click 'Move business unit control down' button")
    public SuperFraudControlPage clickMoveBusinessUnitControlDownButton(String priority) {
        getRowByDataKey(priority).getByTestId("MoveMerchantControlDownButton").click();

        return getCurrentPage();
    }

    @Step("Click 'Move business unit control up' button")
    public SuperFraudControlPage clickMoveBusinessUnitControlUpButton(String priority) {
        getRowByDataKey(priority).getByTestId("MoveMerchantControlUpButton").click();

        return getCurrentPage();
    }

    @Step("Get 'Move business unit control down' button")
    public Locator getMoveBusinessUnitControlDownButton(String priority) {
        return getRowByDataKey(priority).getByTestId("MoveMerchantControlDownButton");
    }

    @Step("Get 'Move business unit control up' button")
    public Locator getMoveBusinessUnitControlUpButton(String priority) {
        return getRowByDataKey(priority).getByTestId("MoveMerchantControlUpButton");
    }

    @Step("Click 'Activate business unit control' button")
    public ActivateBusinessUnitControlDialog clickActivateBusinessUnitControlButton(String priority) {
        getRowByDataKey(priority).locator("//*[@data-icon='check']/..").click();

        return new ActivateBusinessUnitControlDialog(getPage());
    }

    @Step("Click 'Deactivate business unit control' button")
    public DeactivateBusinessUnitControlDialog clickDeactivateBusinessUnitControlButton(String priority) {
        getRowByDataKey(priority).locator("//*[@data-icon='ban']/..").click();

        return new DeactivateBusinessUnitControlDialog(getPage());
    }

    @Step("Click 'Delete business unit control' button")
    public DeleteBusinessUnitControlDialog clickDeleteBusinessUnitControlButton(String priority) {
        getRowByDataKey(priority).getByTestId("DeleteBusinessUnitControlButton").click();

        return new DeleteBusinessUnitControlDialog(getPage());
    }

    @Step("Click 'Delete business unit control' button")
    public DeleteBusinessUnitControlDialog clickDeleteBusinessUnitControlButtonByName(String displayName) {
        getRow(displayName).getByTestId("DeleteBusinessUnitControlButton").click();

        return new DeleteBusinessUnitControlDialog(getPage());
    }
}
