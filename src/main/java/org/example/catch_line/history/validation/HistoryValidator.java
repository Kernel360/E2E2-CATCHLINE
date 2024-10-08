package org.example.catch_line.history.validation;

import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.repository.ReservationRepository;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.repository.WaitingRepository;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.exception.authorizaion.UnauthorizedException;
import org.example.catch_line.exception.booking.ReservationException;
import org.example.catch_line.exception.booking.WaitingException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class HistoryValidator {

	private final WaitingRepository waitingRepository;
	private final ReservationRepository reservationRepository;

	public ReservationEntity checkIfReservationPresent(Long reservationId) {
		return reservationRepository.findByReservationId(reservationId)
			.orElseThrow(() -> new ReservationException("해당 예약은 존재하지 않습니다"));
	}

	public WaitingEntity checkIfWaitingPresent(Long waitingId) {
		return waitingRepository.findByWaitingId(waitingId)
			.orElseThrow(() -> new WaitingException("해당 웨이팅은 존재하지 않습니다"));
	}

	public void validateWaitingOwnership(Long memberId, Long waitingId) {
		WaitingEntity waitingEntity = waitingRepository.findByWaitingId(waitingId)
				.orElseThrow(() -> new WaitingException("해당 웨이팅은 존재하지 않습니다"));

		if (!Objects.equals(waitingEntity.getMember().getMemberId(), memberId)) {
			throw new UnauthorizedException();
		}
	}

	public void validateReservationOwnership(Long memberId, Long reservationId) {
		ReservationEntity reservationEntity = reservationRepository.findByReservationId(reservationId)
				.orElseThrow(() -> new ReservationException("해당 예약은 존재하지 않습니다"));

		if (!Objects.equals(reservationEntity.getMember().getMemberId(), memberId)) {
			throw new UnauthorizedException();
		}
	}

}
