package xyz.npgw.test.page.dashboard;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.common.table.header.UserHeaderMenuTrait;

public class UserDashboardPage extends BaseDashboardPage<UserDashboardPage>
        implements UserHeaderMenuTrait<UserDashboardPage> {

    public UserDashboardPage(Page page) {
        super(page);
    }
}
