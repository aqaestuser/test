package xyz.npgw.test.page.component.select;

import xyz.npgw.test.page.base.BaseTrait;

@SuppressWarnings("unchecked")
public interface SelectAcquirerTrait<CurrentPageT> extends BaseTrait {

    default SelectAcquirerComponent<CurrentPageT> getSelectAcquirerMid() {
        return new SelectAcquirerComponent<>(getPage(), (CurrentPageT) this);
    }
}
