package xyz.npgw.test.common.util;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import lombok.SneakyThrows;
import xyz.npgw.test.common.client.Transaction;
import xyz.npgw.test.common.client.TransactionResponse;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.entity.Status;
import xyz.npgw.test.common.entity.Type;

public class AuthTransactionUtils {

    @SneakyThrows
    public static TransactionResponse createPendingTransaction(APIRequestContext request,
                                                               int amount, BusinessUnit businessUnit,
                                                               String externalTransactionId) {

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .externalTransactionId(externalTransactionId)
                .type(Type.AUTH)
                .merchantId(businessUnit.merchantId())
                .build();

        return WaitUtils.waitUntil(request, Transaction.create(request, transaction), Status.PENDING);
    }

    public static TransactionResponse createAuthorisedTransaction(Playwright playwright, APIRequestContext request,
                                                                  int amount, BusinessUnit businessUnit,
                                                                  String externalTransactionId) {

        TransactionResponse transactionResponse = createPendingTransaction(
                request, amount, businessUnit, externalTransactionId);

        if (transactionResponse.paymentUrl() == null) {
            throw new RuntimeException("create pending AUTH transaction returned null paymentUrl");
        }

        TestUtils.pay(playwright, transactionResponse);

        return Transaction.getTransactionById(request, transactionResponse);
    }

    public static TransactionResponse createCancelAuthorisedTransaction(Playwright playwright, APIRequestContext request,
                                                                        int amount, BusinessUnit businessUnit,
                                                                        String externalTransactionId) {

        TransactionResponse transactionResponse = createAuthorisedTransaction(
                playwright, request, amount, businessUnit, externalTransactionId);

        Transaction.cancel(request, transactionResponse);

        return Transaction.getTransactionById(request, transactionResponse);
    }

    @SneakyThrows
    public static TransactionResponse createSuccessByFullCaptureTransaction(Playwright playwright, APIRequestContext request,
                                                                            int amount, BusinessUnit businessUnit,
                                                                            String externalTransactionId) {

        TransactionResponse transactionResponse = createAuthorisedTransaction(
                playwright, request, amount, businessUnit, externalTransactionId);

        Transaction.capture(request, transactionResponse, amount);

        return WaitUtils.waitUntil(request, transactionResponse, Status.SUCCESS);
    }

    @SneakyThrows
    public static TransactionResponse createSuccessByPartialCaptureTransaction(Playwright playwright, APIRequestContext request,
                                                                               int amount, BusinessUnit businessUnit,
                                                                               String externalTransactionId) {

        TransactionResponse transactionResponse = createAuthorisedTransaction(
                playwright, request, amount, businessUnit, externalTransactionId);

        Transaction.capture(request, transactionResponse, amount / 2);

        WaitUtils.waitUntil(request, transactionResponse, Status.PARTIAL_CAPTURE);

        Transaction.capture(request, transactionResponse, amount / 2);

        return WaitUtils.waitUntil(request, transactionResponse, Status.SUCCESS);
    }

    @SneakyThrows
    public static TransactionResponse createPartialCaptureTransaction(Playwright playwright, APIRequestContext request,
                                                                      int amount, BusinessUnit businessUnit,
                                                                      String externalTransactionId) {

        TransactionResponse transactionResponse = createAuthorisedTransaction(
                playwright, request, amount, businessUnit, externalTransactionId);

        Transaction.capture(request, transactionResponse, amount / 2);

        return WaitUtils.waitUntil(request, transactionResponse, Status.PARTIAL_CAPTURE);
    }

    //only for settled transactions
    @SneakyThrows
    public static TransactionResponse createPartialRefundTransaction(Playwright playwright, APIRequestContext request,
                                                                     int amount, BusinessUnit businessUnit,
                                                                     String externalTransactionId) {

        TransactionResponse transactionResponse = createPartialCaptureTransaction(
                playwright, request, amount, businessUnit, externalTransactionId);

        Transaction.refundCapture(request, transactionResponse, transactionResponse.operationList()[0].operationId());

        return WaitUtils.waitUntil(request, transactionResponse, Status.PARTIAL_REFUND);
    }

    //only for settled transactions
    @SneakyThrows
    public static TransactionResponse createRefundSuccessByFullCaptureTransaction(Playwright playwright, APIRequestContext request,
                                                                                  int amount, BusinessUnit businessUnit,
                                                                                  String externalTransactionId) {

        TransactionResponse transactionResponse = createSuccessByFullCaptureTransaction(
                playwright, request, amount, businessUnit, externalTransactionId);

        Transaction.refundCapture(request, transactionResponse, transactionResponse.operationList()[0].operationId());

        return WaitUtils.waitUntil(request, transactionResponse, Status.REFUND);
    }

    //only for settled transactions
    @SneakyThrows
    public static TransactionResponse createRefundSuccessByPartialCaptureTransaction(Playwright playwright, APIRequestContext request,
                                                                                     int amount, BusinessUnit businessUnit,
                                                                                     String externalTransactionId) {

        TransactionResponse transactionResponse = createSuccessByFullCaptureTransaction(
                playwright, request, amount, businessUnit, externalTransactionId);

        Transaction.refundCapture(request, transactionResponse, transactionResponse.operationList()[0].operationId());

        return WaitUtils.waitUntil(request, transactionResponse, Status.REFUND);
    }
}
