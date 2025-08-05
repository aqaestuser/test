package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.AddAdjustmentTableComponent;
import xyz.npgw.test.page.dialog.adjustment.AddAdjustmentDialog;

public interface AddAdjustmentTableTrait extends BaseTrait {

    default AddAdjustmentTableComponent getTable() {
        return new AddAdjustmentTableComponent(getPage(), (AddAdjustmentDialog) this);
    }
}
