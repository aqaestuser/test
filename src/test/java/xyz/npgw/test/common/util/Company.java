package xyz.npgw.test.common.util;

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
        String fax
){}

