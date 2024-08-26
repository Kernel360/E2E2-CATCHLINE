package org.example.catch_line.dining.restaurant.model.mapper;

import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RestaurantMapper {

    public RestaurantResponse entityToResponse(RestaurantEntity entity) {
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

    public RestaurantResponse entityToResponse(RestaurantEntity entity, boolean hasScrapped) {
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

    public RestaurantEntity requestToEntity(RestaurantCreateRequest request, OwnerEntity ownerEntity, BigDecimal latitude, BigDecimal longitude) {
        return new RestaurantEntity(request.getName(), request.getDescription(), new Rating(BigDecimal.ZERO),
                new PhoneNumber(request.getPhoneNumber()), request.getFoodType(), request.getServiceType(),
                ownerEntity, latitude, longitude);
    }

}
