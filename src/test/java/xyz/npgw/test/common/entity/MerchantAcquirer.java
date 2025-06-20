package xyz.npgw.test.common.entity;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import lombok.extern.log4j.Log4j2;
import org.testng.SkipException;

import static xyz.npgw.test.common.util.TestUtils.encode;

@Log4j2
public record MerchantAcquirer(
        String merchantId,
        String acquirerName,
        Currency[] currencyList,
        boolean isActive) {

    public static void delete(APIRequestContext request, String merchantId) {
        APIResponse response = request.delete("portal-v1/merchant-acquirer/%s".formatted(encode(merchantId)));
        log.info("delete merchant acquirer '{}' - {}", merchantId, response.status());
        if (response.status() >= 500) {
            throw new SkipException(response.text());
        }
    }
}
