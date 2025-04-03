package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.component.TransactionsTableComponent;

public abstract class BasePageWithTable<T extends BasePageWithTable<T>> extends BasePageWithHeader<T> {

    private BaseTableComponent<T> baseTableComponent;

    public BasePageWithTable(Page page) {
        super(page);
    }

    @SuppressWarnings("unchecked")
    public BaseTableComponent<T> getBaseTable() {
        if (baseTableComponent == null) {
            baseTableComponent = new TransactionsTableComponent<>(getPage(), (T) this);
        }
        return baseTableComponent;
    }
}
