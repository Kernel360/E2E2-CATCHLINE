package org.example.catch_line.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.restaurant.model.dto.RestaurantHourResponse;
import org.example.catch_line.restaurant.model.entity.RestaurantHourEntity;
import org.example.catch_line.restaurant.model.mapper.RestaurantHourMapper;
import org.example.catch_line.restaurant.repository.RestaurantHourRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantHourService {

    private final RestaurantHourRepository restaurantHourRepository;
    private final RestaurantHourMapper restaurantHourMapper;

    // 영업 시간 전체 조회
    public List<RestaurantHourResponse> getAllRestaurantHours(Long restaurantId) {
        List<RestaurantHourEntity> restaurantHourList = restaurantHourRepository.findAllByRestaurantRestaurantId(restaurantId);

        return restaurantHourList.stream()
                .map(restaurantHourMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    // 요일 별 영업 시간 조회
    public RestaurantHourResponse getRestaurantHour(Long restaurantId, DayOfWeek dayOfWeek) {
        RestaurantHourEntity entity = restaurantHourRepository.findByRestaurant_RestaurantIdAndDayOfWeek(restaurantId, dayOfWeek);
        return restaurantHourMapper.entityToResponse(entity);
    }

    // 영업 재개

    // 영업 종료
    public void closeBusiness(Long restaurantHourId) {
        RestaurantHourEntity entity = restaurantHourRepository.findById(restaurantHourId).orElseThrow(() -> {
            throw new IllegalArgumentException("restaurantHourId가 없습니다 : " + restaurantHourId);
        });

        entity.closeBusiness();
    }
}
