package xyz.npgw.test.common.client;

import xyz.npgw.test.common.entity.Currency;
import xyz.npgw.test.common.entity.Status;
import xyz.npgw.test.common.entity.Type;

public record TransactionResponse(
        int amount,
        Currency currency,
        String merchantId,
        String externalTransactionId,
        Type type,
        String notificationUrl,
        String redirectUrlSuccess,
        String redirectUrlCancel,
        String redirectUrlFail,
        String expiresAt,
        PaymentDetails paymentDetails,
        String transactionId,
        Status status,
        CustomerDetails customerDetails,
        String createdOn,
        String updatedOn,
        String paymentUrl,
        ThreeDSecureDetails threeDSecureDetails,
        Operation[] operationList) {
}
