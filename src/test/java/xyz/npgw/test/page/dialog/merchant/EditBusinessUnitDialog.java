package xyz.npgw.test.page.dialog.merchant;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

public class EditBusinessUnitDialog extends BaseDialog<CompaniesAndBusinessUnitsPage, EditBusinessUnitDialog> {

    public EditBusinessUnitDialog(Page page) {
        super(page);
    }

    @Override
    protected CompaniesAndBusinessUnitsPage getReturnPage() {
        return new CompaniesAndBusinessUnitsPage(getPage());
    }
}
