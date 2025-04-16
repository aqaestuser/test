package xyz.npgw.test.common.util;

public record BusinessUnit(String[] currencyList, String merchantId, String merchantName, boolean isActive) {

    public BusinessUnit(String merchantName) {
        this(new String[]{}, null, merchantName, true);
    }
}
