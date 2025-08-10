package xyz.npgw.test.page.component.select;

import xyz.npgw.test.page.base.BaseTrait;

@SuppressWarnings("unchecked")
public interface SelectDateRangeTrait<CurrentPageT> extends BaseTrait {

    default SelectDateRangeComponent<CurrentPageT> getSelectDateRange() {
        return new SelectDateRangeComponent<>(getPage(), (CurrentPageT) this);
    }
}
