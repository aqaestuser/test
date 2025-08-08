package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.common.header.AdminHeaderMenuTrait;
import xyz.npgw.test.page.common.system.AdminSystemMenuTrait;

public class AdminTeamPage extends BaseTeamPage<AdminTeamPage>
        implements AdminHeaderMenuTrait<AdminTeamPage>,
                   AdminSystemMenuTrait {

    public AdminTeamPage(Page page) {
        super(page);
    }
}
