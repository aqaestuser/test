package xyz.npgw.test.page.dashboard;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.common.table.header.AdminHeaderMenuTrait;

public class AdminDashboardPage extends BaseDashboardPage<AdminDashboardPage>
        implements AdminHeaderMenuTrait<AdminDashboardPage> {

    public AdminDashboardPage(Page page) {
        super(page);
    }
}
