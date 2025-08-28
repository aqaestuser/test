package xyz.npgw.test.page.component.select;

import xyz.npgw.test.page.base.BaseTrait;

@SuppressWarnings("unchecked")
public interface SelectAcquirerMidTrait<CurrentPageT> extends BaseTrait {

    default SelectAcquirerMidComponent<CurrentPageT> getSelectAcquirerMid() {
        return new SelectAcquirerMidComponent<>(getPage(), (CurrentPageT) this);
    }
}
