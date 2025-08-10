package xyz.npgw.test.page.dialog.gateway;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperGatewayPage;

public class DeleteBusinessUnitAcquirerDialog extends BaseDialog<SuperGatewayPage, DeleteBusinessUnitAcquirerDialog> {

    public DeleteBusinessUnitAcquirerDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperGatewayPage getReturnPage() {
        return new SuperGatewayPage(getPage());
    }

    @Step("Click 'Delete' button")
    public SuperGatewayPage clickDeleteButton() {
        getByRole(AriaRole.BUTTON, "Delete").click();

        return new SuperGatewayPage(getPage());
    }
}
