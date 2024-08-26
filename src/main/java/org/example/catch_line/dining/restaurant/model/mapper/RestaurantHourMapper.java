package org.example.catch_line.dining.restaurant.model.mapper;

import org.example.catch_line.dining.restaurant.model.entity.RestaurantHourEntity;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantHourResponse;
import org.springframework.stereotype.Component;

@Component
public class RestaurantHourMapper {

    public RestaurantHourResponse entityToResponse(RestaurantHourEntity entity) {
        return RestaurantHourResponse.builder()
                .restaurantHourId(entity.getRestaurantHourId())
                .dayOfWeek(entity.getDayOfWeek().getDescription())
                .openTime(entity.getOpenTime())
                .closeTime(entity.getCloseTime())
                .openStatus(entity.getOpenStatus().getDescription())
                .build();
    }
}
