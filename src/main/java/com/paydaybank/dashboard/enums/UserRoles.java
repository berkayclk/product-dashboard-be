package com.paydaybank.dashboard.enums;

public enum UserRoles {
    USER("user"),
    ADMIN("admin");

    private String role;

    UserRoles(String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }
}
