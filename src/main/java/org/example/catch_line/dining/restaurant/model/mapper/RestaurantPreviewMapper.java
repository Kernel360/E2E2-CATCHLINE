package org.example.catch_line.dining.restaurant.model.mapper;

import org.example.catch_line.dining.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;

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

    public static RestaurantPreviewResponse entityToResponse(RestaurantEntity restaurantEntity) {
        return RestaurantPreviewResponse.builder()
                .restaurantId(restaurantEntity.getRestaurantId())
                .name(restaurantEntity.getName())
                .averageRating(restaurantEntity.getRating().getRating())
                .reviewCount(restaurantEntity.getReviewCount())
                .serviceType(restaurantEntity.getServiceType().getDescription())
                .foodType(restaurantEntity.getFoodType().getDescription())
                .build();
    }
}
