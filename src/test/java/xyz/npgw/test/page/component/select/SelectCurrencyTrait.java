package xyz.npgw.test.page.component.select;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.base.HeaderPage;

@SuppressWarnings("unchecked")
public interface SelectCurrencyTrait<CurrentPageT extends HeaderPage<?>> extends BaseTrait {

    default SelectCurrencyComponent<CurrentPageT> getSelectCurrency() {
        return new SelectCurrencyComponent<>(getPage(), (CurrentPageT) this);
    }

}
