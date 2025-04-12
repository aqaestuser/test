package xyz.npgw.test.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    private String companyName;
    private String companyType;
    private String description;
    private String website;
    private String primaryContact;
    private String companyEmail;
    private String country;
    private String state;
    private String zip;
    private String city;
    private String phone;
    private String mobile;
    private String fax;
    private boolean apiActive;
    private boolean portalActive;

}
