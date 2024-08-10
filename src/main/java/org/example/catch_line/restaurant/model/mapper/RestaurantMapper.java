package org.example.catch_line.restaurant.model.mapper;

import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.member.model.vo.PhoneNumber;
import org.example.catch_line.restaurant.model.dto.RestaurantCreateRequest;
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
                .averageRating(entity.getRating().getRating())
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

    // TODO: 식당 위치 조회 구현하기
    public static RestaurantEntity requestToEntity(RestaurantCreateRequest request) {
        return RestaurantEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .phoneNumber(new PhoneNumber(request.getPhoneNumber()))
                .foodType(request.getFoodType())
                .serviceType(request.getServiceType())
                .rating(new Rating(BigDecimal.ZERO))
                .build();
    }
}
