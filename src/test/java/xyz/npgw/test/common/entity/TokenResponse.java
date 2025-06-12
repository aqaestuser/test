package xyz.npgw.test.common.entity;

public record TokenResponse(String userChallengeType, Token token, String sessionId) {
}
