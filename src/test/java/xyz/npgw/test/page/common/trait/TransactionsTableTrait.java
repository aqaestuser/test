package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.TransactionsTableComponent;

@SuppressWarnings("unchecked")
public interface TransactionsTableTrait<CurrentPageT> extends BaseTrait {

    default TransactionsTableComponent<CurrentPageT> getTable() {
        return new TransactionsTableComponent<>(getPage(), (CurrentPageT) this);
    }
}
