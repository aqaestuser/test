package xyz.npgw.test.page.component.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public abstract class UsersTableComponent<CurrentPageT> extends BaseTableComponent<CurrentPageT> {

    public UsersTableComponent(Page page, CurrentPageT currentPage) {
        super(page, currentPage);
    }

    public Locator getUserActivityIcon(String userEmail) {
        return getRow(userEmail).getByTestId("ChangeUserActivityButton").locator("svg");
    }

    public Locator getRowIcon(String userEmail) {
        return getRow(userEmail).locator("svg");
    }

    @Step("Click 'Edit user' button")
    public void clickEditUser(String userEmail) {
        getRow(userEmail).getByTestId("EditUserButton").click();
    }

    @Step("Click 'Activate user' button")
    public void clickActivateUser(String userEmail) {
        getRow(userEmail).locator("//*[@data-icon='check']/..").click();
    }

    @Step("Click 'Deactivate user' button")
    public void clickDeactivateUser(String userEmail) {
        getRow(userEmail).locator("//*[@data-icon='ban']/..").click();
    }

    @Step("Click 'Reset user password' button")
    public void clickResetUserPassword(String email) {
        getRow(email).getByTestId("ResetUserPasswordButton").click();
    }

    @Step("Click 'Delete user' button")
    public void clickDeleteUser(String userEmail) {
        getRow(userEmail).getByTestId("DeleteUserButton").click();
    }
}
