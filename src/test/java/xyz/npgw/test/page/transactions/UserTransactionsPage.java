package xyz.npgw.test.page.transactions;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.common.table.header.UserHeaderMenuTrait;

public class UserTransactionsPage extends BaseTransactionsPage<UserTransactionsPage>
        implements UserHeaderMenuTrait<UserTransactionsPage> {

    public UserTransactionsPage(Page page) {
        super(page);
    }
}
