package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.FraudControlsTableComponent;

public interface FraudControlsTableTrait extends BaseTrait {

    default FraudControlsTableComponent getTableControls() {
        return new FraudControlsTableComponent(getPage());
    }
}
