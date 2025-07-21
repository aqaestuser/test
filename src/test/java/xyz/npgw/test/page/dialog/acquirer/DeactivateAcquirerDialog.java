package xyz.npgw.test.page.dialog.acquirer;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.AcquirersPage;

public class DeactivateAcquirerDialog extends BaseDialog<AcquirersPage, DeactivateAcquirerDialog> {

    public DeactivateAcquirerDialog(Page page) {
        super(page);
    }

    @Override
    protected AcquirersPage getReturnPage() {
        return new AcquirersPage(getPage());
    }

    @Step("Click 'Deactivate' button")
    public AcquirersPage clickDeactivateButton() {
        getByRole(AriaRole.BUTTON, "Deactivate").click();

        return getReturnPage();
    }
}
