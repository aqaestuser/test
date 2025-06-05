package xyz.npgw.test.common.entity;

public record Error(String message) {

    public Error() {
        this("");
    }
}
