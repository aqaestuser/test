package xyz.npgw.test.common.entity;

import lombok.Getter;

@Getter
public enum ControlType {

    BIN_CHECK("BIN Check"),
    FRAUD_SCREEN("Fraud Screen");

    private final String displayText;

    ControlType(String displayText) {
        this.displayText = displayText;
    }
}
