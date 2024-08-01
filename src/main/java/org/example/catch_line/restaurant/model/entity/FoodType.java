package org.example.catch_line.restaurant.model.entity;

public enum FoodType {

    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    WESTERN("양식");

    private final String description;

    FoodType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}