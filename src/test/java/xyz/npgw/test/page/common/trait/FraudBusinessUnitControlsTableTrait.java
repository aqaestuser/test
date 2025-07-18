package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.FraudBusinessUnitControlsTableComponent;

public interface FraudBusinessUnitControlsTableTrait extends BaseTrait {

    default FraudBusinessUnitControlsTableComponent getTableBusinessUnitControls() {
        return new FraudBusinessUnitControlsTableComponent(getPage());
    }
}
