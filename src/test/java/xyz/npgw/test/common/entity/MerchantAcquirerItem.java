package xyz.npgw.test.common.entity;

public class MerchantAcquirerItem extends Acquirer {

    int priority;

    MerchantAcquirerItem(
            String acquirerDisplayName,
            String acquirerMid,
            String acquirerCode,
            String acquirerConfig,
            Currency[] currencyList,
            SystemConfig systemConfig,
            boolean isActive,
            String acquirerName,
            int acquirerMcc,
            int property) {
        super(
                acquirerDisplayName,
                acquirerMid,
                acquirerCode,
                acquirerConfig,
                currencyList,
                systemConfig,
                isActive,
                acquirerName,
                acquirerMcc);
        this.priority = property;
    }
}
