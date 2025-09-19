package xyz.npgw.test.common.client;

import xyz.npgw.test.common.entity.Currency;
import xyz.npgw.test.common.entity.Status;

public record Operation(
        int amount,
        Currency currency,
        String operationId,
        String originalOperationId,
        OperationType type,
        Status status,
        String timestamp) {
}
