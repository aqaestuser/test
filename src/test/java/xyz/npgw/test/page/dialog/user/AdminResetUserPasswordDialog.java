package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.system.AdminTeamPage;

public class AdminResetUserPasswordDialog
        extends ResetUserPasswordDialog<AdminTeamPage, AdminResetUserPasswordDialog> {

    public AdminResetUserPasswordDialog(Page page) {
        super(page);
    }

    @Override
    protected AdminTeamPage getReturnPage() {
        return new AdminTeamPage(getPage());
    }
}
