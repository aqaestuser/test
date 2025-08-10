package xyz.npgw.test.page.component.table;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.system.SuperAcquirersPage;

public interface AcquirersTableTrait extends BaseTrait {

    default AcquirersTableComponent getTable() {
        return new AcquirersTableComponent(getPage(), (SuperAcquirersPage) this);
    }
}
