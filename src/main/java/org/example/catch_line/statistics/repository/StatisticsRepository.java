package org.example.catch_line.statistics.repository;

import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.statistics.model.entity.StatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StatisticsRepository extends JpaRepository<StatisticsEntity, Long> {

    List<StatisticsEntity> findByRestaurantRestaurantId(Long restaurantId);

    @Query("SELECT DISTINCT s.restaurant FROM StatisticsEntity s")
    List<RestaurantEntity> findDistinctRestaurants();
}
