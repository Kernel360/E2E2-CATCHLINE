package org.example.catch_line.restaurant.repository;

import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    Optional<RestaurantEntity> findByName(String name);
}
