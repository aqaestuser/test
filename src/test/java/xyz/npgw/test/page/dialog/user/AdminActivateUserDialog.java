package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.AdminTeamPage;

public class AdminActivateUserDialog extends BaseDialog<AdminTeamPage, AdminActivateUserDialog> {

    public AdminActivateUserDialog(Page page) {
        super(page);
    }

    @Step("Click 'Activate' button")
    public AdminTeamPage clickActivateButton() {
        getByRole(AriaRole.BUTTON, "Activate").click();

        return getReturnPage();
    }

    @Override
    protected AdminTeamPage getReturnPage() {
        return new AdminTeamPage(getPage());
    }
}
