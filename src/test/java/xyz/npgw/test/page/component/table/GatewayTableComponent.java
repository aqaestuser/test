package xyz.npgw.test.page.component.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.gateway.ActivateBusinessUnitAcquirerDialog;
import xyz.npgw.test.page.dialog.gateway.DeactivateBusinessUnitAcquirerDialog;
import xyz.npgw.test.page.dialog.gateway.DeleteBusinessUnitAcquirerDialog;
import xyz.npgw.test.page.system.SuperGatewayPage;

public class GatewayTableComponent extends BaseTableComponent<SuperGatewayPage> {

    public GatewayTableComponent(Page page, SuperGatewayPage currentPage) {
        super(page, currentPage);
    }

    @Override
    protected SuperGatewayPage getCurrentPage() {
        return new SuperGatewayPage(getPage());
    }

    public Locator getMoveAcquirerMidDownButton(String priority) {
        return getRowByDataKey(priority).getByTestId("MoveMerchantAcquirerDownButton");
    }

    public Locator getMoveAcquirerMidUpButton(String priority) {
        return getRowByDataKey(priority).getByTestId("MoveMerchantAcquirerUpButton");
    }

    public Locator getActivateBusinessUnitAcquirerButton(String priority) {
        return getRowByDataKey(priority).locator("//*[@data-icon='check']/..");
    }

    public Locator getDeactivateAcquirerMidButton(String priority) {
        return getRowByDataKey(priority).locator("//*[@data-icon='ban']/..");
    }

    public Locator getDeleteAcquirerMidButton(String priority) {
        return getRowByDataKey(priority).getByTestId("DeleteMerchantAcquirerButton");
    }

    @Step("Hover over 'Move acquirer MID down' button")
    public SuperGatewayPage hoverOverMoveAcquirerMidDownButton(String priority) {
        getMoveAcquirerMidDownButton(priority).hover();

        return getCurrentPage();
    }

    @Step("Click 'Move acquirer MID down' button")
    public SuperGatewayPage clickMoveAcquirerMidDownButton(String priority) {
        getMoveAcquirerMidDownButton(priority).click();

        return getCurrentPage();
    }

    @Step("Hover over 'Move acquirer MID up' button")
    public SuperGatewayPage hoverOverMoveAcquirerMidUpButton(String priority) {
        getMoveAcquirerMidUpButton(priority).hover();

        return getCurrentPage();
    }

    @Step("Click 'Move acquirer MID up' button")
    public SuperGatewayPage clickMoveAcquirerMidUpButton(String priority) {
        getMoveAcquirerMidUpButton(priority).click();

        return getCurrentPage();
    }

    @Step("Hover over 'Activate acquirer MID' button")
    public SuperGatewayPage hoverOverActivateAcquirerMidButton(String priority) {
        getActivateBusinessUnitAcquirerButton(priority).hover();

        return getCurrentPage();
    }

    @Step("Click 'Activate acquirer MID' button")
    public ActivateBusinessUnitAcquirerDialog clickActivateAcquirerMidButton(String priority) {
        getActivateBusinessUnitAcquirerButton(priority).click();

        return new ActivateBusinessUnitAcquirerDialog(getPage());
    }

    @Step("Hover over 'Deactivate acquirer MID' button")
    public SuperGatewayPage hoverOverDeactivateAcquirerMidButton(String priority) {
        getDeactivateAcquirerMidButton(priority).hover();

        return getCurrentPage();
    }

    @Step("Click 'Deactivate acquirer MID' button")
    public DeactivateBusinessUnitAcquirerDialog clickDeactivateAcquirerMidButton(String priority) {
        getDeactivateAcquirerMidButton(priority).click();

        return new DeactivateBusinessUnitAcquirerDialog(getPage());
    }

    @Step("Hover over 'Delete acquirer MID' button for {priority} priority")
    public SuperGatewayPage hoverOverDeleteAcquirerMidButton(String priority) {
        getDeleteAcquirerMidButton(priority).hover();

        return getCurrentPage();
    }

    @Step("Click 'Delete acquirer MID' button")
    public DeleteBusinessUnitAcquirerDialog clickDeleteAcquirerMidButton(String priority) {
        getDeleteAcquirerMidButton(priority).click();

        return new DeleteBusinessUnitAcquirerDialog(getPage());
    }
}
