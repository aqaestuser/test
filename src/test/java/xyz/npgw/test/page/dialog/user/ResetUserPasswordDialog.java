package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.TeamPage;

public class ResetUserPasswordDialog extends BaseDialog<TeamPage, ResetUserPasswordDialog> {

    public ResetUserPasswordDialog(Page page) {
        super(page);
    }

    @Override
    protected TeamPage getReturnPage() {
        return new TeamPage(getPage());
    }

    @Step("Enter new password in the 'New password' field")
    public ResetUserPasswordDialog fillPasswordField(String newPassword) {
        getByPlaceholder("Enter new password").fill(newPassword);

        return this;
    }

    @Step("Click 'Reset' button")
    public TeamPage clickResetButton() {
        getByRole(AriaRole.BUTTON, "Reset").click();

        return new TeamPage(getPage());
    }
}
