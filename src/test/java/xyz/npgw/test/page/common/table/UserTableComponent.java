package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.user.ChangeUserActivityDialog;
import xyz.npgw.test.page.dialog.user.DeleteUserDialog;
import xyz.npgw.test.page.dialog.user.EditUserDialog;
import xyz.npgw.test.page.dialog.user.ResetUserPasswordDialog;
import xyz.npgw.test.page.system.TeamPage;

public class UserTableComponent extends BaseTableComponent<TeamPage> {

    public UserTableComponent(Page page) {
        super(page);
    }

    @Override
    protected TeamPage getCurrentPage() {
        return new TeamPage(getPage());
    }

    @Step("Click 'Edit user' icon")
    public EditUserDialog clickEditUserIcon(String email) {
        getRow(email).getByTestId("EditUserButton").click();

        return new EditUserDialog(getPage());
    }

    @Step("Click 'Deactivate user' icon")
    public ChangeUserActivityDialog clickDeactivateUserIcon(String email) {
        getRow(email).locator("//*[@data-icon='ban']/..").click();

        return new ChangeUserActivityDialog(getPage());
    }

    @Step("Click 'Activate user' icon")
    public ChangeUserActivityDialog clickActivateUserIcon(String email) {
        getRow(email).locator("//*[@data-icon='check']/..").click();

        return new ChangeUserActivityDialog(getPage());
    }

    @Step("Click 'Reset user password' icon")
    public ResetUserPasswordDialog clickResetUserPasswordIcon(String email) {
        getRow(email).getByTestId("ResetUserPasswordButton").click();

        return new ResetUserPasswordDialog(getPage());
    }

    public Locator getUserActivityIcon(String email) {
        return getRow(email).getByTestId("ChangeUserActivityButton").locator("svg");
    }

    @Step("Click 'Delete user' button")
    public DeleteUserDialog clickDeleteUserIcon(String email) {
        getRow(email).getByTestId("DeleteUserButton").click();

        return new DeleteUserDialog(getPage());
    }
}
