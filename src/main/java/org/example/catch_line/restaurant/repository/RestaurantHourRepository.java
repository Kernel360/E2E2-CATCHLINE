package org.example.catch_line.restaurant.repository;

import org.example.catch_line.restaurant.model.dto.RestaurantHourResponse;
import org.example.catch_line.restaurant.model.entity.RestaurantHourEntity;
import org.example.catch_line.restaurant.model.entity.constant.DayOfWeeks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantHourRepository extends JpaRepository<RestaurantHourEntity, Long> {

    List<RestaurantHourEntity> findAllByRestaurantRestaurantId(Long restaurantId);

    RestaurantHourEntity findByRestaurant_RestaurantIdAndDayOfWeek(Long restaurantId, DayOfWeeks dayOfWeek);

}
