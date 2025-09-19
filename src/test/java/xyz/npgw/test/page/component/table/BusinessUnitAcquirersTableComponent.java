package xyz.npgw.test.page.component.table;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.gateway.ActivateBusinessUnitAcquirerDialog;
import xyz.npgw.test.page.dialog.gateway.DeactivateBusinessUnitAcquirerDialog;
import xyz.npgw.test.page.dialog.gateway.DeleteBusinessUnitAcquirerDialog;
import xyz.npgw.test.page.system.SuperGatewayPage;

public class BusinessUnitAcquirersTableComponent extends BaseTableComponent<SuperGatewayPage> {

    public BusinessUnitAcquirersTableComponent(Page page, SuperGatewayPage currentPage) {
        super(page, currentPage);
    }

    @Override
    protected SuperGatewayPage getCurrentPage() {
        return new SuperGatewayPage(getPage());
    }

    @Step("Click 'Move business unit acquirer down' button")
    public SuperGatewayPage clickMoveBusinessUnitAcquirerDownButton(String priority) {
        getRowByDataKey(priority).getByTestId("MoveMerchantAcquirerDownButton").click();

        return getCurrentPage();
    }

    @Step("Click 'Move business unit acquirer up' button")
    public SuperGatewayPage clickMoveBusinessUnitAcquirerUpButton(String priority) {
        getRowByDataKey(priority).getByTestId("MoveMerchantAcquirerUpButton").click();

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
