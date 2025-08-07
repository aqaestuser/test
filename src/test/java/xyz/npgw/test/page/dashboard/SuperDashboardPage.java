package xyz.npgw.test.page.dashboard;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.common.header.SuperHeaderMenuTrait;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;

public final class SuperDashboardPage extends BaseDashboardPage<SuperDashboardPage>
        implements SelectCompanyTrait<SuperDashboardPage>,
                   SuperHeaderMenuTrait<SuperDashboardPage> {

    public SuperDashboardPage(Page page) {
        super(page);
    }
}
