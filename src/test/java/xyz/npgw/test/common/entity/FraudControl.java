package xyz.npgw.test.common.entity;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.Builder;
import lombok.CustomLog;
import lombok.Getter;

import static xyz.npgw.test.common.util.TestUtils.encode;

@Getter
@Builder
@CustomLog
public class FraudControl {

    @Builder.Default
    private String controlName = "";
    @Builder.Default
    private String controlDisplayName = "";
    @Builder.Default
    private String controlType = "bin_check";
    @Builder.Default
    private String controlCode = "";
    @Builder.Default
    private String controlConfig = "";
    @Builder.Default
    private boolean isActive = true;

    public static void create(APIRequestContext request, FraudControl fraudControl) {
        APIResponse response = request.post("portal-v1/control", RequestOptions.create().setData(fraudControl));
        log.response(response, "create control %s".formatted(fraudControl.controlName));
    }

    public static void delete(APIRequestContext request, String fraudControlName) {
        APIResponse response = request.delete("portal-v1/control/%s".formatted(encode(fraudControlName)));
        log.response(response, "delete control %s".formatted(fraudControlName));
    }

    public static FraudControl[] getAll(APIRequestContext request) {
        APIResponse response = request.get("portal-v1/control");
        log.response(response, "get all Fraud Control");
        return new Gson().fromJson(response.text(), FraudControl[].class);
    }

    @Override
    public String toString() {
        return "%s%s%s%s%s%s".formatted(controlType.equals("bin_check") ? "BIN Check" : "Fraud Screen", controlName,
                controlDisplayName, controlCode, controlConfig, isActive ? "Active" : "Inactive");
    }
}
