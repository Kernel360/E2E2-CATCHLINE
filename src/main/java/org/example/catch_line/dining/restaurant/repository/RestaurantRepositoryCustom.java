package org.example.catch_line.dining.restaurant.repository;

import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantRepositoryCustom {
    Page<RestaurantEntity> findRestaurantsByCriteria(Pageable pageable, String criteria, String type, String keyword);
}
