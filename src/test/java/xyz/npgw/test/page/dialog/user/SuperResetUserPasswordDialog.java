package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.system.SuperTeamPage;

public class SuperResetUserPasswordDialog
        extends ResetUserPasswordDialog<SuperTeamPage, SuperResetUserPasswordDialog> {

    public SuperResetUserPasswordDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperTeamPage getReturnPage() {
        return new SuperTeamPage(getPage());
    }
}
