package xyz.npgw.test.common.client;

import com.google.gson.Gson;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import lombok.Builder;
import lombok.CustomLog;
import lombok.Getter;
import lombok.Setter;
import xyz.npgw.test.common.entity.Currency;
import xyz.npgw.test.common.entity.Type;
import xyz.npgw.test.common.util.TestUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@CustomLog
public class Transaction {

    @Builder.Default
    private String notificationUrl = "https://hkdk.events/yhpys9at0zp7lf";
    @Builder.Default
    private int amount = 1000;
    @Builder.Default
    private String merchantId = "";
    @Builder.Default
    private String redirectUrlSuccess = "https://hkdk.events/qd8lfp7hqyqc03";
    @Builder.Default
    private String redirectUrlCancel = "https://hkdk.events/oj03lklkzpdive";
    @Builder.Default
    private Currency currency = Currency.EUR;
    @Builder.Default
    private String externalTransactionId = "000000000000000000000000000000";
    @Builder.Default
    private PaymentDetails paymentDetails = new PaymentDetails();
    @Builder.Default
    private Type type = Type.SALE;
    @Builder.Default
    private CustomerDetails customerDetails = new CustomerDetails();
    @Builder.Default
    private String expiresAt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            .format(ZonedDateTime.now().plusMinutes(35));
    @Builder.Default
    private String redirectUrlFail = "https://hkdk.events/zj36a72tes7hhz";

    public static TransactionResponse create(APIRequestContext request, Transaction transaction) {
        transaction.setExternalTransactionId(TestUtils.now().replace(".", "") + transaction.externalTransactionId);
        APIResponse response = request.post("/merchant-v1/transaction?async=true",
                RequestOptions.create().setData(transaction));
        log.response(response, "create %s %s transaction %.2f%s for %s".formatted(
                transaction.type,
                transaction.externalTransactionId,
                transaction.amount / 100.0,
                transaction.currency,
                transaction.merchantId));

        if (response.status() / 100 != 2) {
            throw new RuntimeException("Status code is not group 200.");
        }

        return new Gson().fromJson(response.text(), TransactionResponse.class);
    }

    public static TransactionResponse getTransaction(APIRequestContext request, Transaction transaction) {
        APIResponse response = request.get("/merchant-v1/transaction?merchantId=%s&externalTransactionId=%s"
                .formatted(transaction.merchantId, transaction.externalTransactionId));
        log.response(response, "get transaction status for %s with %s".formatted(
                transaction.merchantId,
                transaction.externalTransactionId));

        return new Gson().fromJson(response.text(), TransactionResponse.class);
    }

    public static TransactionResponse getTransactionById(APIRequestContext request, TransactionResponse transactionResponse) {
        APIResponse response = request.get("/merchant-v1/transaction/%s".formatted(transactionResponse.transactionId()));
        log.response(response, "get transaction by id %s".formatted(transactionResponse.transactionId()));

        return new Gson().fromJson(response.text(), TransactionResponse.class);
    }

    //only for auth type(cancel a authorised status transaction - release all locked funds)
    public static TransactionResponse cancel(APIRequestContext request, TransactionResponse transactionResponse) {
        APIResponse response = request.post("/merchant-v1/transaction/%s/cancel".formatted(transactionResponse.transactionId()));
        log.response(response, "cancel transaction %s".formatted(transactionResponse.externalTransactionId()));

        return new Gson().fromJson(response.text(), TransactionResponse.class);
    }

    //only for auth type transactions(capture specified amount <= authorised)
    public static TransactionResponse capture(APIRequestContext request, TransactionResponse transactionResponse, int amount) {
        APIResponse response = request.post("/merchant-v1/transaction/%s/capture".formatted(transactionResponse.transactionId()),
                RequestOptions.create().setData(new Amount(amount, transactionResponse.currency())));
        log.response(response, "capture AUTH transaction %s".formatted(transactionResponse.externalTransactionId()));

        return new Gson().fromJson(response.text(), TransactionResponse.class);
    }

    //only for auth type transactions(refund the entire amount of capture operation)
    public static TransactionResponse refundCapture(
            APIRequestContext request, TransactionResponse transactionResponse, String operationId) {
        APIResponse response = request.post("/merchant-v1/transaction/%s/refund/capture/%s"
                .formatted(transactionResponse.transactionId(), operationId));
        log.response(response, "%s refund capture operation %s for %s"
                .formatted(transactionResponse.operationList(), operationId, transactionResponse.externalTransactionId()));

        return new Gson().fromJson(response.text(), TransactionResponse.class);
    }

    //only for sale type transactions (partial refund available too)
    public static TransactionResponse refund(APIRequestContext request, TransactionResponse transactionResponse, int amount) {
        APIResponse response = request.post("/merchant-v1/transaction/%s/refund".formatted(transactionResponse.transactionId()),
                RequestOptions.create().setData(new Amount(amount, transactionResponse.currency())));
        log.response(response, "refund %.2f%s for %s".formatted(
                amount / 100.0, transactionResponse.currency(), transactionResponse.externalTransactionId()));

        return new Gson().fromJson(response.text(), TransactionResponse.class);
    }

    private record Amount(int amount, Currency currency) {
    }
}
