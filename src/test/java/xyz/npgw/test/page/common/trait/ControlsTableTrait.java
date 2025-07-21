package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.ControlsTableComponent;

public interface ControlsTableTrait extends BaseTrait {

    default ControlsTableComponent getTableControls() {
        return new ControlsTableComponent(getPage());
    }
}
