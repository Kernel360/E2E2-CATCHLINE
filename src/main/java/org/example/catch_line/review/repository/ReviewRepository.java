package org.example.catch_line.review.repository;

import org.example.catch_line.review.model.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Long countByRestaurantRestaurantId(Long restaurantId);

    List<ReviewEntity> findAllByRestaurantRestaurantId(Long restaurantId);

    List<ReviewEntity> findAllByRestaurantRestaurantIdOrderByCreatedAtDesc(Long restaurantId);
}
