package org.example.catch_line.common.constant;

public enum ServiceType {

    WAITING("줄서기"),
    RESERVATION("예약");

    private final String description;

    ServiceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
