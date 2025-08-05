package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.gateway.ActivateBusinessUnitAcquirerDialog;
import xyz.npgw.test.page.dialog.gateway.DeactivateBusinessUnitAcquirerDialog;
import xyz.npgw.test.page.dialog.gateway.DeleteBusinessUnitAcquirerDialog;
import xyz.npgw.test.page.system.GatewayPage;

public class BusinessUnitAcquirersTableComponent extends BaseTableComponent<GatewayPage> {

    public BusinessUnitAcquirersTableComponent(Page page, GatewayPage currentPage) {
        super(page, currentPage);
    }

    @Override
    protected GatewayPage getCurrentPage() {
        return new GatewayPage(getPage());
    }

    @Step("Click 'Move business unit acquirer down' button")
    public GatewayPage clickMoveBusinessUnitAcquirerDownButton(String priority) {
        getRow(priority).getByTestId("MoveMerchantAcquirerDownButton").click();

        return getCurrentPage();
    }

    @Step("Click 'Move business unit acquirer up' button")
    public GatewayPage clickMoveBusinessUnitAcquirerUpButton(String priority) {
        getRow(priority).getByTestId("MoveMerchantAcquirerUpButton").click();

        return getCurrentPage();
    }

    @Step("Click 'Activate business unit acquirer' button")
    public ActivateBusinessUnitAcquirerDialog clickActivateBusinessUnitAcquirerButton(String priority) {
        getRow(priority).locator("//*[@data-icon='check']/..").click();

        return new ActivateBusinessUnitAcquirerDialog(getPage());
    }

    @Step("Click 'Deactivate business unit acquirer' button")
    public DeactivateBusinessUnitAcquirerDialog clickDeactivateBusinessUnitAcquirerButton(String priority) {
        getRow(priority).locator("//*[@data-icon='ban']/..").click();

        return new DeactivateBusinessUnitAcquirerDialog(getPage());
    }

    @Step("Click 'Delete business unit acquirer' button")
    public DeleteBusinessUnitAcquirerDialog clickDeleteBusinessUnitAcquirer(String priority) {
        getRowByDataKey(priority).locator(getByTestId("DeleteMerchantAcquirerButton")).click();

        return new DeleteBusinessUnitAcquirerDialog(getPage());
    }
}
