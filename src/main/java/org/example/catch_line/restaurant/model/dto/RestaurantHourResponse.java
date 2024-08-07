package org.example.catch_line.restaurant.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.catch_line.restaurant.model.entity.constant.OpenStatus;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@Builder
public class RestaurantHourResponse {

    private Long restaurantHourId;
    private DayOfWeek dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private OpenStatus openStatus;
}
