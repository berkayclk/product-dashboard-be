package com.paydaybank.dashboard.enums;

import java.util.Arrays;

public enum UserRoles {
    USER("USER"),
    ADMIN("ADMIN");

    private String role;

    UserRoles(String role) {
        this.role = role;
    }

    public static UserRoles getRole(String role) {
        return Arrays.stream(UserRoles.values()).filter(s->s.toString().equals(role)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return this.role;
    }
}