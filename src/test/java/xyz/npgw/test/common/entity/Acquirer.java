package xyz.npgw.test.common.entity;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.extern.log4j.Log4j2;
import org.testng.SkipException;

import static xyz.npgw.test.common.util.TestUtils.encode;

@Log4j2
public record Acquirer(
        String acquirerCode,
        String acquirerConfig,
        SystemConfig systemConfig,
        String acquirerName,
        String[] currencyList,
        boolean isActive) {

    public Acquirer(String acquirerName) {
        this("NGenius", "et", new SystemConfig(),
                acquirerName, new String[]{"USD"}, true);
    }

    public static void create(APIRequestContext request, Acquirer acquirer) {
        APIResponse response = request.post("portal-v1/acquirer", RequestOptions.create().setData(acquirer));
        log.info("create acquirer '{}' - {}", acquirer.acquirerName(), response.status());
        if (response.status() >= 500) {
            throw new SkipException(response.text());
        }
    }

    public static Acquirer[] getAll(APIRequestContext request) {
        APIResponse response = request.get("portal-v1/acquirer");
        log.info("get all acquirers - {} {}", response.status(), response.text());
        if (response.status() >= 500) {
            throw new SkipException(response.text());
        }
        return new Gson().fromJson(response.text(), Acquirer[].class);
    }

    public static void delete(APIRequestContext request, String acquirerName) {
        APIResponse response = request.delete("portal-v1/acquirer/%s".formatted(encode(acquirerName)));
        log.info("delete acquirer '{}' - {}", acquirerName, response.status());
        if (response.status() >= 500) {
            throw new SkipException(response.text());
        }
    }
}
