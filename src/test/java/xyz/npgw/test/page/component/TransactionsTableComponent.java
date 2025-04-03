package xyz.npgw.test.page.component;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.base.BasePage;
import xyz.npgw.test.page.base.BaseTableComponent;

public class TransactionsTableComponent<T extends BasePage<T>> extends BaseTableComponent<T> {

    public TransactionsTableComponent(Page page, T owner) {
        super(page, owner);
    }
}
