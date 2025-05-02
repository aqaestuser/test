package xyz.npgw.test.common.entity;

import net.datafaker.Faker;

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

    public boolean isEmpty() {
        return companyAddress.isEmpty() && description.isEmpty() && website.isEmpty() && primaryContact.isEmpty()
                && isPortalActive && isApiActive;
    }
}
