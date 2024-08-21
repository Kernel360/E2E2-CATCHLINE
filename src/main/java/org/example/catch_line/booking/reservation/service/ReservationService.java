package org.example.catch_line.booking.reservation.service;

import jakarta.transaction.Transactional;
import org.example.catch_line.booking.reservation.model.dto.ReservationUpdateRequest;
import org.example.catch_line.booking.reservation.model.dto.ReservationResponse;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.model.mapper.ReservationResponseMapper;
import org.example.catch_line.booking.reservation.repository.ReservationRepository;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.history.validation.HistoryValidator;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.validation.MemberValidator;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationResponseMapper reservationResponseMapper;
	private final HistoryValidator historyValidator;
	private final MemberValidator memberValidator;
	private final RestaurantValidator restaurantValidator;

	public boolean isReservationTimeConflict(Long restaurantId, LocalDateTime reservationDate) {
		List<ReservationEntity> existingReservations = reservationRepository.findByRestaurantRestaurantIdAndReservationDate(restaurantId, reservationDate);

		return !existingReservations.isEmpty();
	}

	public ReservationResponse addReserve(Long restaurantId, ReservationUpdateRequest reservationUpdateRequest, Long memberId) {
		if (isReservationTimeConflict(restaurantId, reservationUpdateRequest.getReservationDate())) {
			throw new CatchLineException("이미 해당 시간에 예약이 존재합니다");
		}

		MemberEntity member = memberValidator.checkIfMemberPresent(memberId);
		RestaurantEntity restaurant = restaurantValidator.checkIfRestaurantPresent(restaurantId);

		ReservationEntity reservation = ReservationEntity.builder()
			.member(member)
			.restaurant(restaurant)
			.memberCount(reservationUpdateRequest.getMemberCount())
			.status(Status.SCHEDULED)
			.reservationDate(reservationUpdateRequest.getReservationDate())
			.build();
		ReservationEntity savedEntity = reservationRepository.save(reservation);

		return reservationResponseMapper.convertToResponse(savedEntity);
	}


	public void completedReservation(Long reservationId) {
		ReservationEntity reservationEntity = historyValidator.checkIfReservationPresent(reservationId);
		reservationEntity.changeReservationStatus(Status.COMPLETED);
		reservationRepository.save(reservationEntity);
	}

	@Transactional
	@Scheduled(cron = "0 0 0 * * ?")
	public void updateScheduledReservation() {
		List<ReservationEntity> reservationEntities = reservationRepository.findAllByStatus(Status.SCHEDULED);

		for (ReservationEntity reservationEntity : reservationEntities) {
			if(LocalDateTime.now().isAfter(reservationEntity.getReservationDate())){
				reservationEntity.changeReservationStatus(Status.CANCELED);
			}
		}
		reservationRepository.saveAll(reservationEntities);
	}

	public void cancelReservation(Long reservationId) {
		ReservationEntity reservationEntity = historyValidator.checkIfReservationPresent(reservationId);
		reservationEntity.changeReservationStatus(Status.CANCELED);
		reservationRepository.save(reservationEntity);
	}

	public ReservationEntity findReservationById(Long reservationId) {
		ReservationEntity reservation = historyValidator.checkIfReservationPresent(reservationId);

		return reservation;
	}

}
