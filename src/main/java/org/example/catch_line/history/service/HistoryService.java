package org.example.catch_line.history.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	private final WaitingRepository waitingRepository;
	private final ReservationRepository reservationRepository;

	public List<HistoryResponse> getAllHistory(HttpSession session, Status status) {

		Long memberId = SessionUtils.getMemberId(session);

		List<HistoryResponse> historyResponseList = new ArrayList<>();

		List<WaitingEntity> allWaiting = waitingRepository.findByMemberMemberIdAndStatus(memberId,status);
		List<ReservationEntity> allReservation = reservationRepository.findByMemberMemberIdAndStatus(memberId,status);

		Map<Long, List<WaitingEntity>> waitingByRestaurant = allWaiting.stream()
			.collect(Collectors.groupingBy(waiting -> waiting.getRestaurant().getRestaurantId()));

		for (Map.Entry<Long, List<WaitingEntity>> entry : waitingByRestaurant.entrySet()) {
			Long restaurantId = entry.getKey();
			List<WaitingEntity> waitingList = entry.getValue();
			List<WaitingEntity> sortedWaitingList = waitingRepository.findByRestaurantRestaurantIdOrderByCreatedAt(restaurantId);
			// 정렬된 대로 순서를 매김
			waitingList.sort(Comparator.comparing(WaitingEntity::getCreatedAt));
			for (int i = 0; i < waitingList.size(); i++) {
				WaitingEntity waiting = waitingList.get(i);
				historyResponseList.add(waitingToHistoryResponse(waiting, i + 1));
			}
		}

		Map<Long, List<ReservationEntity>> reservationByRestaurant = allReservation.stream()
			.collect(Collectors.groupingBy(reservation -> reservation.getRestaurant().getRestaurantId()));

		for (ReservationEntity reservation : allReservation) {
			historyResponseList.add(reservationToHistoryResponse(reservation));
		}

		Collections.sort(historyResponseList, Comparator.comparing(HistoryResponse::getCreatedAt).reversed());

		return historyResponseList;

	}

	private HistoryResponse waitingToHistoryResponse(WaitingEntity entity,int waitingRegistrationId) {
		return HistoryResponse.builder()
			.restaurantId(entity.getRestaurant().getRestaurantId())
			.waitingId(entity.getWaitingId())
			.memberCount(entity.getMemberCount())
			.restaurantName(entity.getRestaurant().getName())
			.status(entity.getStatus())
			.waitingType(entity.getWaitingType())
			.serviceType(entity.getRestaurant().getServiceType())
			.createdAt(entity.getCreatedAt())
			.modifiedAt(entity.getModifiedAt())
			.waitingRegistrationId(waitingRegistrationId)
			.build();
	}

	private HistoryResponse reservationToHistoryResponse(ReservationEntity entity) {
		return HistoryResponse.builder()
			.restaurantId(entity.getRestaurant().getRestaurantId())
			.reservationId(entity.getReservationId())
			.memberCount(entity.getMemberCount())
			.restaurantName(entity.getRestaurant().getName())
			.status(entity.getStatus())
			.reservationDate(entity.getReservationDate())
			.serviceType(entity.getRestaurant().getServiceType())
			.createdAt(entity.getCreatedAt())
			.modifiedAt(entity.getModifiedAt())
			.build();

	}

}
