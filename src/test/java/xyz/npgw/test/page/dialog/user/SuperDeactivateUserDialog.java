package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperTeamPage;

public class SuperDeactivateUserDialog extends BaseDialog<SuperTeamPage, SuperDeactivateUserDialog> {

    public SuperDeactivateUserDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperTeamPage getReturnPage() {
        return new SuperTeamPage(getPage());
    }

    @Step("Click 'Deactivate' button")
    public SuperTeamPage clickDeactivateButton() {
        getByRole(AriaRole.BUTTON, "Deactivate").click();

        return getReturnPage();
    }
}
