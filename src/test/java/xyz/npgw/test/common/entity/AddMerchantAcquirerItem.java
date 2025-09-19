package xyz.npgw.test.common.entity;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.CustomLog;

@CustomLog
public record AddMerchantAcquirerItem(
        String merchantId,
        String acquirerName,
        Currency[] currencyList,
        boolean isActive) {

    //addMerchantAcquirerItem
    public static void create(APIRequestContext request, BusinessUnit businessUnit, Acquirer acquirer) {
        APIResponse response = request.post("portal-v1/merchant-acquirer",
                RequestOptions.create().setData(new AddMerchantAcquirerItem(businessUnit.merchantId(),
                        acquirer.getAcquirerName(), acquirer.getCurrencyList(), true)));
        log.response(response, "add %s acquirer to %s".formatted(acquirer.getAcquirerName(), businessUnit.merchantTitle()));
    }
}
