package xyz.npgw.test.page.dialog.acquirer;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.AcquirersPage;

public class DeleteAcquirerDialog extends BaseDialog<AcquirersPage, DeleteAcquirerDialog> {

    private final Locator deleteButton = getByRole(AriaRole.BUTTON, "Delete");

    public DeleteAcquirerDialog(Page page) {
        super(page);
    }

    @Override
    protected AcquirersPage getReturnPage() {
        return new AcquirersPage(getPage());
    }

    @Step("Click 'Delete' button")
    public AcquirersPage clickDeleteButton() {
        deleteButton.click();

        return new AcquirersPage(getPage());
    }
}
