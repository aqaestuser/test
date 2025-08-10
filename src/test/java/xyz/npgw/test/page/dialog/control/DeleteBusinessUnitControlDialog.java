package xyz.npgw.test.page.dialog.control;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperFraudControlPage;

public class DeleteBusinessUnitControlDialog
        extends BaseDialog<SuperFraudControlPage, DeleteBusinessUnitControlDialog> {

    public DeleteBusinessUnitControlDialog(Page page) {
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
}
