package xyz.npgw.test.common.util;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import lombok.SneakyThrows;
import xyz.npgw.test.common.client.Transaction;
import xyz.npgw.test.common.client.TransactionResponse;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.entity.Status;
import xyz.npgw.test.common.entity.Type;

public class SaleTransactionUtils {

    @SneakyThrows
    public static TransactionResponse createPendingTransaction(APIRequestContext request,
                                                               int amount, BusinessUnit businessUnit,
                                                               String externalTransactionId) {

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .externalTransactionId(externalTransactionId)
                .type(Type.SALE)
                .merchantId(businessUnit.merchantId())
                .build();

        TransactionResponse transactionResponse = Transaction.create(request, transaction);

        return WaitUtils.waitUntil(request, transactionResponse, Status.PENDING);
    }

    @SneakyThrows
    public static TransactionResponse createSuccessTransaction(Playwright playwright, APIRequestContext request,
                                                               int amount, BusinessUnit businessUnit,
                                                               String externalTransactionId) {

        TransactionResponse transactionResponse = createPendingTransaction(
                request, amount, businessUnit, externalTransactionId);

        TestUtils.pay(playwright, transactionResponse);

        return WaitUtils.waitUntil(request, transactionResponse, Status.SUCCESS);
    }

    //only for settled transactions
    @SneakyThrows
    public static TransactionResponse createRefundTransaction(Playwright playwright, APIRequestContext request,
                                                              int amount, BusinessUnit businessUnit,
                                                              String externalTransactionId) {

        TransactionResponse transactionResponse = createSuccessTransaction(
                playwright, request, amount, businessUnit, externalTransactionId);

        Transaction.refund(request, transactionResponse, amount);

        return WaitUtils.waitUntil(request, transactionResponse, Status.REFUND);
    }

    //only for settled transactions
    @SneakyThrows
    public static TransactionResponse createPartialRefundTransaction(Playwright playwright, APIRequestContext request,
                                                                     int amount, BusinessUnit businessUnit,
                                                                     String externalTransactionId) {

        TransactionResponse transactionResponse = createSuccessTransaction(
                playwright, request, amount, businessUnit, externalTransactionId);

        Transaction.refund(request, transactionResponse, amount / 2);

        return WaitUtils.waitUntil(request, transactionResponse, Status.PARTIAL_REFUND);
    }
}
