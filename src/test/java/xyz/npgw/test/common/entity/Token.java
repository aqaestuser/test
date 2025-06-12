package xyz.npgw.test.common.entity;

public record Token(String accessToken, int expiresIn, String idToken, String refreshToken, String tokenType) {
}
