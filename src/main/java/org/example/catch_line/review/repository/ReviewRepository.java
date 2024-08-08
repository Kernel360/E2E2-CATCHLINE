package org.example.catch_line.review.repository;

import org.example.catch_line.review.model.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Long countByRestaurantRestaurantId(Long restaurantId);

    @Query("SELECT r.rating FROM ReviewEntity r WHERE r.restaurant.restaurantId = :restaurantId")
    List<Integer> findRatingsByRestaurantId(@Param("restaurantId") Long restaurantId);

    List<ReviewEntity> findAllByRestaurantRestaurantIdOrderByCreatedAtDesc(Long restaurantId);
}
