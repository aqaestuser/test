package xyz.npgw.test.page.component.table;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.system.SuperFraudControlPage;

public interface ControlsTableTrait extends BaseTrait {

    default ControlsTableComponent getTableControls() {
        return new ControlsTableComponent(getPage(), (SuperFraudControlPage) this);
    }
}
