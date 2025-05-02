package xyz.npgw.test.common.entity;

public record SystemConfig(
        String challengeUrl,
        String fingerprintUrl,
        String resourceUrl,
        String notificationQueue) {

    public SystemConfig() {
        this("", "", "", "");
    }
}
