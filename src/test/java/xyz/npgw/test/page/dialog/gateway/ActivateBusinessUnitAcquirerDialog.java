package xyz.npgw.test.page.dialog.gateway;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperGatewayPage;

public class ActivateBusinessUnitAcquirerDialog
        extends BaseDialog<SuperGatewayPage, ActivateBusinessUnitAcquirerDialog> {

    public ActivateBusinessUnitAcquirerDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperGatewayPage getReturnPage() {
        return new SuperGatewayPage(getPage());
    }

    @Step("Click 'Activate' button")
    public SuperGatewayPage clickActivateButton() {
        getByRole(AriaRole.BUTTON, "Activate").click();

        return getReturnPage();
    }
}
