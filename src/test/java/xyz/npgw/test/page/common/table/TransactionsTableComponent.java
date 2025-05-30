package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.dialog.TransactionDetailsDialog;

public class TransactionsTableComponent extends BaseTableComponent<TransactionsPage> {

    public TransactionsTableComponent(Page page) {
        super(page);
    }

    @Override
    protected TransactionsPage getCurrentPage() {
        return new TransactionsPage(getPage());
    }

    public TransactionDetailsDialog clickOnTransaction() {
        getFirstRowCell("NPGW reference").click();

        return new TransactionDetailsDialog(getPage());
    }
}
