package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.dialog.adjustment.AddAdjustmentDialog;

@Getter
public class TransactionManagementPage extends BaseSystemPage<TransactionManagementPage>
        implements AlertTrait<TransactionsPage> {

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
