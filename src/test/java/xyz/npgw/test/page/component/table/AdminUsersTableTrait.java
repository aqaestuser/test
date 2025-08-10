package xyz.npgw.test.page.component.table;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.system.AdminTeamPage;

public interface AdminUsersTableTrait extends BaseTrait {

    default AdminUsersTableComponent getTable() {
        return new AdminUsersTableComponent(getPage(), (AdminTeamPage) this);
    }
}
