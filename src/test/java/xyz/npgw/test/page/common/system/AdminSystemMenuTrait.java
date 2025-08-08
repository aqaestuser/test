package xyz.npgw.test.page.common.system;

import xyz.npgw.test.page.base.BaseTrait;

public interface AdminSystemMenuTrait extends BaseTrait {

    default AdminSystemMenuComponent getSystemMenu() {
        return new AdminSystemMenuComponent(getPage());
    }
}
