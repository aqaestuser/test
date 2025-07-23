package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.AddAdjustmentTableComponent;

public interface AddAdjustmentTableTrait extends BaseTrait {

    default AddAdjustmentTableComponent getTable() {
        return new AddAdjustmentTableComponent(getPage());
    }
}
