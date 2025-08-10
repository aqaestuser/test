package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.AdminTeamPage;

public class AdminDeleteUserDialog extends BaseDialog<AdminTeamPage, AdminDeleteUserDialog> {

    public AdminDeleteUserDialog(Page page) {
        super(page);
    }

    @Override
    protected AdminTeamPage getReturnPage() {
        return new AdminTeamPage(getPage());
    }

    @Step("Click 'Delete' button")
    public AdminTeamPage clickDeleteButton() {
        getByRole(AriaRole.BUTTON, "Delete").click();

        return getReturnPage();
    }
}
