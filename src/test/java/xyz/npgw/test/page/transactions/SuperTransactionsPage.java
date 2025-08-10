package xyz.npgw.test.page.transactions;

import com.microsoft.playwright.Page;
import lombok.Getter;
import xyz.npgw.test.page.component.header.SuperHeaderMenuTrait;
import xyz.npgw.test.page.component.select.SelectCompanyTrait;

@Getter
public class SuperTransactionsPage extends BaseTransactionsPage<SuperTransactionsPage>
        implements SelectCompanyTrait<SuperTransactionsPage>,
                   SuperHeaderMenuTrait<SuperTransactionsPage> {

    public SuperTransactionsPage(Page page) {
        super(page);
    }

}
