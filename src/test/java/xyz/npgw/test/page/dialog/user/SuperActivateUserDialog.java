package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperTeamPage;

public class SuperActivateUserDialog extends BaseDialog<SuperTeamPage, SuperActivateUserDialog> {

    public SuperActivateUserDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperTeamPage getReturnPage() {
        return new SuperTeamPage(getPage());
    }

    @Step("Click 'Activate' button")
    public SuperTeamPage clickActivateButton() {
        getByRole(AriaRole.BUTTON, "Activate").click();

        return getReturnPage();
    }
}
