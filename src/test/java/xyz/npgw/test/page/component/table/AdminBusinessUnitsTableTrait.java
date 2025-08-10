package xyz.npgw.test.page.component.table;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.system.AdminBusinessUnitsPage;

public interface AdminBusinessUnitsTableTrait extends BaseTrait {

    default AdminBusinessUnitsTableComponent getTable() {
        return new AdminBusinessUnitsTableComponent(getPage(), (AdminBusinessUnitsPage) this);
    }
}
