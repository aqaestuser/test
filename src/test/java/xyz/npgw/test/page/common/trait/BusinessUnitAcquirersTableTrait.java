package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.BusinessUnitAcquirersTableComponent;

public interface BusinessUnitAcquirersTableTrait extends BaseTrait {

    default BusinessUnitAcquirersTableComponent getTable() {
        return new BusinessUnitAcquirersTableComponent(getPage());
    }
}
