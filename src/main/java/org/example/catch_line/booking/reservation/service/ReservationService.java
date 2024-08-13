package org.example.catch_line.booking.reservation.service;

import org.example.catch_line.booking.reservation.model.dto.ReservationRequest;
import org.example.catch_line.booking.reservation.model.dto.ReservationResponse;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.model.mapper.ReservationResponseMapper;
import org.example.catch_line.booking.reservation.repository.ReservationRepository;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.exception.booking.ServiceTypeException;
import org.example.catch_line.history.validation.HistoryValidator;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.validation.MemberValidator;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.restaurant.validation.RestaurantValidator;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationResponseMapper reservationResponseMapper;
	private final HistoryValidator historyValidator;
	private final MemberValidator memberValidator;
	private final RestaurantValidator restaurantValidator;

	public ReservationResponse addReserve(Long restaurantId, ReservationRequest reservationRequest, Long memberId) {

		MemberEntity member = memberValidator.checkIfMemberPresent(memberId);
		RestaurantEntity restaurant = restaurantValidator.checkIfRestaurantPresent(restaurantId);

		ReservationEntity reservation = ReservationEntity.builder()
			.member(member)
			.restaurant(restaurant)
			.memberCount(reservationRequest.getMemberCount())
			.status(Status.SCHEDULED)
			.reservationDate(reservationRequest.getReservationDate())
			.build();
		ReservationEntity savedEntity = reservationRepository.save(reservation);

		return reservationResponseMapper.convertToResponse(savedEntity);
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
