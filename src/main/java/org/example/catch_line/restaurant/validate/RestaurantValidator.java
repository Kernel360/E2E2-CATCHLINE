package org.example.catch_line.restaurant.validate;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantValidator {

    private final RestaurantRepository restaurantRepository;

    public RestaurantEntity checkIfRestaurantPresent(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 식당이 없습니다."));
    }
}
