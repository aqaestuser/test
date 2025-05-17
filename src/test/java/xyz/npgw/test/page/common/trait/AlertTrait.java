package xyz.npgw.test.page.common.trait;

import xyz.npgw.test.page.base.BaseTrait;
import xyz.npgw.test.page.common.AlertComponent;

@SuppressWarnings("unchecked")
public interface AlertTrait<CurrentPageT> extends BaseTrait {

    default AlertComponent<CurrentPageT> getAlert() {
        return new AlertComponent<>(getPage(), (CurrentPageT) this);
    }
}
