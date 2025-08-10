package xyz.npgw.test.page.component.table;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.system.SuperTeamPage;

public interface SuperUsersTableTrait extends BaseTrait {

    default SuperUsersTableComponent getTable() {
        return new SuperUsersTableComponent(getPage(), (SuperTeamPage) this);
    }
}
