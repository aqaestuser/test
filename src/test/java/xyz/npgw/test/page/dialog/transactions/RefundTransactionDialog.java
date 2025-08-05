package xyz.npgw.test.page.dialog.transactions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.transactions.SuperTransactionsPage;

@Getter
public class RefundTransactionDialog
        extends BaseDialog<SuperTransactionsPage, RefundTransactionDialog> {

    private final Locator amountToRefundInput = locator("[aria-roledescription='Number field']");
    private final Locator increaseAmountToRefundButton = getByLabelExact("Increase Amount to refund");

    public RefundTransactionDialog(Page page) {
        super(page);
    }

    public Locator getRefundMessage(String expectedText) {
        return getPage().locator("p:has-text('" + expectedText + "')");
    }

    @Override
    protected SuperTransactionsPage getReturnPage() {
        return new SuperTransactionsPage(getPage());
    }
}
