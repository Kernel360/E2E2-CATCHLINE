package org.example.catch_line.dining.restaurant.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@Builder
public class RestaurantHourResponse {

    private Long restaurantHourId;
    private String dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String openStatus;
}
