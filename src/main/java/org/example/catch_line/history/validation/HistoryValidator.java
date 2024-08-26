package org.example.catch_line.history.validation;

import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.repository.ReservationRepository;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.repository.WaitingRepository;
import org.example.catch_line.exception.CatchLineException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HistoryValidator {

	private final WaitingRepository waitingRepository;
	private final ReservationRepository reservationRepository;

	public ReservationEntity checkIfReservationPresent(Long reservationId) {
		return reservationRepository.findByReservationId(reservationId)
			.orElseThrow(() -> new CatchLineException("해당 예약은 존재하지 않습니다"));

	}

	public WaitingEntity checkIfWaitingPresent(Long waitingId) {
		return waitingRepository.findByWaitingId(waitingId)
			.orElseThrow(() -> new CatchLineException("해당 웨이팅은 존재하지 않습니다"));
	}

	public void validateWaitingOwnership(Long memberId, Long waitingId) {
		WaitingEntity waitingEntity = waitingRepository.findByWaitingId(waitingId)
				.orElseThrow(() -> new CatchLineException("해당 웨이팅은 존재하지 않습니다"));

		if (!waitingEntity.getMember().getMemberId().equals(memberId)) {
			throw new CatchLineException("해당 대기를 삭제할 권한이 없습니다");
		}
	}

	public void validateReservationOwnership(Long memberId, Long reservationId) {
		ReservationEntity reservationEntity = reservationRepository.findByReservationId(reservationId)
				.orElseThrow(() -> new CatchLineException("해당 예약은 존재하지 않습니다"));

		if (!reservationEntity.getMember().getMemberId().equals(memberId)) {
			throw new CatchLineException("해당 예약을 삭제할 권한이 없습니다");
		}
	}

}
