package xyz.npgw.test.common.entity;

public record TransactionSummary(
        String merchantId,
        String updatedOn,
        Currency currency,
        Status status,
        long totalAmount,
        long totalCount) {

    public TransactionSummary(Currency currency, Status status, long totalAmount, long totalCount) {
        this("", "", currency, status, totalAmount, totalCount);
    }
}
