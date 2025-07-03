package xyz.npgw.test.page.dialog.acquirer;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.GatewayPage;

public class DeleteBusinessUnitAcquirerDialog extends BaseDialog<GatewayPage, DeleteBusinessUnitAcquirerDialog> {

    public DeleteBusinessUnitAcquirerDialog(Page page) {
        super(page);
    }

    @Override
    protected GatewayPage getReturnPage() {
        return new GatewayPage(getPage());
    }

    @Step("Click 'Delete' button")
    public GatewayPage clickDeleteButton() {
        getByRole(AriaRole.BUTTON, "Delete").click();

        return new GatewayPage(getPage());
    }
}
