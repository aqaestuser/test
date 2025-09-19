package xyz.npgw.test.common.client;

public record CustomerDetails(
        String zip,
        String country,
        String lastName,
        String externalCustomerId,
        String address2,
        String city,
        String address1,
        String ipAddress,
        String dateOfBirth,
        String firstName,
        String phone,
        String state,
        String email) {

    public CustomerDetails() {
        this(
                "90210", "US", "Jordan", "32556",
                "unit 5", "Coast City", "59 Gil Broome Avenue",
                "192.168.3.123", "2004-08-24",
                "Hal", "+17723493500", "CA", "hal@greenlantern.com");
    }
}
