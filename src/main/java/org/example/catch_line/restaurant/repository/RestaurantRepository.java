package org.example.catch_line.restaurant.repository;

import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

}
