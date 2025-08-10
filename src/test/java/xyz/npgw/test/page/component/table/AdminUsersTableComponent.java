package xyz.npgw.test.page.component.table;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.dialog.user.AdminActivateUserDialog;
import xyz.npgw.test.page.dialog.user.AdminDeactivateUserDialog;
import xyz.npgw.test.page.dialog.user.AdminDeleteUserDialog;
import xyz.npgw.test.page.dialog.user.AdminEditUserDialog;
import xyz.npgw.test.page.dialog.user.AdminResetUserPasswordDialog;
import xyz.npgw.test.page.system.AdminTeamPage;

public class AdminUsersTableComponent extends UsersTableComponent<AdminTeamPage> {

    public AdminUsersTableComponent(Page page, AdminTeamPage currentPage) {
        super(page, currentPage);
    }

    public AdminEditUserDialog clickEditUserButton(String userEmail) {
        clickEditUser(userEmail);

        return new AdminEditUserDialog(getPage());
    }

    public AdminActivateUserDialog clickActivateUserButton(String userEmail) {
        clickActivateUser(userEmail);

        return new AdminActivateUserDialog(getPage());
    }

    public AdminDeactivateUserDialog clickDeactivateUserButton(String userEmail) {
        clickDeactivateUser(userEmail);

        return new AdminDeactivateUserDialog(getPage());
    }

    public AdminResetUserPasswordDialog clickResetUserPasswordIcon(String email) {
        clickResetUserPassword(email);

        return new AdminResetUserPasswordDialog(getPage());
    }

    public AdminDeleteUserDialog clickDeleteUserIcon(String userEmail) {
        getRow(userEmail).getByTestId("DeleteUserButton").click();

        return new AdminDeleteUserDialog(getPage());
    }

}
