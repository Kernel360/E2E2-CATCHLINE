package org.example.catch_line.dining.restaurant.model.dto;

import java.time.LocalTime;

import org.example.catch_line.common.constant.DayOfWeeks;
import org.example.catch_line.dining.restaurant.model.entity.constant.OpenStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RestaurantHourRequest {

	private String dayOfWeek;
	private LocalTime openTime;
	private LocalTime closeTime;
	private String openStatus;
}
