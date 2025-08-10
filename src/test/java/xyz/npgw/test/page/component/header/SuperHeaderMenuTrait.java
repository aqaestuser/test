package xyz.npgw.test.page.component.header;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.base.HeaderPage;

@SuppressWarnings("unchecked")
public interface SuperHeaderMenuTrait<CurrentPageT extends HeaderPage<?>> extends BaseTrait {

    default SuperHeaderMenuComponent<CurrentPageT> getHeader() {
        return new SuperHeaderMenuComponent<>(getPage(), (CurrentPageT) this);
    }

}
