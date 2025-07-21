package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.BusinessUnitControlsTableComponent;

public interface BusinessUnitControlsTableTrait extends BaseTrait {

    default BusinessUnitControlsTableComponent getTableBusinessUnitControls() {
        return new BusinessUnitControlsTableComponent(getPage());
    }
}
