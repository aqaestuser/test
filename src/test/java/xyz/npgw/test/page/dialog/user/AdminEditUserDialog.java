package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.system.AdminTeamPage;

public class AdminEditUserDialog extends EditUserDialog<AdminTeamPage, AdminEditUserDialog> {

    public AdminEditUserDialog(Page page) {
        super(page);
    }

    @Override
    protected AdminTeamPage getReturnPage() {
        return new AdminTeamPage(getPage());
    }
}
