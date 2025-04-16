package xyz.npgw.test.common.util;

public record Merchant(
        String merchantName,
        boolean usd,
        boolean eur,
        boolean active
) {
}
