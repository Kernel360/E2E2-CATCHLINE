package org.example.catch_line.dining.restaurant.repository;

import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.constant.FoodType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    Optional<RestaurantEntity> findByName(String name);

    Optional<RestaurantEntity> findByOwnerOwnerId(Long ownerId);

    List<RestaurantEntity> findAllByOwnerOwnerId(Long ownerId);

    Page<RestaurantEntity> findAllByNameContaining(String name, Pageable pageable);

    Page<RestaurantEntity> findAllByFoodType(FoodType foodType, Pageable pageable);

}
