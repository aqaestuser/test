package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.MenuTrait;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;
import xyz.npgw.test.page.common.trait.SelectStatusTrait;
import xyz.npgw.test.page.common.trait.UsersTableTrait;

public final class SuperTeamPage extends TeamPage<SuperTeamPage> implements
        MenuTrait,
        SelectStatusTrait<SuperTeamPage>,
        AlertTrait<SuperTeamPage>,
        UsersTableTrait<SuperTeamPage>,
        SelectCompanyTrait<SuperTeamPage> {

    public SuperTeamPage(Page page) {
        super(page);
    }

    @Override
    protected SuperTeamPage getReturnPage() {
        return this;
    }
}
