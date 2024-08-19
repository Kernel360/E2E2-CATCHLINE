package org.example.catch_line.common.constant;

import java.time.DayOfWeek;
import java.util.Arrays;

import org.example.catch_line.exception.CatchLineException;

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

	public static DayOfWeeks fromDescription(String description) {
		return Arrays.stream(values())
			.filter(e -> e.description.equals(description))
			.findFirst()
			.orElseThrow(() -> new CatchLineException("Enum에 값이 없습니다: " + description));
	}
}
