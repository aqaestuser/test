package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.common.header.SuperHeaderMenuTrait;
import xyz.npgw.test.page.common.system.SuperSystemMenuTrait;
import xyz.npgw.test.page.common.trait.SelectDateRangeTrait;
import xyz.npgw.test.page.dialog.adjustment.AddAdjustmentDialog;

@Getter
public class TransactionManagementPage extends HeaderPage<TransactionManagementPage>
        implements SuperHeaderMenuTrait<TransactionManagementPage>,
                   SuperSystemMenuTrait,
                   SelectDateRangeTrait<TransactionManagementPage> {

    private final Locator addAdjustmentButton = getByTestId("AddAdjustmentButtonTransactionManagementPage");
    private final Locator transactionsTable = getByLabelExact("transactions table");

    public TransactionManagementPage(Page page) {
        super(page);
    }

    public AddAdjustmentDialog clickAddAdjustmentButton() {
        addAdjustmentButton.click();

        return new AddAdjustmentDialog(getPage());
    }
}
