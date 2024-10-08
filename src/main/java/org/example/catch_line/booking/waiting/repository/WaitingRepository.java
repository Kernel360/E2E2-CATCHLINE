package org.example.catch_line.booking.waiting.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingRepository extends JpaRepository<WaitingEntity, Long> {

	Optional<WaitingEntity> findByWaitingId(long waitingId);

	long countByRestaurantAndCreatedAtBetweenAndCreatedAtBefore(RestaurantEntity restaurant, LocalDateTime startOfDay,
		LocalDateTime endOfDay, LocalDateTime createdAt);

	long countByRestaurantAndStatusAndCreatedAtBefore(RestaurantEntity restaurant, Status status,
		LocalDateTime createdAt);

	List<WaitingEntity> findByMemberMemberIdAndStatusAndCreatedAtBetween(Long memberId, Status status,
		LocalDateTime startOfDay, LocalDateTime endOfDay);

	List<WaitingEntity> findByMemberMemberIdAndStatus(Long memberId, Status status);

	boolean existsByMemberMemberIdAndStatus(Long memberId, Status status);

	List<WaitingEntity> findAllByRestaurantRestaurantIdAndStatus(Long restaurantId,Status status);

	List<WaitingEntity> findAllByStatus(Status status);

	int countByRestaurantAndModifiedAtBetween(RestaurantEntity restaurant, LocalDateTime start, LocalDateTime end);

}
