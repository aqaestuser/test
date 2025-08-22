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

    public Locator getMoveBusinessUnitControlDownButton(String priority) {
        return getRowByDataKey(priority).getByTestId("MoveMerchantControlDownButton");
    }

    public Locator getMoveBusinessUnitControlUpButton(String priority) {
        return getRowByDataKey(priority).getByTestId("MoveMerchantControlUpButton ");
    }

    public Locator getActivateBusinessUnitControlButton(String priority) {
        return getRowByDataKey(priority).locator("//*[@data-icon='check']/..");
    }

    public Locator getDeactivateBusinessUnitControlButton(String priority) {
        return getRowByDataKey(priority).locator("//*[@data-icon='ban']/..");
    }

    public Locator getDeleteBusinessUnitControlButton(String priority) {
        return getRowByDataKey(priority).getByTestId("DeleteBusinessUnitControlButton");
    }

    @Step("Click 'Move business unit control down' button")
    public SuperFraudControlPage clickMoveBusinessUnitControlDownButton(String priority) {
        getMoveBusinessUnitControlDownButton(priority).click();

        return getCurrentPage();
    }

    @Step("Click 'Move business unit control up' button")
    public SuperFraudControlPage clickMoveBusinessUnitControlUpButton(String priority) {
        getMoveBusinessUnitControlUpButton(priority).click();

        return getCurrentPage();
    }

    @Step("Click 'Activate business unit control' button")
    public ActivateBusinessUnitControlDialog clickActivateBusinessUnitControlButton(String priority) {
        getActivateBusinessUnitControlButton(priority).click();

        return new ActivateBusinessUnitControlDialog(getPage());
    }

    @Step("Click 'Deactivate business unit control' button")
    public DeactivateBusinessUnitControlDialog clickDeactivateBusinessUnitControlButton(String priority) {
        getDeactivateBusinessUnitControlButton(priority).click();

        return new DeactivateBusinessUnitControlDialog(getPage());
    }

    @Step("Click 'Delete business unit control' button")
    public DeleteBusinessUnitControlDialog clickDeleteBusinessUnitControlButton(String priority) {
        getDeleteBusinessUnitControlButton(priority).click();

        return new DeleteBusinessUnitControlDialog(getPage());
    }

    @Step("Hover over Move Control down icon to get Tooltip")
    public SuperFraudControlPage hoverOverMoveControlDownIcon(String priority) {
        getMoveBusinessUnitControlDownButton(priority).hover();

        return getCurrentPage();
    }

    @Step("Hover over Move Control up icon to get Tooltip")
    public SuperFraudControlPage hoverOverMoveControlUpIcon(String priority) {
        getMoveBusinessUnitControlUpButton(priority).hover();

        return getCurrentPage();
    }

    @Step("Hover over Deactivate Control icon to get Tooltip")
    public SuperFraudControlPage hoverOverDeactivateControlIcon(String priority) {
        getDeactivateBusinessUnitControlButton(priority).hover();

        return getCurrentPage();
    }

    @Step("Hover over Activate Control icon to get Tooltip")
    public SuperFraudControlPage hoverOverActivateControlIcon(String priority) {
        getActivateBusinessUnitControlButton(priority).hover();

        return getCurrentPage();
    }

    @Step("Hover over Delete Control icon to get Tooltip")
    public SuperFraudControlPage hoverOverDeleteIcon(String priority) {
        getDeleteBusinessUnitControlButton(priority).hover();

        return getCurrentPage();
    }
}
