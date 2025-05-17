package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.HeaderComponent;

public interface HeaderTrait extends BaseTrait {

    default HeaderComponent getHeader() {
        return new HeaderComponent(getPage());
    }
}
