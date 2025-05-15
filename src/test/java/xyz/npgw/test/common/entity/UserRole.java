package xyz.npgw.test.common.entity;

import lombok.Getter;

@Getter
public enum UserRole {

    SUPER("System admin"),
    ADMIN("Company admin"),
    USER("Company analyst");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }
}
