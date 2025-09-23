package xyz.npgw.test.page.component.table;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.system.SuperGatewayPage;

public interface GatewayTableTrait extends BaseTrait {

    default GatewayTableComponent getTable() {
        return new GatewayTableComponent(getPage(), (SuperGatewayPage) this);
    }
}
