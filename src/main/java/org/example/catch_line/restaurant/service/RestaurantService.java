package org.example.catch_line.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.mapper.RestaurantMapper;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantResponse createRestaurant(RestaurantCreateRequest request) {
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
                .reviewCount(0L)
                .scrapCount(0L)
                .build();
    }
}
