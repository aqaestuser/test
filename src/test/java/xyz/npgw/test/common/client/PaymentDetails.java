package xyz.npgw.test.common.client;

import xyz.npgw.test.common.entity.CardType;

public record PaymentDetails(
        String country, //TODO ask do we need this field
        String expMonth,
        String cvv,
        String cardHolderName,
        String expYear,
        String bin, //TODO ask do we need this field
        CardType cardType,
        String paymentMethod,
        String pan,
        String issuer) { //TODO ask do we need this field

    public PaymentDetails() {
        this("US", "06", "148", "Hal Jordan", "2029", "bin", CardType.MASTERCARD, "CreditCard", "2303779999000275", "issuer");
    }
}
