package org.example.catch_line.restaurant.model.mapper;

import org.example.catch_line.member.model.vo.PhoneNumber;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public class RestaurantMapper {

    public static RestaurantResponse entityToResponse(RestaurantEntity entity) {
        return RestaurantResponse.builder()
                .restaurantId(entity.getRestaurantId())
                .name(entity.getName())
                .description(entity.getDescription())
                .averageRating(entity.getRating())
                .phoneNumber(entity.getPhoneNumber().getPhoneNumberValue())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .scrapCount(entity.getScrapCount())
                .reviewCount(entity.getReviewCount())
                .foodType(entity.getFoodType())
                .serviceType(entity.getServiceType())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }

    public static RestaurantResponse entityToResponse(RestaurantEntity entity, BigDecimal averageRating, Long reviewCount) {
        return RestaurantResponse.builder()
                .restaurantId(entity.getRestaurantId())
                .name(entity.getName())
                .description(entity.getDescription())
                .averageRating(averageRating)
                .phoneNumber(entity.getPhoneNumber().getPhoneNumberValue())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .scrapCount(entity.getScrapCount())
                .reviewCount(reviewCount)
                .foodType(entity.getFoodType())
                .serviceType(entity.getServiceType())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }
}
