package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.SelectDateRangeComponent;

@SuppressWarnings("unchecked")
public interface SelectDateRangeTrait<CurrentPageT> extends BaseTrait {

    default SelectDateRangeComponent<CurrentPageT> getSelectDateRange() {
        return new SelectDateRangeComponent<>(getPage(), (CurrentPageT) this);
    }
}
