package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BasePage;
import xyz.npgw.test.page.dialog.BaseDialog;

public abstract class ResetUserPasswordDialog<
        ReturnPageT extends BasePage,
        CurrentDialogT extends ResetUserPasswordDialog<ReturnPageT, CurrentDialogT>>
        extends BaseDialog<ReturnPageT, CurrentDialogT> {

    public ResetUserPasswordDialog(Page page) {
        super(page);
    }

    @Step("Enter new password in the 'New password' field")
    public CurrentDialogT fillPasswordField(String newPassword) {
        getByPlaceholder("Enter new password").fill(newPassword);

        return getCurrentDialog();
    }

    @Step("Click 'Reset' button")
    public ReturnPageT clickResetButton() {
        getByRole(AriaRole.BUTTON, "Reset").click();

        return getReturnPage();
    }
}
