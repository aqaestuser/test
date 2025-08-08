package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.SuperBusinessUnitsTableComponent;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

public interface SuperBusinessUnitsTableTrait extends BaseTrait {

    default SuperBusinessUnitsTableComponent getTable() {
        return new SuperBusinessUnitsTableComponent(getPage(), (CompaniesAndBusinessUnitsPage) this);
    }
}
