package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.DateRangePickerComponent;

@SuppressWarnings("unchecked")
public interface DateRangePickerTrait<CurrentPageT> extends BaseTrait {

    default DateRangePickerComponent<CurrentPageT> getDateRangePicker() {
        return new DateRangePickerComponent<>(getPage(), (CurrentPageT) this);
    }
}
