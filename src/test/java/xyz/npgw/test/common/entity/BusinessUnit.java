package xyz.npgw.test.common.entity;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.RequestOptions;
import lombok.CustomLog;
import lombok.SneakyThrows;
import xyz.npgw.test.common.ProjectProperties;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static xyz.npgw.test.common.util.TestUtils.encode;

@CustomLog
public record BusinessUnit(
        String merchantId,
        String merchantTitle) {

    public BusinessUnit(String merchantTitle) {
        this(null, merchantTitle);
    }

    public static BusinessUnit create(APIRequestContext request, String companyName, String merchantTitle) {
        APIResponse response = request.post("portal-v1/company/%s/merchant".formatted(encode(companyName)),
                RequestOptions.create().setData(new BusinessUnit(merchantTitle)));
        log.response(response, "create merchant for company %s".formatted(companyName));

        if (response.status() == 422) {
            return Arrays.stream(getAll(request, companyName))
                    .filter(m -> m.merchantTitle.equals(merchantTitle))
                    .findFirst()
                    .orElseThrow();
        }

        return new Gson().fromJson(response.text(), BusinessUnit.class);
    }

    public static BusinessUnit[] getAll(APIRequestContext request, String companyName) {
        APIResponse response = request.get("portal-v1/company/%s/merchant".formatted(encode(companyName)));
        log.response(response, "get all merchants for company %s".formatted(companyName));

        if (response.status() == 404) {
            return new BusinessUnit[]{};
        }
        return new Gson().fromJson(response.text(), BusinessUnit[].class);
    }

    public static String getNewApikey(APIRequestContext request, String companyName, BusinessUnit businessUnit) {
        APIResponse response = request.post("portal-v1/company/%s/merchant/%s".formatted(encode(companyName), businessUnit.merchantId));
        log.response(response, "get api key for merchant %s of company %s".formatted(businessUnit.merchantTitle, companyName));

        return response.text();
    }

    @SneakyThrows
    public static void deleteWithTimeout(APIRequestContext request, String companyName, BusinessUnit businessUnit) {
        double timeout = ProjectProperties.getDefaultTimeout();
        while (204 != delete(request, companyName, businessUnit)) {
            TimeUnit.MILLISECONDS.sleep(300);
            timeout -= 300;
            if (timeout <= 0) {
                throw new TimeoutError("Delete '%s'".formatted(businessUnit.merchantTitle));
            }
        }
    }

    public static int delete(APIRequestContext request, String companyName, BusinessUnit businessUnit) {
        APIResponse response = request.delete(
                "portal-v1/company/%s/merchant/%s".formatted(encode(companyName), businessUnit.merchantId()));
        log.response(response, "delete merchant %s".formatted(businessUnit.merchantId()));
        return response.status();
    }
}
