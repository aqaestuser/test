package xyz.npgw.test.page.system;

import xyz.npgw.test.page.common.TableTrait;

public interface UserTableTrait extends TableTrait {

    default UserTableComponent getTable() {
        return new UserTableComponent(getPage());
    }
}
