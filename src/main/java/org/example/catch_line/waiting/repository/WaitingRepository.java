package org.example.catch_line.waiting.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.example.catch_line.common.constant.Status;
import org.example.catch_line.waiting.model.entity.WaitingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingRepository extends JpaRepository<WaitingEntity, Long> {
	List<WaitingEntity> findByMemberMemberId(Long memberId);

	List<WaitingEntity> findByMemberMemberIdAndStatus(Long memberId, Status status);

	List<WaitingEntity> findByRestaurantRestaurantIdOrderByCreatedAt(Long restaurantId);

	@Query("SELECT COUNT(w) FROM WaitingEntity w WHERE w.restaurant.restaurantId = :restaurantId AND w.status = 'Scheduled' AND w.createdAt < :createdAt")
	int countScheduledWaitingBefore(@Param("restaurantId") Long restaurantId, @Param("createdAt")LocalDateTime createdAt);

}
