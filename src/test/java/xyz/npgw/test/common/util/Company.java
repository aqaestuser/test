package xyz.npgw.test.common.util;

import net.datafaker.Faker;

public record Company(
        String companyName,
        String companyType,
        String description,
        String website,
        String primaryContact,
        String companyEmail,
        boolean apiActive,
        boolean portalActive,
        String country,
        String state,
        String zip,
        String city,
        String phone,
        String mobile,
        String fax) {

    public Company(String companyName, String companyType) {
        this(companyName, companyType,
                "", "", "", "",
                true, true,
                "", "", "", "",
                "", "", "");
    }

    public Company(Faker faker) {
        this(faker.company().name().replace("'", ""), faker.company().industry());
    }

    public Company(String companyName) {
        this(companyName, new Faker().company().industry());
    }
}
