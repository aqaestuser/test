package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperTeamPage;

public class SuperDeleteUserDialog extends BaseDialog<SuperTeamPage, SuperDeleteUserDialog> {

    public SuperDeleteUserDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperTeamPage getReturnPage() {
        return new SuperTeamPage(getPage());
    }

    @Step("Click 'Delete' button")
    public SuperTeamPage clickDeleteButton() {
        getByRole(AriaRole.BUTTON, "Delete").click();

        return getReturnPage();
    }
}
