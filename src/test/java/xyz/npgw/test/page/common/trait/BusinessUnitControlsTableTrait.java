package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.BusinessUnitControlsTableComponent;
import xyz.npgw.test.page.system.FraudControlPage;

public interface BusinessUnitControlsTableTrait extends BaseTrait {

    default BusinessUnitControlsTableComponent getTableBusinessUnitControls() {
        return new BusinessUnitControlsTableComponent(getPage(), (FraudControlPage) this);
    }
}
