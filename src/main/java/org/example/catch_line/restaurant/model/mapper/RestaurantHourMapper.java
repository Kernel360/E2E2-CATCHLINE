package org.example.catch_line.restaurant.model.mapper;

import org.example.catch_line.restaurant.model.dto.RestaurantHourResponse;
import org.example.catch_line.restaurant.model.entity.RestaurantHourEntity;
import org.springframework.stereotype.Service;

@Service
public class RestaurantHourMapper {

    public RestaurantHourResponse entityToResponse(RestaurantHourEntity entity) {
        return RestaurantHourResponse.builder()
                .restaurantHourId(entity.getRestaurantHourId())
                .dayOfWeek(entity.getDayOfWeek())
                .openTime(entity.getOpenTime())
                .closeTime(entity.getCloseTime())
                .openStatus(entity.getOpenStatus())
                .build();
    }
}
