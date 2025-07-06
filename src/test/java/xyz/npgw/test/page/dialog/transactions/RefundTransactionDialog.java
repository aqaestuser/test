package xyz.npgw.test.page.dialog.transactions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.dialog.BaseDialog;

@Getter
public class RefundTransactionDialog extends BaseDialog<TransactionsPage, RefundTransactionDialog> {

    private final Locator amountToRefundInput = locator("[aria-roledescription='Number field']");
    private final Locator increaseAmountToRefundButton = getByLabelExact("Increase Amount to refund");

    public RefundTransactionDialog(Page page) {
        super(page);
    }

    @Override
    protected TransactionsPage getReturnPage() {
        return new TransactionsPage(getPage());
    }

    public Locator getRefundMessage(String expectedText) {
        return getPage().locator("p:has-text('" + expectedText + "')");
    }
}
