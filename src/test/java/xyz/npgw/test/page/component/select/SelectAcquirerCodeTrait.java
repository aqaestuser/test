package xyz.npgw.test.page.component.select;

import xyz.npgw.test.page.base.BaseTrait;

@SuppressWarnings("unchecked")
public interface SelectAcquirerCodeTrait<CurrentPageT> extends BaseTrait {

    default SelectAcquirerCodeComponent<CurrentPageT> getSelectAcquirerCode() {
        return new SelectAcquirerCodeComponent<>(getPage(), (CurrentPageT) this);
    }
}
