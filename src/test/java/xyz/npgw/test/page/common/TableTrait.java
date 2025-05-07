package xyz.npgw.test.page.common;

import xyz.npgw.test.page.base.BaseTrait;

public interface TableTrait extends BaseTrait {

    default TableComponent getTable() {
        return new TableComponent(getPage());
    }
}
