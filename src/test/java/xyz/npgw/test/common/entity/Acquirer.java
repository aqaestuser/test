package xyz.npgw.test.common.entity;

public record Acquirer(
        String acquirerCode,
        String acquirerConfig,
        SystemConfig systemConfig,
        String acquirerName,
        String[] currencyList,
        boolean isActive) {

    public Acquirer() {
        this("", "", new SystemConfig(), "", new String[]{}, true);
    }
}
