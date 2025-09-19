package xyz.npgw.test.common.entity;

public record SystemConfig(
        String challengeUrl,
        String fingerprintUrl,
        String resourceUrl,
        String notificationQueue) {

    public SystemConfig() {
        this(
                "https://test.npgw.xyz/merchant-v1/challenge",
                "https://test.npgw.xyz/merchant-v1/fingerprint",
                "https://test.npgw.xyz/merchant-v1/resource",
                "notification.fifo");
    }

    @Override
    public String toString() {
        return "Challenge URL" + challengeUrl
                + "Fingerprint URL" + fingerprintUrl
                + "Resource URL" + resourceUrl
                + "Notification queue" + notificationQueue;
    }
}
