package org.example.catch_line.restaurant.model.entity.constant;

import java.time.DayOfWeek;

public enum DayOfWeeks {

    MONDAY("월요일"),
    TUESDAY("화요일"),
    WEDNESDAY("수요일"),
    THURSDAY("목요일"),
    FRIDAY("금요일"),
    SATURDAY("토요일"),
    SUNDAY("일요일");

    private final String description;

    DayOfWeeks(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static DayOfWeeks from(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return MONDAY;
            case TUESDAY: return TUESDAY;
            case WEDNESDAY: return WEDNESDAY;
            case THURSDAY: return THURSDAY;
            case FRIDAY: return FRIDAY;
            case SATURDAY: return SATURDAY;
            case SUNDAY: return SUNDAY;
        }
        return null;
    }
}
