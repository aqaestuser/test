package xyz.npgw.test.page.common;

import xyz.npgw.test.page.base.BaseTrait;

public interface HeaderTrait extends BaseTrait {

    default HeaderComponent getHeader() {
        return new HeaderComponent(getPage());
    }
}
