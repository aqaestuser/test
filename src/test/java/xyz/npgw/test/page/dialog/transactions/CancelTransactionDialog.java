package xyz.npgw.test.page.dialog.transactions;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.transactions.SuperTransactionsPage;

public class CancelTransactionDialog
        extends BaseDialog<SuperTransactionsPage, CancelTransactionDialog> {

    public CancelTransactionDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperTransactionsPage getReturnPage() {
        return new SuperTransactionsPage(getPage());
    }

    @Step("Click 'Cancel' button")
    public SuperTransactionsPage clickCancelButton() {
        getByTextExact("Cancel").click();

        return new SuperTransactionsPage(getPage());
    }
}
