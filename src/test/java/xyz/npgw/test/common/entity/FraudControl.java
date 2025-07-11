package xyz.npgw.test.common.entity;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.Builder;
import lombok.CustomLog;
import lombok.Getter;

@Getter
@Builder
@CustomLog
public class FraudControl {

    @Builder.Default
    private String controlDisplayName = "";
    @Builder.Default
    private String controlCode = "";
    @Builder.Default
    private String controlConfig = "";
    @Builder.Default
    private boolean isActive = true;
    @Builder.Default
    private String controlName = "";

    public static void create(APIRequestContext request, FraudControl fraudControl) {
        APIResponse response = request.post("portal-v1/control", RequestOptions.create().setData(fraudControl));
        log.response(response, "create control %s".formatted(fraudControl.controlName));
    }
}
