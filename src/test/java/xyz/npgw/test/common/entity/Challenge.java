package xyz.npgw.test.common.entity;

public record Challenge(String sessionId, Credentials data, String userChallengeType) {
}
