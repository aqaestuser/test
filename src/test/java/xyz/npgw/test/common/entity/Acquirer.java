package xyz.npgw.test.common.entity;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.Builder;
import lombok.CustomLog;
import lombok.Getter;

import java.util.Arrays;

import static xyz.npgw.test.common.util.TestUtils.encode;

@Getter
@Builder
@CustomLog
public class Acquirer {

    @Builder.Default
    private String acquirerDisplayName = "display name";
    @Builder.Default
    private String acquirerMid = "acquirer mid";
    @Builder.Default
    private String acquirerCode = "NGenius";
    @Builder.Default
    private String acquirerConfig = "default";
    @Builder.Default
    private Currency[] currencyList = new Currency[]{Currency.USD, Currency.EUR};
    @Builder.Default
    private SystemConfig systemConfig = new SystemConfig();
    @Builder.Default
    private boolean isActive = true;
    @Builder.Default
    private String acquirerName = "acquirer name";
    @Builder.Default
    private String acquirerMidMcc = "1111";

    public String getStatus() {
        return isActive ? "Active" : "Inactive";
    }

    public String getCurrency() {
        return String.join(", ", Arrays.stream(currencyList).map(Enum::name).toList());
    }

    public static void create(APIRequestContext request, Acquirer acquirer) {
        APIResponse response = request.post("portal-v1/acquirer", RequestOptions.create().setData(acquirer));
        log.response(response, "create acquirer %s".formatted(acquirer.acquirerName));
    }

    public static Acquirer[] getAll(APIRequestContext request) {
        APIResponse response = request.get("portal-v1/acquirer");
        log.response(response, "get all acquirers");
        return new Gson().fromJson(response.text(), Acquirer[].class);
    }

    public static void delete(APIRequestContext request, String acquirerName) {
        APIResponse response = request.delete("portal-v1/acquirer/%s".formatted(encode(acquirerName)));
        log.response(response, "delete acquirer %s".formatted(acquirerName));
    }
}
