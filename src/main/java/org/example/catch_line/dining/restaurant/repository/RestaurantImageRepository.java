package org.example.catch_line.dining.restaurant.repository;

import org.example.catch_line.dining.restaurant.model.entity.RestaurantImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantImageRepository extends JpaRepository<RestaurantImageEntity, Long> {
    List<RestaurantImageEntity> findAllByRestaurantRestaurantId(Long restaurantId);
}
