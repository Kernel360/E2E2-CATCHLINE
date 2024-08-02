package org.example.catch_line.restaurant.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.example.catch_line.restaurant.model.entity.FoodType;
import org.example.catch_line.restaurant.model.entity.ServiceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantResponse {

    @NotEmpty
    private Long restaurantId;

    @NotEmpty(message = "가게 이름은 비어있을 수 없습니다.")
    private String name;

    @NotEmpty(message = "가게 소개 글은 비어있을 수 없습니다.")
    private String description;

    @Min(0) @Max(5)
    private BigDecimal rating;

    @NotEmpty(message = "전화번호는 비어있을 수 없습니다.")
    @Pattern(regexp = "\\d{10,11}", message = "전화번호는 10자 이상 11자 이하의 숫자만 입력하세요.")
    private String phoneNumber;

    @Min(0) @Max(5)
    private BigDecimal latitude;

    @Min(0) @Max(5)
    private BigDecimal longitude;


    private Long scrapCount;
    private Long reviewCount;
    private FoodType foodType;
    private ServiceType serviceType;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
