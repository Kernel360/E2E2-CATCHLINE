package org.example.catch_line.common.constant;

import java.time.DayOfWeek;
import java.util.Arrays;

public enum DayOfWeeks {

    MONDAY(DayOfWeek.MONDAY, "월요일"),
    TUESDAY(DayOfWeek.TUESDAY, "화요일"),
    WEDNESDAY(DayOfWeek.WEDNESDAY, "수요일"),
    THURSDAY(DayOfWeek.THURSDAY, "목요일"),
    FRIDAY(DayOfWeek.FRIDAY, "금요일"),
    SATURDAY(DayOfWeek.SATURDAY, "토요일"),
    SUNDAY(DayOfWeek.SUNDAY, "일요일");

    private final DayOfWeek dayOfWeek;
    private final String description;

    DayOfWeeks(DayOfWeek dayOfWeek, String description) {
        this.dayOfWeek = dayOfWeek;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public static DayOfWeeks from(DayOfWeek dayOfWeek) {
        return Arrays.stream(values())
                .filter(e -> e.dayOfWeek == dayOfWeek)
                .findFirst()
                .orElse(null);
    }
}
