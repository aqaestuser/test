package xyz.npgw.test.page.dialog.acquirer;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperAcquirersPage;

public class DeactivateAcquirerDialog extends BaseDialog<SuperAcquirersPage, DeactivateAcquirerDialog> {

    public DeactivateAcquirerDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperAcquirersPage getReturnPage() {
        return new SuperAcquirersPage(getPage());
    }

    @Step("Click 'Deactivate' button")
    public SuperAcquirersPage clickDeactivateButton() {
        getByRole(AriaRole.BUTTON, "Deactivate").click();

        return getReturnPage();
    }
}
