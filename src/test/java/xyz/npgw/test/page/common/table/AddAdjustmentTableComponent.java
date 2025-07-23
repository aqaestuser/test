package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import xyz.npgw.test.page.dialog.adjustment.AddAdjustmentDialog;

public class AddAdjustmentTableComponent extends BaseTableComponent<AddAdjustmentDialog> {

    public AddAdjustmentTableComponent(Page page) {
        super(page, page.getByRole(AriaRole.DIALOG));
    }

    @Override
    protected AddAdjustmentDialog getCurrentPage() {
        return new AddAdjustmentDialog(getPage());
    }

    public AddAdjustmentDialog clickTransaction() {
        getFirstRow().click();

        return getCurrentPage();
    }
}
