package xyz.npgw.test.page.system;

import xyz.npgw.test.page.base.BaseTrait;

public interface MenuTrait extends BaseTrait {

    default MenuComponent getSystemMenu() {
        return new MenuComponent(getPage());
    }
}
