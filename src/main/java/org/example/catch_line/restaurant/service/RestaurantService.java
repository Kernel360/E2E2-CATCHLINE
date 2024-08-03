package org.example.catch_line.restaurant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.mapper.RestaurantMapper;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantResponse createRestaurant(RestaurantCreateRequest request) {
        // TODO: 식당 이름은 중복되도 되지 않을까?
        if(restaurantRepository.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("중복된 식당 이름입니다.");
        }

        RestaurantEntity entity = toEntity(request);
        RestaurantEntity savedEntity = restaurantRepository.save(entity);
        return restaurantMapper.toDto(savedEntity);
    }

    public RestaurantResponse findRestaurant(Long restaurantId) {
        RestaurantEntity entity = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        return restaurantMapper.toDto(entity);
    }

    public void deleteRestaurant(Long restaurantId) {
        restaurantRepository.deleteById(restaurantId);
    }

    // TODO: 식당 위치 조회 구현하기

    private static RestaurantEntity toEntity(RestaurantCreateRequest request) {
        return RestaurantEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .phoneNumber(request.getPhoneNumber())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .foodType(request.getFoodType())
                .serviceType(request.getServiceType())
                .rating(BigDecimal.valueOf(0))
                .build();
    }
}
