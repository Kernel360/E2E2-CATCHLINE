package org.example.catch_line.restaurant.model.dto;

import jakarta.validation.constraints.*;
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

    @NotNull(message = "위도는 비어있을 수 없습니다.")
    private BigDecimal latitude;

    @NotNull(message = "경도는 비어있을 수 없습니다.")
    private BigDecimal longitude;

    @NotEmpty
    private Long scrapCount;

    @NotEmpty
    private Long reviewCount;

    @NotNull(message = "한식, 중식, 일식, 양식 중에 선택해주세요.")
    private FoodType foodType;

    @NotNull(message = "줄서기, 예약 중에 선택해주세요.")
    private ServiceType serviceType;

    @NotEmpty
    private LocalDateTime createdAt;

    @NotEmpty
    private LocalDateTime modifiedAt;
}
