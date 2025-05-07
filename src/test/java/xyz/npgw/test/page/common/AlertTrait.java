package xyz.npgw.test.page.common;

import xyz.npgw.test.page.base.BaseTrait;

@SuppressWarnings("unchecked")
public interface AlertTrait<CurrentPageT> extends BaseTrait {

    default AlertComponent<CurrentPageT> getAlert() {
        return new AlertComponent<>(getPage(), (CurrentPageT) this);
    }
}
