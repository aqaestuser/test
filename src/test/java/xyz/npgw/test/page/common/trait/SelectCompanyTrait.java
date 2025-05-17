package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.SelectCompanyComponent;

@SuppressWarnings("unchecked")
public interface SelectCompanyTrait<CurrentPageT> extends BaseTrait {

    default SelectCompanyComponent<CurrentPageT> getSelectCompany() {
        return new SelectCompanyComponent<>(getPage(), (CurrentPageT) this);
    }
}
