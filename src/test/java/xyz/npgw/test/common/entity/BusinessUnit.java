package xyz.npgw.test.common.entity;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.extern.log4j.Log4j2;
import org.testng.SkipException;

import static xyz.npgw.test.common.util.TestUtils.encode;

@Log4j2
public record BusinessUnit(
        String merchantId,
        String merchantTitle) {

    public BusinessUnit(String merchantTitle) {
        this(null, merchantTitle);
    }

    public static BusinessUnit create(APIRequestContext request, String companyName, String merchantTitle) {
        APIResponse response = request.post("portal-v1/company/%s/merchant".formatted(encode(companyName)),
                RequestOptions.create().setData(new BusinessUnit(merchantTitle)));
        if (response.status() >= 500) {
            throw new SkipException(response.text());
        }
        log.info("create merchant for company '{}' - {}", companyName, response.status());
        return new Gson().fromJson(response.text(), BusinessUnit.class);
    }

    public static BusinessUnit[] getAll(APIRequestContext request, String companyName) {
        APIResponse response = request.get("portal-v1/company/%s/merchant".formatted(encode(companyName)));
        log.info("get all merchants for company '{}' - {}", companyName, response.status());
        if (response.status() >= 500) {
            throw new SkipException(response.text());
        }
        if (response.status() == 404) {
            return new BusinessUnit[]{};
        }
        return new Gson().fromJson(response.text(), BusinessUnit[].class);
    }

    public static void delete(APIRequestContext request, String companyName, BusinessUnit businessUnit) {
        APIResponse response = request.delete(
                "portal-v1/company/%s/merchant/%s".formatted(encode(companyName), businessUnit.merchantId()));
        log.info("delete merchant '{}' - {}", businessUnit.merchantId(), response.status());
    }
}
