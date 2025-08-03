package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.SelectAcquirerComponent;

@SuppressWarnings("unchecked")
public interface SelectAcquirerTrait<CurrentPageT> extends BaseTrait {

    default SelectAcquirerComponent<CurrentPageT> getSelectAcquirerMid() {
        return new SelectAcquirerComponent<>(getPage(), (CurrentPageT) this);
    }
}
