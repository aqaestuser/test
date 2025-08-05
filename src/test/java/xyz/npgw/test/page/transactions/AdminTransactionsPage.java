package xyz.npgw.test.page.transactions;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.common.table.header.AdminHeaderMenuTrait;

public class AdminTransactionsPage extends BaseTransactionsPage<AdminTransactionsPage>
        implements AdminHeaderMenuTrait<AdminTransactionsPage> {

    public AdminTransactionsPage(Page page) {
        super(page);
    }
}
