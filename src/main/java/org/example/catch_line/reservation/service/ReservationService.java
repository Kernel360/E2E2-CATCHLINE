package org.example.catch_line.reservation.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.catch_line.common.constant.Status;
import org.example.catch_line.reservation.model.dto.ReservationRequest;
import org.example.catch_line.reservation.model.dto.ReservationResponse;
import org.example.catch_line.reservation.model.entity.ReservationEntity;
import org.example.catch_line.reservation.model.mapper.ReservationResponseMapper;
import org.example.catch_line.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationResponseMapper reservationResponseMapper;

	public ReservationResponse addReserve(ReservationRequest reservationRequest) {
		ReservationEntity reservation = ReservationEntity.builder()
			.memberCount(reservationRequest.getMemberCount())
			.status(Status.SCHEDULED)
			.reservationDate(reservationRequest.getReservationDate())
			.build();
		ReservationEntity savedEntity = reservationRepository.save(reservation);

		return reservationResponseMapper.convertToResponse(savedEntity);
	}

	public List<ReservationResponse> getAllReservation() {
		List<ReservationEntity> reservationEntities = reservationRepository.findAll();

		return reservationEntities.stream()
			.map(reservationResponseMapper::convertToResponse)
			.collect(Collectors.toList());
	}

	public ReservationResponse getReservationById(Long id) {
		ReservationEntity reservationEntity = reservationRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("정확한 아이디가 아닙니다: " + id));
		return reservationResponseMapper.convertToResponse(reservationEntity);
	}

	public void cancelReservation(Long id) {
		ReservationEntity reservationEntity = reservationRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("예약 아이디가 다릅니다: " + id));
		reservationEntity.setStatus(Status.CANCELED);
		reservationRepository.save(reservationEntity);
	}

	public void editReservation(Long id, ReservationRequest reservationRequest) {
		ReservationEntity reservationEntity = reservationRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("예약 아이디가 다릅니다: " + id));

		reservationEntity.updateReservation(reservationRequest);
		reservationRepository.save(reservationEntity);
	}

}
