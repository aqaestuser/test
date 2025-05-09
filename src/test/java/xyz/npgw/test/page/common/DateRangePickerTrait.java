package xyz.npgw.test.page.common;

import xyz.npgw.test.page.base.BaseTrait;

@SuppressWarnings("unchecked")
public interface DateRangePickerTrait<CurrentPageT> extends BaseTrait {

    default DateRangePickerComponent<CurrentPageT> getDateRangePicker() {
        return new DateRangePickerComponent<>(getPage(), (CurrentPageT) this);
    }
}
