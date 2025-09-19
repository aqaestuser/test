package xyz.npgw.test.common.entity;

import lombok.CustomLog;

@CustomLog
public record MerchantAcquirer(
        String merchantId,
        MerchantAcquirerItem[] acquirerList) {
}
