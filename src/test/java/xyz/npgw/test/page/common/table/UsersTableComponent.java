package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.user.ActivateUserDialog;
import xyz.npgw.test.page.dialog.user.DeactivateUserDialog;
import xyz.npgw.test.page.dialog.user.DeleteUserDialog;
import xyz.npgw.test.page.dialog.user.EditUserDialog;
import xyz.npgw.test.page.dialog.user.ResetUserPasswordDialog;
import xyz.npgw.test.page.system.SuperTeamPage;

public class UsersTableComponent extends BaseTableComponent<SuperTeamPage> {

    public UsersTableComponent(Page page, SuperTeamPage currentPage) {
        super(page, currentPage);
    }

    @Override
    protected SuperTeamPage getCurrentPage() {
        return new SuperTeamPage(getPage());
    }

    public Locator getUserActivityIcon(String userEmail) {
        return getRow(userEmail).getByTestId("ChangeUserActivityButton").locator("svg");
    }

    @Step("Click 'Edit user' button")
    public EditUserDialog clickEditUserButton(String userEmail) {
        getRow(userEmail).getByTestId("EditUserButton").click();

        return new EditUserDialog(getPage());
    }

    @Step("Click 'Activate user' button")
    public ActivateUserDialog clickActivateUserButton(String userEmail) {
        getRow(userEmail).locator("//*[@data-icon='check']/..").click();

        return new ActivateUserDialog(getPage());
    }

    @Step("Click 'Deactivate user' button")
    public DeactivateUserDialog clickDeactivateUserButton(String userEmail) {
        getRow(userEmail).locator("//*[@data-icon='ban']/..").click();

        return new DeactivateUserDialog(getPage());
    }

    @Step("Click 'Reset user password' button")
    public ResetUserPasswordDialog clickResetUserPasswordIcon(String email) {
        getRow(email).getByTestId("ResetUserPasswordButton").click();

        return new ResetUserPasswordDialog(getPage());
    }

    @Step("Click 'Delete user' button")
    public DeleteUserDialog clickDeleteUserIcon(String userEmail) {
        getRow(userEmail).getByTestId("DeleteUserButton").click();

        return new DeleteUserDialog(getPage());
    }
}
