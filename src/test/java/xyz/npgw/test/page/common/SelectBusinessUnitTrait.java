package xyz.npgw.test.page.common;

import xyz.npgw.test.page.base.BaseTrait;

@SuppressWarnings("unchecked")
public interface SelectBusinessUnitTrait<CurrentPageT> extends BaseTrait {
    default SelectBusinessUnitComponent<CurrentPageT> getSelectBusinessUnit() {
        return new SelectBusinessUnitComponent<>(getPage(), (CurrentPageT) this);
    }
}
