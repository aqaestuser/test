package xyz.npgw.test.page.system;

import xyz.npgw.test.page.base.BaseTrait;

@SuppressWarnings("unchecked")
public interface SelectAcquirerTrait<CurrentPageT> extends BaseTrait {

    default SelectAcquirerComponent<CurrentPageT> getSelectAcquirer() {
        return new SelectAcquirerComponent<>(getPage(), (CurrentPageT) this);
    }
}
