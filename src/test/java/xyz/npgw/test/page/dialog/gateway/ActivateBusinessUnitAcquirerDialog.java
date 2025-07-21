package xyz.npgw.test.page.dialog.gateway;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.GatewayPage;

public class ActivateBusinessUnitAcquirerDialog extends BaseDialog<GatewayPage, ActivateBusinessUnitAcquirerDialog> {

    public ActivateBusinessUnitAcquirerDialog(Page page) {
        super(page);
    }

    @Override
    protected GatewayPage getReturnPage() {
        return new GatewayPage(getPage());
    }

    @Step("Click 'Activate' button")
    public GatewayPage clickActivateButton() {
        getByRole(AriaRole.BUTTON, "Activate").click();

        return getReturnPage();
    }
}
