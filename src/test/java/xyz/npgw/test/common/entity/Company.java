package xyz.npgw.test.common.entity;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;
import org.testng.SkipException;

import static xyz.npgw.test.common.util.TestUtils.encode;

@Log4j2
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

    public static void create(APIRequestContext request, String companyName) {
        APIResponse response = request.post("portal-v1/company",
                RequestOptions.create().setData(new Company(companyName)));
        log.info("create company '{}' - {}", companyName, response.status());
        if (response.status() >= 500) {
            throw new SkipException(response.text());
        }
    }

    public static Company[] getAll(APIRequestContext request) {
        APIResponse response = request.get("portal-v1/company");
        log.info("get all companies - {} {}", response.status(), response.text());
        if (response.status() >= 500) {
            throw new SkipException(response.text());
        }
        return new Gson().fromJson(response.text(), Company[].class);
    }

    public static int delete(APIRequestContext request, String companyName) {
        APIResponse response = request.delete("portal-v1/company/%s".formatted(encode(companyName)));
        log.info("delete company '{}' - {}", companyName, response.status());
        if (response.status() >= 500) {
            throw new SkipException(response.text());
        }
        return response.status();
    }
}
