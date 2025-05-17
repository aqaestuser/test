package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.MenuComponent;

public interface MenuTrait extends BaseTrait {

    default MenuComponent getSystemMenu() {
        return new MenuComponent(getPage());
    }
}
