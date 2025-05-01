package xyz.npgw.test.common.entity;

public record BusinessUnit(String merchantId, String merchantName) {

    public BusinessUnit(String businessUnitName) {
        this(null, businessUnitName);
    }
}
