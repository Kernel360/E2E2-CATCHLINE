package org.example.catch_line.common.constant;

public enum Role {
    OWNER("식당사장님"),
    USER("일반사용자");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthority() {
        return "ROLE_" + name();
    }

}
