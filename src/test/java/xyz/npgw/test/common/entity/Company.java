package xyz.npgw.test.common.entity;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.CustomLog;
import lombok.SneakyThrows;
import net.datafaker.Faker;

import java.util.concurrent.TimeUnit;

import static xyz.npgw.test.common.util.TestUtils.encode;

@CustomLog
public record Company(
        String companyName,
        String companyType,
        Address companyAddress,
        String description,
        String website,
        String primaryContact,
        String email,
        boolean isPortalActive,
        boolean isApiActive) {

    public Company(String companyName, String companyType) {
        this(companyName, companyType, new Address(),
                "", "", "", "",
                true, true);
    }

    public Company(Faker faker) {
        this(faker.company().name(), faker.company().industry());
    }

    public Company(String companyName) {
        this(companyName, new Faker().company().industry());
    }

    @SneakyThrows
    public static void create(APIRequestContext request, String companyName) {
        APIResponse response = request.post("portal-v1/company",
                RequestOptions.create().setData(new Company(companyName)));
        log.response(response, "create company %s".formatted(companyName));

        while (!exists(request, companyName)) {
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public static boolean exists(APIRequestContext request, String companyName) {
        APIResponse response = request.get("portal-v1/company");
        log.response(response, "exists via all companies");
        return response.ok() && response.text().contains(companyName);
    }

    public static Company[] getAll(APIRequestContext request) {
        APIResponse response = request.get("portal-v1/company");
        log.response(response, "get all companies");
        return new Gson().fromJson(response.text(), Company[].class);
    }

    public static int delete(APIRequestContext request, String companyName) {
        APIResponse response = request.delete("portal-v1/company/%s".formatted(encode(companyName)));
        log.response(response, "delete company %s".formatted(companyName));
        return response.status();
    }
}
