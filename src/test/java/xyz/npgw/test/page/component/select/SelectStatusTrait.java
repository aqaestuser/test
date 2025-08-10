package xyz.npgw.test.page.component.select;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.base.HeaderPage;

@SuppressWarnings("unchecked")
public interface SelectStatusTrait<CurrentPageT extends HeaderPage<?>> extends BaseTrait {

    default SelectStatusComponent<CurrentPageT> getSelectStatus() {
        return new SelectStatusComponent<>(getPage(), (CurrentPageT) this);
    }

}
