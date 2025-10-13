package xyz.npgw.test.page.dialog.transactions;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;

public class RefundCapturedTransactionDialog
        extends BaseDialog<TransactionDetailsDialog, RefundCapturedTransactionDialog> {

    public RefundCapturedTransactionDialog(Page page) {
        super(page);
    }

    @Override
    protected TransactionDetailsDialog getReturnPage() {
        return new TransactionDetailsDialog(getPage());
    }

    @Step("Click 'Refund' button")
    public TransactionDetailsDialog clickRefundButton() {
        getByTextExact("Refund").click();

        return new TransactionDetailsDialog(getPage());
    }
}
