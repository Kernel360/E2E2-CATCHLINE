package org.example.catch_line.dining.restaurant.validation;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantHourEntity;
import org.example.catch_line.dining.restaurant.repository.RestaurantHourRepository;
import org.example.catch_line.dining.restaurant.repository.RestaurantRepository;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.exception.dining.RestaurantHourNotFoundException;
import org.example.catch_line.exception.dining.RestaurantNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantValidator {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantHourRepository restaurantHourRepository;

    public RestaurantEntity checkIfRestaurantPresent(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
    }

    public RestaurantHourEntity checkIfRestaurantHourPresent(Long restaurantHourId) {
        return restaurantHourRepository.findById(restaurantHourId)
                .orElseThrow(() -> new RestaurantHourNotFoundException(restaurantHourId));
    }

}
