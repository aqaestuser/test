package xyz.npgw.test.page.common.system;

import xyz.npgw.test.page.base.BaseTrait;

public interface SuperSystemMenuTrait extends BaseTrait {

    default SuperSystemMenuComponent getSystemMenu() {
        return new SuperSystemMenuComponent(getPage());
    }
}
