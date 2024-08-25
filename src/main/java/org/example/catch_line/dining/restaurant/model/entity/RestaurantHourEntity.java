package org.example.catch_line.dining.restaurant.model.entity;

import java.time.LocalTime;

import org.example.catch_line.common.constant.DayOfWeeks;
import org.example.catch_line.dining.restaurant.model.entity.constant.OpenStatus;
import org.example.catch_line.exception.CatchLineException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurant_hour")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantHourEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long restaurantHourId;

	@Enumerated(EnumType.STRING)
	private DayOfWeeks dayOfWeek;
	private LocalTime openTime;
	private LocalTime closeTime;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OpenStatus openStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private RestaurantEntity restaurant;

	public RestaurantHourEntity(DayOfWeeks dayOfWeek, LocalTime openTime, LocalTime closeTime, OpenStatus openStatus,
		RestaurantEntity restaurant) {
		this.dayOfWeek = dayOfWeek;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.openStatus = openStatus;
		this.restaurant = restaurant;
	}

	public void updateRestaurantHourEntity(String dayOfWeekStr, LocalTime openTime, LocalTime closeTime, String openStatusStr) {
		try {
			DayOfWeeks dayOfWeek = DayOfWeeks.fromDescription(dayOfWeekStr.toUpperCase());
			OpenStatus openStatus = OpenStatus.valueOf(openStatusStr.toUpperCase());

			this.dayOfWeek = dayOfWeek;
			this.openTime = openTime;
			this.closeTime = closeTime;
			this.openStatus = openStatus;

		} catch (CatchLineException e) {
			System.err.println("Enum 값이 잘못되었습니다: " + e.getMessage());
		}
	}

	public void updateOpenStatus(OpenStatus openStatus) {
		this.openStatus = openStatus;
	}
}
