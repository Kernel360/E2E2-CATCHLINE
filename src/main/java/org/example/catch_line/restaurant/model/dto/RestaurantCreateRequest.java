package org.example.catch_line.restaurant.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;

@Getter
@AllArgsConstructor
@Builder
public class RestaurantCreateRequest {

    @NotBlank(message = "가게 이름은 비어있을 수 없습니다.")
    private String name;

    @NotBlank(message = "가게 소개 글은 비어있을 수 없습니다.")
    private String description;

    @NotBlank(message = "전화번호는 비어있을 수 없습니다.")
    private String phoneNumber;

    @NotBlank(message = "주소는 비어있을 수 없습니다.")
    private String address;

    @NotNull(message = "한식, 중식, 일식, 양식 중에 선택해주세요.")
    private FoodType foodType;

    @NotNull(message = "줄서기, 예약 중에 선택해주세요.")
    private ServiceType serviceType;

}
