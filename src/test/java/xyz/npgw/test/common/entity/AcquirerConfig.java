package xyz.npgw.test.common.entity;

public record AcquirerConfig(
        String tokenUrl,
        String apiKey,
        String paymentUrl,
        String captureUrl,
        String refundUrl,
        String refundCaptureUrl,
        String cancelUrl,
        String serviceUrl,
        int transactionMinAmount,
        int transactionMaxAmount) {

    public AcquirerConfig() {
        this(
                "https://api-gateway.sandbox.ngenius-payments.com/identity/auth/access-token",
                "ZmE1NWY1MmItNDFmNC00YzQ5LThlOWYtMWM1YTkyY2ZjMjIwOjRlYjRmMmJhLTk1MTYtNDA1ZS04ZGYwLTkwNjkwNjRkOTQxYw==",
                "https://api-gateway.sandbox.ngenius-payments.com/transactions/outlets/9af51c82-3525-491b-b4f1-4708d8a8608f/payment/card",
                "https://api-gateway.sandbox.ngenius-payments.com/transactions/outlets/9af51c82-3525-491b-b4f1-4708d8a8608f/orders/%s/payments/%s/captures",
                "https://api-gateway.sandbox.ngenius-payments.com/transactions/outlets/9af51c82-3525-491b-b4f1-4708d8a8608f/orders/%s/payments/%s/purchases/%s/refund",
                "https://api-gateway.sandbox.ngenius-payments.com/transactions/outlets/9af51c82-3525-491b-b4f1-4708d8a8608f/orders/%s/payments/%s/captures/%s/refund",
                "https://api-gateway.sandbox.ngenius-payments.com/transactions/outlets/9af51c82-3525-491b-b4f1-4708d8a8608f/orders/%s/payments/%s/cancel",
                "http://acquirer-ngenius.acquirer-ngenius-terraform:8082/ngenius-v1/token",
                10 * 100,
                2500 * 100);
    }
}
