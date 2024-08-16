package org.example.catch_line.dining.menu.repository;

import org.example.catch_line.dining.menu.model.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

    List<MenuEntity> findAllByRestaurantRestaurantId(Long restaurantId);
}
