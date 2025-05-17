package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.SelectBusinessUnitComponent;

@SuppressWarnings("unchecked")
public interface SelectBusinessUnitTrait<CurrentPageT> extends BaseTrait {

    default SelectBusinessUnitComponent<CurrentPageT> getSelectBusinessUnit() {
        return new SelectBusinessUnitComponent<>(getPage(), (CurrentPageT) this);
    }
}
