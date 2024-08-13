package org.example.catch_line.history.validation;

import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.repository.ReservationRepository;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.repository.WaitingRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HistoryValidator {

	private final WaitingRepository waitingRepository;
	private final ReservationRepository reservationRepository;

	public ReservationEntity checkIfReservationPresent(Long reservationId) {
		return reservationRepository.findByReservationId(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("해당 예약은 존재하지 않습니다"));

	}

	public WaitingEntity checkIfWaitingPresent(Long waitingId) {
		return waitingRepository.findByWaitingId(waitingId)
			.orElseThrow(() -> new IllegalArgumentException("해당 웨이팅은 존재하지 않습니다"));
	}

}
