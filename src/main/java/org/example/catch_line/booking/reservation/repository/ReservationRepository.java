package org.example.catch_line.booking.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

	List<ReservationEntity> findByMemberMemberIdAndStatus(Long memberId, Status status);

	Optional<ReservationEntity> findByReservationId(Long reservationId);

	List<ReservationEntity> findAllByRestaurantRestaurantIdAndStatus(Long restaurantId,Status status);

	List<ReservationEntity> findAllByStatus(Status status);

	int countByRestaurantAndReservationDateBetween(RestaurantEntity restaurant, LocalDateTime start, LocalDateTime end);

//	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<ReservationEntity> findByRestaurantRestaurantIdAndReservationDate(Long restaurantId, LocalDateTime reservationDate);
}


