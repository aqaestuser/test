package xyz.npgw.test.page.dialog.merchant;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

public class DeleteBusinessUnitDialog extends BaseDialog<CompaniesAndBusinessUnitsPage, DeleteBusinessUnitDialog> {

    public DeleteBusinessUnitDialog(Page page) {
        super(page);
    }

    @Override
    protected CompaniesAndBusinessUnitsPage getReturnPage() {
        return new CompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click 'Delete' button")
    public CompaniesAndBusinessUnitsPage clickDeleteButton() {
        getByRole(AriaRole.BUTTON, "Delete").click();

        return new CompaniesAndBusinessUnitsPage(getPage());
    }
}
