package xyz.npgw.test.page.dialog.merchant;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperCompaniesAndBusinessUnitsPage;

public class DeleteBusinessUnitDialog extends BaseDialog<SuperCompaniesAndBusinessUnitsPage, DeleteBusinessUnitDialog> {

    public DeleteBusinessUnitDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperCompaniesAndBusinessUnitsPage getReturnPage() {
        return new SuperCompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click 'Delete' button")
    public SuperCompaniesAndBusinessUnitsPage clickDeleteButton() {
        getByRole(AriaRole.BUTTON, "Delete").click();

        return new SuperCompaniesAndBusinessUnitsPage(getPage());
    }
}
