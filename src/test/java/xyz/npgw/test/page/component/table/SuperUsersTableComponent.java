package xyz.npgw.test.page.component.table;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.dialog.user.SuperActivateUserDialog;
import xyz.npgw.test.page.dialog.user.SuperDeactivateUserDialog;
import xyz.npgw.test.page.dialog.user.SuperDeleteUserDialog;
import xyz.npgw.test.page.dialog.user.SuperEditUserDialog;
import xyz.npgw.test.page.dialog.user.SuperResetUserPasswordDialog;
import xyz.npgw.test.page.system.SuperTeamPage;

public class SuperUsersTableComponent extends UsersTableComponent<SuperTeamPage> {

    public SuperUsersTableComponent(Page page, SuperTeamPage currentPage) {
        super(page, currentPage);
    }

    public SuperEditUserDialog clickEditUserButton(String userEmail) {
        clickEditUser(userEmail);

        return new SuperEditUserDialog(getPage());
    }

    public SuperActivateUserDialog clickActivateUserButton(String userEmail) {
        clickActivateUser(userEmail);

        return new SuperActivateUserDialog(getPage());
    }

    public SuperDeactivateUserDialog clickDeactivateUserButton(String userEmail) {
        clickDeactivateUser(userEmail);

        return new SuperDeactivateUserDialog(getPage());
    }

    public SuperResetUserPasswordDialog clickResetUserPasswordIcon(String email) {
        clickResetUserPassword(email);

        return new SuperResetUserPasswordDialog(getPage());
    }

    public SuperDeleteUserDialog clickDeleteUserIcon(String userEmail) {
        getRow(userEmail).getByTestId("DeleteUserButton").click();

        return new SuperDeleteUserDialog(getPage());
    }
}
