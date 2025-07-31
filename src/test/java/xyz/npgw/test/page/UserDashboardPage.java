package xyz.npgw.test.page;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.SelectBusinessUnitTrait;
import xyz.npgw.test.page.common.trait.SelectDateRangeTrait;
import xyz.npgw.test.page.common.trait.UserHeaderTrait;

public final class UserDashboardPage extends DashboardPage<UserDashboardPage> implements
        UserHeaderTrait<UserDashboardPage>,
        SelectBusinessUnitTrait<UserDashboardPage>,
        SelectDateRangeTrait<UserDashboardPage>,
        AlertTrait<UserDashboardPage> {

    public UserDashboardPage(Page page) {
        super(page);
    }

    @Override
    protected UserDashboardPage getReturnPage() {
        return this;
    }
}
