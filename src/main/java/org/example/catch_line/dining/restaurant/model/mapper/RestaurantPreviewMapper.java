package org.example.catch_line.dining.restaurant.model.mapper;

import org.example.catch_line.dining.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RestaurantPreviewMapper {

    public RestaurantPreviewResponse entityToResponse(RestaurantEntity restaurantEntity, BigDecimal averageRating, Long reviewCount, Long scrapCount) {
        return RestaurantPreviewResponse.builder()
                .restaurantId(restaurantEntity.getRestaurantId())
                .name(restaurantEntity.getName())
                .averageRating(averageRating)
                .reviewCount(reviewCount)
                .scrapCount(scrapCount)
                .serviceType(restaurantEntity.getServiceType().getDescription())
                .foodType(restaurantEntity.getFoodType().getDescription())
                .build();
    }

    public RestaurantPreviewResponse entityToResponse(RestaurantEntity restaurantEntity) {
        return RestaurantPreviewResponse.builder()
                .restaurantId(restaurantEntity.getRestaurantId())
                .name(restaurantEntity.getName())
                .averageRating(restaurantEntity.getRating().getRating())
                .reviewCount(restaurantEntity.getReviewCount())
                .scrapCount(restaurantEntity.getScrapCount())
                .serviceType(restaurantEntity.getServiceType().getDescription())
                .foodType(restaurantEntity.getFoodType().getDescription())
                .build();
    }
}
