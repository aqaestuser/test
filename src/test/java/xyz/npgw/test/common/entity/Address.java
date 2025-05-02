package xyz.npgw.test.common.entity;

public record Address(
        String city,
        String state,
        String zip,
        String country,
        String phone,
        String mobile,
        String fax) {

    public Address() {
        this("", "", "", "", "", "", "");
    }

    public boolean isEmpty() {
        return city.isEmpty() && zip.isEmpty() && country.isEmpty() && phone.isEmpty() && mobile.isEmpty()
                && fax.isEmpty();
    }
}
