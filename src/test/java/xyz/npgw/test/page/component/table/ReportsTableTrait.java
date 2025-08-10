package xyz.npgw.test.page.component.table;

import xyz.npgw.test.page.ReportsPage;
import xyz.npgw.test.page.base.BaseTrait;

public interface ReportsTableTrait extends BaseTrait {

    default ReportsTableComponent getTable() {
        return new ReportsTableComponent(getPage(), (ReportsPage) this);
    }
}
