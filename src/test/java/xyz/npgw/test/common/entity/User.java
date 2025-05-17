package xyz.npgw.test.common.entity;

import xyz.npgw.test.common.ProjectProperties;
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

    public static User createSystemAdmin(String email, String password) {
        return new User("super", true, UserRole.SUPER, new String[]{}, email, password);
    }

    public static User createSystemAdmin(String email) {
        return createSystemAdmin(email, ProjectProperties.getUserPassword());
    }

    public static User createCompanyAdmin(String companyName, String email, String password) {
        return new User(companyName, true, UserRole.ADMIN, new String[]{}, email, password);
    }

    public static User createCompanyAdmin(String companyName, String email) {
        return createCompanyAdmin(companyName, email, ProjectProperties.getUserPassword());
    }

    public static User createCompanyAnalyst(String companyName, String[] merchantIds, String email, String password) {
        return new User(companyName, true, UserRole.USER, merchantIds, email, password);
    }

    public static User createCompanyAnalyst(String companyName, String[] merchantIds, String email) {
        return createCompanyAnalyst(companyName, merchantIds, email, ProjectProperties.getUserPassword());
    }

    @Override
    public String toString() {
        return "User: %s %s".formatted(email, userRole);
    }
}
