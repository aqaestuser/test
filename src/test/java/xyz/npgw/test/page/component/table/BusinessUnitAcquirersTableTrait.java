package xyz.npgw.test.page.component.table;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.system.SuperGatewayPage;

public interface BusinessUnitAcquirersTableTrait extends BaseTrait {

    default BusinessUnitAcquirersTableComponent getTable() {
        return new BusinessUnitAcquirersTableComponent(getPage(), (SuperGatewayPage) this);
    }
}
