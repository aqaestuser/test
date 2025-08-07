package xyz.npgw.test.page.common.header;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.base.HeaderPage;

@SuppressWarnings("unchecked")
public interface UserHeaderMenuTrait<CurrentPageT extends HeaderPage<?>> extends BaseTrait {

    default UserHeaderMenuComponent<CurrentPageT> getHeader() {
        return new UserHeaderMenuComponent<>(getPage(), (CurrentPageT) this);
    }

}
