package xyz.npgw.test.common.entity;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import lombok.CustomLog;

@CustomLog
public record Info(App app) {

    public static Info get(APIRequestContext request) {
        APIResponse response = request.get("portal-v1/info");
        log.response(response, "get info");
        return new Gson().fromJson(response.text(), Info.class);
    }
}
