package xyz.npgw.test.common.entity;

public record SystemConfig(
//        String laborum_c47,
//        String ex_8,
        String challengeUrl,
        String fingerprintUrl,
        String resourceUrl,
        String notificationQueue) {

    public SystemConfig() {
        this("", "", "", "");
    }
}
