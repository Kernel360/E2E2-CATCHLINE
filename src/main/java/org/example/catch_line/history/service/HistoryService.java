package org.example.catch_line.history.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.common.constant.SessionConst;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.reservation.model.dto.ReservationResponse;
import org.example.catch_line.reservation.model.entity.ReservationEntity;
import org.example.catch_line.reservation.repository.ReservationRepository;
import org.example.catch_line.reservation.service.ReservationService;
import org.example.catch_line.waiting.model.dto.WaitingResponse;
import org.example.catch_line.waiting.model.entity.WaitingEntity;
import org.example.catch_line.waiting.repository.WaitingRepository;
import org.example.catch_line.waiting.service.WaitingService;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryService {

	private final WaitingService waitingService;
	private final ReservationService reservationService;

	private final WaitingRepository waitingRepository;
	private final ReservationRepository reservationRepository;

	public List<HistoryResponse> getAllHistory(HttpSession session, Status status) {

		Long memberId = SessionUtils.getMemberId(session);

		List<HistoryResponse> historyResponseList = new ArrayList<>();

		// List<WaitingResponse> allWaiting = waitingService.getAllWaiting(memberId);
		// List<ReservationResponse> allReservation = reservationService.getAllReservation(memberId);

		List<WaitingEntity> allWaiting = waitingRepository.findByMemberMemberIdAndStatus(memberId,status);
		List<ReservationEntity> allReservation = reservationRepository.findByMemberMemberIdAndStatus(memberId,status);

		for (WaitingEntity waiting : allWaiting) {
			historyResponseList.add(waitingToHistoryResponse(waiting));
		}

		for (ReservationEntity reservation : allReservation) {
			historyResponseList.add(reservationToHistoryResponse(reservation));
		}



		Collections.sort(historyResponseList, Comparator.comparing(HistoryResponse::getCreatedAt).reversed());

		return historyResponseList;
	}

	private HistoryResponse waitingToHistoryResponse(WaitingEntity response) {
		return HistoryResponse.builder()
			.waitingId(response.getWaitingId())
			.memberCount(response.getMemberCount())
			.status(response.getStatus())
			.waitingType(response.getWaitingType())
			.createdAt(response.getCreatedAt())
			.modifiedAt(response.getModifiedAt())
			.build();

	}

	private HistoryResponse reservationToHistoryResponse(ReservationEntity response) {
		return HistoryResponse.builder()
			.reservationId(response.getReservationId())
			.memberCount(response.getMemberCount())
			.status(response.getStatus())
			.reservationDate(response.getReservationDate())
			.createdAt(response.getCreatedAt())
			.modifiedAt(response.getModifiedAt())
			.build();

	}

}
