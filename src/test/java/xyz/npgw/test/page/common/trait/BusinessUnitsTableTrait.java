package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.BusinessUnitsTableComponent;

public interface BusinessUnitsTableTrait extends BaseTrait {

    default BusinessUnitsTableComponent getTable() {
        return new BusinessUnitsTableComponent(getPage());
    }
}
