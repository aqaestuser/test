package xyz.npgw.test.page.base.trait;

import xyz.npgw.test.component.SystemMenuComponent;

public interface SystemMenuTrait extends BaseTrait {

    default SystemMenuComponent getSystemMenu() {
        return new SystemMenuComponent(getPage());
    }
}
