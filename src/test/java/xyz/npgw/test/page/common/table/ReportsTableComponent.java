package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.ReportsPage;

public class ReportsTableComponent extends BaseTableComponent<ReportsPage> {

    public ReportsTableComponent(Page page) {
        super(page);
    }

    @Override
    protected ReportsPage getCurrentPage() {
        return new ReportsPage(getPage());
    }
}
