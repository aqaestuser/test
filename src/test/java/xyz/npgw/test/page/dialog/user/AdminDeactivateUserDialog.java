package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.AdminTeamPage;

public class AdminDeactivateUserDialog extends BaseDialog<AdminTeamPage, AdminDeactivateUserDialog> {

    public AdminDeactivateUserDialog(Page page) {
        super(page);
    }

    @Override
    protected AdminTeamPage getReturnPage() {
        return new AdminTeamPage(getPage());
    }

    @Step("Click 'Deactivate' button")
    public AdminTeamPage clickDeactivateButton() {
        getByRole(AriaRole.BUTTON, "Deactivate").click();

        return getReturnPage();
    }
}
