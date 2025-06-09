package xyz.npgw.test.common.entity;

public record Transaction(
        int amount,
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
        Error error,
        String createdOn,
        String updatedOn) {

    public Transaction(
            String createdOn,
            String npgwReference,
            String merchantReference,
            int amount,
            CardType cardType,
            Currency currency,
            Status status) {
        this(amount, currency, "", merchantReference, "", "", "",
                "", new PaymentDetails(cardType), npgwReference, status, new Error(), createdOn, "");
    }
}
