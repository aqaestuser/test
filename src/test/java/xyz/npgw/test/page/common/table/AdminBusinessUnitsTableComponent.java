package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.system.AdminBusinessUnitsPage;

public class AdminBusinessUnitsTableComponent extends BaseTableComponent<AdminBusinessUnitsPage> {

    public AdminBusinessUnitsTableComponent(Page page, AdminBusinessUnitsPage currentPage) {
        super(page, currentPage);
    }

    @Override
    protected AdminBusinessUnitsPage getCurrentPage() {
        return new AdminBusinessUnitsPage(getPage());
    }
}
