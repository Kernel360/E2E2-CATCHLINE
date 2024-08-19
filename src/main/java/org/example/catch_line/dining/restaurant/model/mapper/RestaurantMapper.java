package org.example.catch_line.dining.restaurant.model.mapper;

import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;

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
                .hasScrapped(false)
                .build();
    }

    public static RestaurantResponse entityToResponse(RestaurantEntity entity, boolean hasScrapped) {
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
                .hasScrapped(hasScrapped)
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

    public static RestaurantEntity requestToEntity(RestaurantCreateRequest request, BigDecimal latitude, BigDecimal longitude,OwnerEntity owner) {

        return RestaurantEntity.builder()
            .name(request.getName())
            .description(request.getDescription())
            .phoneNumber(new PhoneNumber(request.getPhoneNumber()))
            .foodType(request.getFoodType())
            .serviceType(request.getServiceType())
            .rating(new Rating(BigDecimal.ZERO))
            .longitude(longitude)
            .latitude(latitude)
            .owner(owner)
            .build();
    }


}