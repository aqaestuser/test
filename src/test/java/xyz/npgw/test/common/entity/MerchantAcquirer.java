package xyz.npgw.test.common.entity;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import lombok.CustomLog;

import static xyz.npgw.test.common.util.TestUtils.encode;

@CustomLog
public record MerchantAcquirer(
        String merchantId,
        String acquirerName,
        Currency[] currencyList,
        boolean isActive) {

    public static void delete(APIRequestContext request, String merchantId) {
        APIResponse response = request.delete("portal-v1/merchant-acquirer/%s".formatted(encode(merchantId)));
        log.response(response, "delete merchant acquirer %s".formatted(merchantId));
    }
}
