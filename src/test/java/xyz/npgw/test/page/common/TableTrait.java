package xyz.npgw.test.page.common;

import xyz.npgw.test.page.base.BaseTrait;

public interface TableTrait<CurrentPageT> extends BaseTrait {

    default TableComponent<CurrentPageT> getTable() {
        return new TableComponent<>(getPage(), (CurrentPageT) this);
    }
}
