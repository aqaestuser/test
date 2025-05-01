package xyz.npgw.test.common.entity;

import xyz.npgw.test.common.UserRole;

public record User(
        String companyName,
        boolean enabled,
        UserRole userRole,
        String[] merchantIds,
        String email,
        String password) {

    @Override
    public String toString() {
        return "User: %s %s".formatted(email, userRole);
    }
}
