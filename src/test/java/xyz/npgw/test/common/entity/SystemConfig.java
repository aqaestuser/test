package xyz.npgw.test.common.entity;

public record SystemConfig(
        String challengeUrl,
        String fingerprintUrl,
        String resourceUrl,
        String notificationQueue) {

    public SystemConfig() {
        this(
                "https://test.npgw.xyz/challenge/url",
                "https://test.npgw.xyz/fingerprint/url",
                "https://test.npgw.xyz/resource/url",
                "notificationQueue");
    }
}
