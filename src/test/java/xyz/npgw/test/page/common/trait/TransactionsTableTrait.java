package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.TransactionsTableComponent;

public interface TransactionsTableTrait extends BaseTrait {

    default TransactionsTableComponent getTable() {
        return new TransactionsTableComponent(getPage());
    }
}
