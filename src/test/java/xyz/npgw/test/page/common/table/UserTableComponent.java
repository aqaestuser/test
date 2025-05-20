package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.user.ChangeUserActivityDialog;
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

    @Step("Click 'Edit user'")
    public EditUserDialog clickEditUserButton(String email) {
        getRow(email).getByTestId("EditUserButton").click();

        return new EditUserDialog(getPage());
    }

    @Step("Click 'Deactivate user' button")
    public ChangeUserActivityDialog clickDeactivateUserButton(String email) {
        getRow(email).locator("//*[@data-icon='ban']/..").click();

        return new ChangeUserActivityDialog(getPage());
    }

    @Step("Click 'Activate user' button")
    public ChangeUserActivityDialog clickActivateUserButton(String email) {
        getRow(email).locator("//*[@data-icon='check']/..").click();

        return new ChangeUserActivityDialog(getPage());
    }

    @Step("Click 'Reset user password' button")
    public ResetUserPasswordDialog clickResetUserPasswordButton(String email) {
        getRow(email).getByTestId("ResetUserPasswordButton").click();

        return new ResetUserPasswordDialog(getPage());
    }

    public Locator getUserActivityIcon(String email) {
        return getRow(email).getByTestId("ChangeUserActivityButton").locator("svg");
    }

    @Step("Deactivate user")
    public TeamPage deactivateUser(String email) {
        return clickDeactivateUserButton(email)
                .clickDeactivateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickRefreshDataButton();
    }
}
