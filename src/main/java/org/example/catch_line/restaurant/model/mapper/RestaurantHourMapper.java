package org.example.catch_line.restaurant.model.mapper;

import org.example.catch_line.restaurant.model.dto.RestaurantHourResponse;
import org.example.catch_line.restaurant.model.entity.RestaurantHourEntity;
import org.springframework.stereotype.Service;

public class RestaurantHourMapper {

    public static RestaurantHourResponse entityToResponse(RestaurantHourEntity entity) {
        return RestaurantHourResponse.builder()
                .restaurantHourId(entity.getRestaurantHourId())
                .dayOfWeek(entity.getDayOfWeek().getDescription())
                .openTime(entity.getOpenTime())
                .closeTime(entity.getCloseTime())
                .openStatus(entity.getOpenStatus().getDescription())
                .build();
    }
}
