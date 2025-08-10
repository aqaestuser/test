package xyz.npgw.test.page.dialog.gateway;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperGatewayPage;

public class DeactivateBusinessUnitAcquirerDialog
        extends BaseDialog<SuperGatewayPage, DeactivateBusinessUnitAcquirerDialog> {

    public DeactivateBusinessUnitAcquirerDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperGatewayPage getReturnPage() {
        return new SuperGatewayPage(getPage());
    }

    @Step("Click 'Deactivate' button")
    public SuperGatewayPage clickDeactivateButton() {
        getByRole(AriaRole.BUTTON, "Deactivate").click();

        return getReturnPage();
    }

    @Step("Click on 'Cancel' button ")
    public SuperGatewayPage clickCancelButton() {
        getByRole(AriaRole.BUTTON, "Cancel").click();

        return getReturnPage();
    }
}
