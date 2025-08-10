package xyz.npgw.test.page.component.table;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.system.SuperFraudControlPage;

public interface BusinessUnitControlsTableTrait extends BaseTrait {

    default BusinessUnitControlsTableComponent getTableBusinessUnitControls() {
        return new BusinessUnitControlsTableComponent(getPage(), (SuperFraudControlPage) this);
    }
}
