package xyz.npgw.test.page.dialog.acquirer;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperAcquirersPage;

public class ActivateAcquirerDialog extends BaseDialog<SuperAcquirersPage, ActivateAcquirerDialog> {

    public ActivateAcquirerDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperAcquirersPage getReturnPage() {
        return new SuperAcquirersPage(getPage());
    }

    @Step("Click 'Activate' button")
    public SuperAcquirersPage clickActivateButton() {
        getByRole(AriaRole.BUTTON, "Activate").click();

        return getReturnPage();
    }
}
