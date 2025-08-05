package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.AcquirersTableComponent;
import xyz.npgw.test.page.system.AcquirersPage;

public interface AcquirersTableTrait extends BaseTrait {

    default AcquirersTableComponent getTable() {
        return new AcquirersTableComponent(getPage(), (AcquirersPage) this);
    }
}
