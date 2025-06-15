package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.TeamPage;

public class DeleteUserDialog extends BaseDialog<TeamPage, DeleteUserDialog> {

    private final Locator deleteButton = getByRole(AriaRole.BUTTON, "Delete");

    public DeleteUserDialog(Page page) {
        super(page);
    }

    @Override
    protected TeamPage getReturnPage() {
        return new TeamPage(getPage());
    }

    @Step("Click 'Delete' button")
    public TeamPage clickDeleteButton() {
        deleteButton.click();

        return new TeamPage(getPage());
    }
}
