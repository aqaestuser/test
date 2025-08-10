package xyz.npgw.test.page.component.table;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.system.SuperCompaniesAndBusinessUnitsPage;

public interface SuperBusinessUnitsTableTrait extends BaseTrait {

    default SuperBusinessUnitsTableComponent getTable() {
        return new SuperBusinessUnitsTableComponent(getPage(), (SuperCompaniesAndBusinessUnitsPage) this);
    }
}
