package xyz.npgw.test.page.common.header;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.base.HeaderPage;

@SuppressWarnings("unchecked")
public interface AdminHeaderMenuTrait<CurrentPageT extends HeaderPage<?>> extends BaseTrait {

    default AdminHeaderMenuComponent<CurrentPageT> getHeader() {
        return new AdminHeaderMenuComponent<>(getPage(), (CurrentPageT) this);
    }

}
