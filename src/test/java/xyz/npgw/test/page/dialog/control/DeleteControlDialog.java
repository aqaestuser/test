package xyz.npgw.test.page.dialog.control;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperFraudControlPage;

public class DeleteControlDialog extends BaseDialog<SuperFraudControlPage, DeleteControlDialog> {

    private final Locator cancelButton = getDialog().getByText("Cancel");

    public DeleteControlDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperFraudControlPage getReturnPage() {
        return new SuperFraudControlPage(getPage());
    }

    @Step("Click 'Delete' button")
    public SuperFraudControlPage clickDeleteButton() {
        getByRole(AriaRole.BUTTON, "Delete").click();

        return getReturnPage();
    }

    @Step("Click on the 'Cancel' button to close form")
    public SuperFraudControlPage clickCancelButton() {
        cancelButton.click();

        return getReturnPage();
    }
}
