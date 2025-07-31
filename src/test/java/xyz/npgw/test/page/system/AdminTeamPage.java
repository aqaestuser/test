package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.common.trait.AdminHeaderTrait;
import xyz.npgw.test.page.common.trait.AdminMenuTrait;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.SelectStatusTrait;
import xyz.npgw.test.page.common.trait.UsersTableTrait;

public final class AdminTeamPage extends TeamPage<AdminTeamPage> implements
        AdminHeaderTrait<AdminTeamPage>,
        AdminMenuTrait,
        SelectStatusTrait<AdminTeamPage>,
        AlertTrait<AdminTeamPage>,
        UsersTableTrait<AdminTeamPage> {

    public AdminTeamPage(Page page) {
        super(page);
    }

    @Override
    protected AdminTeamPage getReturnPage() {
        return this;
    }
}
