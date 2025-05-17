package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.UserTableComponent;

public interface UserTableTrait extends BaseTrait {

    default UserTableComponent getTable() {
        return new UserTableComponent(getPage());
    }
}
