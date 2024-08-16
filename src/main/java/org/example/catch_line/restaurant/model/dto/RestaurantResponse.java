package org.example.catch_line.restaurant.model.dto;

import lombok.*;
import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class RestaurantResponse {

    private Long restaurantId;
    private String name;
    private String description;
    private BigDecimal averageRating;
    private String phoneNumber;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Long scrapCount;
    private Long reviewCount;
    private FoodType foodType;
    private ServiceType serviceType;
    private boolean hasScrapped;
}
