package xyz.npgw.test.common.entity;

import xyz.npgw.test.common.UserRole;

import java.util.Arrays;

public record User(
        String companyName,
        boolean enabled,
        UserRole userRole,
        String[] merchantIds,
        String email,
        String password) {

    public User(
            String companyName,
            boolean enabled,
            UserRole userRole,
            BusinessUnit[] businessUnits,
            String email,
            String password) {
        this(companyName, enabled, userRole,
                Arrays.stream(businessUnits).map(BusinessUnit::merchantId).toArray(String[]::new), email, password);
    }

    @Override
    public String toString() {
        return "User: %s %s".formatted(email, userRole);
    }
}
