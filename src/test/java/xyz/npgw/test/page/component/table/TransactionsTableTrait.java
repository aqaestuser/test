package xyz.npgw.test.page.component.table;

import xyz.npgw.test.page.base.BaseTrait;

@SuppressWarnings("unchecked")
public interface TransactionsTableTrait<CurrentPageT> extends BaseTrait {

    default TransactionsTableComponent<CurrentPageT> getTable() {
        return new TransactionsTableComponent<>(getPage(), (CurrentPageT) this);
    }
}
