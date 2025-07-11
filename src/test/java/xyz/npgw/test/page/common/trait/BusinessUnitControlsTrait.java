package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.BusinessUnitControlsTableComponent;

public interface BusinessUnitControlsTrait extends BaseTrait {

    default BusinessUnitControlsTableComponent getBusinessUnitControlsTable() {
        return new BusinessUnitControlsTableComponent(getPage());
    }
}
