package org.example.catch_line.restaurant.model.dto;

import lombok.*;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
