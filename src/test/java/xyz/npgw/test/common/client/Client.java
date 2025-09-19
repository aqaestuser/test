package xyz.npgw.test.common.client;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.CustomLog;
import xyz.npgw.test.common.entity.Token;

@CustomLog
public record Client(String apiKey) {

    public static Token getToken(APIRequestContext request, String apiKey) {
        APIResponse response = request.post("/merchant-v1/token",
                RequestOptions.create()
                        .setHeader("Content-Type", "text/plain")
                        .setData(apiKey));
        log.response(response, "get client token %s".formatted(apiKey));
        return new Gson().fromJson(response.text(), Token.class);
    }
}
