package org.example.catch_line.restaurant.service;

import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class RestaurantServiceTest {

    @Autowired RestaurantService restaurantService;
    @Autowired RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("식당 생성 테스트")
    @Transactional
    void restaurant_create() {
        // given
        RestaurantCreateRequest request = getRestaurantCreateRequest();

        // when
        RestaurantResponse restaurant = restaurantService.createRestaurant(request);

        // then
        assertThat(restaurant).isNotNull();
        assertThat(restaurant.getRestaurantId()).isNotNull();

        RestaurantEntity savedEntity = restaurantRepository.findById(restaurant.getRestaurantId()).get();

        assertThat(restaurant.getRestaurantId()).isEqualTo(savedEntity.getRestaurantId());
    }

    @Test
    @DisplayName("식당 이름 중복 테스트")
    @Transactional
    void restaurant_create_duplicate_name() {
        RestaurantCreateRequest request1 = getRestaurantCreateRequest();
        RestaurantCreateRequest request2 = getRestaurantCreateRequest();
        restaurantService.createRestaurant(request1);

        assertThatThrownBy(() -> restaurantService.createRestaurant(request2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("식당 상세 조회 테스트")
    @Transactional
    void restaurant_find() {
        // given
        RestaurantCreateRequest request = getRestaurantCreateRequest();

        // when
        RestaurantResponse restaurant = restaurantService.createRestaurant(request);
        Long restaurantId = restaurant.getRestaurantId();
        RestaurantResponse response = restaurantService.findRestaurant(restaurantId);

        // then
        assertThat(restaurantId).isEqualTo(response.getRestaurantId());
        assertThat(restaurant.getName()).isEqualTo(response.getName());
        assertThat(restaurant.getDescription()).isEqualTo(response.getDescription());
        assertThat(restaurant.getPhoneNumber()).isEqualTo(response.getPhoneNumber());
        assertThat(restaurant.getAverageRating()).isEqualTo(response.getAverageRating());
    }

    @Test
    @DisplayName("식당 삭제 테스트")
    @Transactional
    void restaurant_delete() {
        // given
        RestaurantCreateRequest request = getRestaurantCreateRequest();

        // when
        RestaurantResponse restaurant = restaurantService.createRestaurant(request);
        Long restaurantId = restaurant.getRestaurantId();
        restaurantService.deleteRestaurant(restaurantId);

        // then
        assertThat(restaurantRepository.findById(restaurantId)).isEmpty();
    }

    private static RestaurantCreateRequest getRestaurantCreateRequest() {
        RestaurantCreateRequest request = RestaurantCreateRequest.builder()
                .description("식당 소개")
                .name("한식집")
                .latitude(BigDecimal.ZERO)
                .longitude(BigDecimal.ZERO)
                .phoneNumber("0212341234")
                .foodType(FoodType.KOREAN)
                .serviceType(ServiceType.WAITING)
                .build();
        return request;
    }
}