package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.table.FraudControlTableComponent;

public interface FraudControlsTrait extends BaseTrait {

    default FraudControlTableComponent getTable() {
        return new FraudControlTableComponent(getPage());
    }
}
