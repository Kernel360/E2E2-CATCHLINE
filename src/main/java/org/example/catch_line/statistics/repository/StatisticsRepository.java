package org.example.catch_line.statistics.repository;

import org.example.catch_line.statistics.model.entity.StatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatisticsRepository extends JpaRepository<StatisticsEntity, Long> {

    List<StatisticsEntity> findByRestaurant_restaurantId(Long restaurantId);
}
