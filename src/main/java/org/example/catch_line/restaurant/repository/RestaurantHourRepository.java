package org.example.catch_line.restaurant.repository;

import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.entity.RestaurantHourEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantHourRepository extends JpaRepository<RestaurantHourEntity, Long> {
}
