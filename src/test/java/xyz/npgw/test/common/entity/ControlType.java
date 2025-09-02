package xyz.npgw.test.common.entity;

import lombok.Getter;

@Getter
public enum ControlType {

    BIN_CHECK("BIN Check", "bin_check"),
    FRAUD_SCREEN("Fraud Screen", "fraud_screen");

    private final String displayText;
    private final String apiValue;

    ControlType(String displayText, String apiValue) {
        this.displayText = displayText;
        this.apiValue = apiValue;
    }

    @Override
    public String toString() {
        return apiValue;
    }
}
