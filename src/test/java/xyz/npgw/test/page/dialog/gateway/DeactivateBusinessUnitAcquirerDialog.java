package xyz.npgw.test.page.dialog.gateway;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.GatewayPage;

public class DeactivateBusinessUnitAcquirerDialog
        extends BaseDialog<GatewayPage, DeactivateBusinessUnitAcquirerDialog> {

    public DeactivateBusinessUnitAcquirerDialog(Page page) {
        super(page);
    }

    @Override
    protected GatewayPage getReturnPage() {
        return new GatewayPage(getPage());
    }

    @Step("Click 'Deactivate' button")
    public GatewayPage clickDeactivateButton() {
        getByRole(AriaRole.BUTTON, "Deactivate").click();

        return getReturnPage();
    }

    @Step("Click on 'Cancel' button ")
    public GatewayPage clickCancelButton() {
        getByRole(AriaRole.BUTTON, "Cancel").click();

        return getReturnPage();
    }
}
