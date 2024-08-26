package org.example.catch_line.dining.restaurant.model.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.catch_line.common.constant.DayOfWeeks;
import org.example.catch_line.dining.restaurant.model.entity.constant.OpenStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RestaurantHourRequest {

	@NotBlank(message = "요일은 비어있을 수 없습니다.")
	private String dayOfWeek;

	@NotNull(message = "영업 시작 시간은 비어있을 수 없습니다.")
	private LocalTime openTime;

	@NotNull(message = "영업 종료 시간은 비어있을 수 없습니다.")
	private LocalTime closeTime;

	@NotBlank(message = "영업 상태는 비어있을 수 없습니다.")
	private String openStatus;
}
