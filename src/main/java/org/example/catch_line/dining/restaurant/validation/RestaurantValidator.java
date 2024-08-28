package org.example.catch_line.dining.restaurant.validation;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantHourEntity;
import org.example.catch_line.dining.restaurant.repository.RestaurantHourRepository;
import org.example.catch_line.dining.restaurant.repository.RestaurantRepository;
import org.example.catch_line.exception.CatchLineException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantValidator {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantHourRepository restaurantHourRepository;

    public RestaurantEntity checkIfRestaurantPresent(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CatchLineException("해당하는 식당이 없습니다."));
    }

    public RestaurantHourEntity checkIfRestaurantHourPresent(Long restaurantHourId) {
        return restaurantHourRepository.findById(restaurantHourId)
                .orElseThrow(() -> new CatchLineException("영업 시간이 존재하지 않습니다"));
    }

}
