package xyz.npgw.test.common.entity;

public record BusinessUnit(
        String merchantId,
        String merchantTitle) {

    public BusinessUnit(String businessUnitName) {
        this(null, businessUnitName);
    }
}
