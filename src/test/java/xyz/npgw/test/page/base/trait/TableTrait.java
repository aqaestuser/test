package xyz.npgw.test.page.base.trait;

import xyz.npgw.test.component.TableComponent;

public interface TableTrait extends BaseTrait {

    default TableComponent getTable() {
        return new TableComponent(getPage());
    }
}
