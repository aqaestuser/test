package xyz.npgw.test.common.entity;

public record ApiError(long timestamp, int status, String message, String path, String code) {
}
