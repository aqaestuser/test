package xyz.npgw.test.page.base.trait;

import xyz.npgw.test.component.HeaderComponent;

public interface HeaderTrait extends BaseTrait {

    default HeaderComponent getHeader() {
        return new HeaderComponent(getPage());
    }
}
