package xyz.npgw.test.common.entity;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import lombok.extern.log4j.Log4j2;
import org.testng.SkipException;

@Log4j2
public record Info(App app) {

    public static Info get(APIRequestContext request) {
        APIResponse response = request.get("portal-v1/info");
        log.info("get info - {}", response.status());
        if (response.status() >= 500) {
            throw new SkipException(response.text());
        }
        return new Gson().fromJson(response.text(), Info.class);
    }
}
