package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.TeamPage;

public class DeleteUserDialog extends BaseDialog<TeamPage, DeleteUserDialog> {

    public DeleteUserDialog(Page page) {
        super(page);
    }

    @Override
    protected TeamPage getReturnPage() {
        return new TeamPage(getPage());
    }

    @Step("Click 'Delete' button")
    public TeamPage clickDeleteButton() {
        getByRole(AriaRole.BUTTON, "Delete").click();

        return new TeamPage(getPage());
    }
}
