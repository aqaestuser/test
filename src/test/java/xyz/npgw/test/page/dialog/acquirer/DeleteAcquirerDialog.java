package xyz.npgw.test.page.dialog.acquirer;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperAcquirersPage;

public class DeleteAcquirerDialog extends BaseDialog<SuperAcquirersPage, DeleteAcquirerDialog> {

    private final Locator deleteButton = getByRole(AriaRole.BUTTON, "Delete");

    public DeleteAcquirerDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperAcquirersPage getReturnPage() {
        return new SuperAcquirersPage(getPage());
    }

    @Step("Click 'Delete' button")
    public SuperAcquirersPage clickDeleteButton() {
        deleteButton.click();

        return getReturnPage();
    }
}
