package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.GatewayTableComponent;

public interface GatewayTableTrait extends BaseTrait {

    default GatewayTableComponent getTable() {
        return new GatewayTableComponent(getPage());
    }
}
