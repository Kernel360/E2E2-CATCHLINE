package org.example.catch_line.restaurant.model.mapper;

import org.example.catch_line.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;

import java.math.BigDecimal;

public class RestaurantPreviewMapper {

    public static RestaurantPreviewResponse entityToResponse(RestaurantEntity restaurantEntity, BigDecimal averageRating, Long reviewCount) {
        return RestaurantPreviewResponse.builder()
                .restaurantId(restaurantEntity.getRestaurantId())
                .name(restaurantEntity.getName())
                .averageRating(averageRating)
                .reviewCount(reviewCount)
                .serviceType(restaurantEntity.getServiceType().getDescription())
                .foodType(restaurantEntity.getFoodType().getDescription())
                .build();
    }
}
