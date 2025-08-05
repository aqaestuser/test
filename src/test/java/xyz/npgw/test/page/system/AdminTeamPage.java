package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.common.trait.SelectStatusTrait;
import xyz.npgw.test.page.common.trait.UsersTableTrait;

public class AdminTeamPage extends AdminSystemPage<AdminTeamPage>
        implements UsersTableTrait,
                   SelectStatusTrait<AdminTeamPage> {

    public AdminTeamPage(Page page) {
        super(page);
    }
}
