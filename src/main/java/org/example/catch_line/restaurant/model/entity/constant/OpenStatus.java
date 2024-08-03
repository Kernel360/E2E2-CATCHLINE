package org.example.catch_line.restaurant.model.entity.constant;

public enum OpenStatus {

    OPEN("영업 중"),
    CLOSE("영업 종료");

    private final String description;

    OpenStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
