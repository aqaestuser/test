package xyz.npgw.test.page.common;

import xyz.npgw.test.page.base.BaseTrait;

@SuppressWarnings("unchecked")
public interface SelectCompanyTrait<CurrentPageT> extends BaseTrait {

    default SelectCompanyComponent<CurrentPageT> getSelectCompany() {
        return new SelectCompanyComponent<>(getPage(), (CurrentPageT) this);
    }
}
