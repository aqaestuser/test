package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.component.header.AdminHeaderMenuTrait;
import xyz.npgw.test.page.component.system.AdminSystemMenuTrait;
import xyz.npgw.test.page.component.table.AdminUsersTableTrait;
import xyz.npgw.test.page.dialog.user.AdminAddUserDialog;

public class AdminTeamPage extends BaseTeamPage<AdminTeamPage>
        implements AdminHeaderMenuTrait<AdminTeamPage>,
                   AdminSystemMenuTrait,
                   AdminUsersTableTrait {

    public AdminTeamPage(Page page) {
        super(page);
    }

    public AdminAddUserDialog clickAddUserButton() {
        clickAddUser();

        return new AdminAddUserDialog(getPage());
    }
}
