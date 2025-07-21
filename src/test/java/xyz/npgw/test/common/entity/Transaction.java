package xyz.npgw.test.common.entity;

public record Transaction(
        double amount,
        Currency currency,
        String merchantId,
        String externalTransactionId,
        String redirectUrlSuccess,
        String redirectUrlCancel,
        String redirectUrlFail,
        String expiresAt,
        PaymentDetails paymentDetails,
        String transactionId,
        Status status,
        xyz.npgw.test.common.entity.Error error,
        String createdOn,
        String updatedOn) {

    public Transaction(
            String createdOn,
            String npgwReference,
            String merchantReference,
            double amount,
            Currency currency,
            CardType cardType,
            Status status) {
        this(amount, currency, "", merchantReference, "", "", "",
                "", new PaymentDetails(cardType), npgwReference, status, new Error(), createdOn, "");
    }
}
