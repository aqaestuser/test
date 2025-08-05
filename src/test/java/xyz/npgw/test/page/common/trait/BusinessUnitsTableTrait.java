package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.BusinessUnitsTableComponent;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

public interface BusinessUnitsTableTrait extends BaseTrait {

    default BusinessUnitsTableComponent getTable() {
        return new BusinessUnitsTableComponent(getPage(), (CompaniesAndBusinessUnitsPage) this);
    }
}
