package org.example.catch_line.history.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.catch_line.common.constant.SessionConst;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.reservation.model.entity.ReservationEntity;
import org.example.catch_line.reservation.repository.ReservationRepository;
import org.example.catch_line.waiting.model.entity.WaitingEntity;
import org.example.catch_line.waiting.repository.WaitingRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryService {

	private final WaitingRepository waitingRepository;
	private final ReservationRepository reservationRepository;

	public List<HistoryResponse> getAllHistory(Long memberId, Status status) {

		List<HistoryResponse> historyResponseList = new ArrayList<>();

		List<WaitingEntity> allWaiting = waitingRepository.findByMemberMemberIdAndStatus(memberId, status);
		List<ReservationEntity> allReservation = reservationRepository.findByMemberMemberIdAndStatus(memberId, status);

		Map<Long, List<WaitingEntity>> waitingByRestaurant = allWaiting.stream()
			.collect(Collectors.groupingBy(waiting -> waiting.getRestaurant().getRestaurantId()));

		for (Map.Entry<Long, List<WaitingEntity>> entry : waitingByRestaurant.entrySet()) {
			List<WaitingEntity> waitingList = entry.getValue();
			// 정렬된 대로 순서를 매김
			waitingList.sort(Comparator.comparing(WaitingEntity::getCreatedAt));

			for (int i = 0; i < waitingList.size(); i++) {
				WaitingEntity waiting = waitingList.get(i);
				int scheduledCount = waitingRepository.countScheduledWaitingBefore(
					waiting.getRestaurant().getRestaurantId(), waiting.getCreatedAt());
				historyResponseList.add(waitingToHistoryResponse(waiting, i + 1, scheduledCount + 1));
			}
		}

		for (ReservationEntity reservation : allReservation) {
			historyResponseList.add(reservationToHistoryResponse(reservation));
		}

		Collections.sort(historyResponseList, Comparator.comparing(HistoryResponse::getCreatedAt).reversed());

		return historyResponseList;

	}

	public HistoryResponse findWaitingDetailById(List<HistoryResponse> historyList, String waitingId) {
		return historyList.stream()
			.filter(h -> h.getWaitingId() != null && waitingId.equals(h.getWaitingId().toString()))  // null 체크 추가
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("오류: 해당 대기 정보를 찾을 수 없습니다."));
	}

	public HistoryResponse findReservationDetailById(List<HistoryResponse> historyList, String reservationId) {
		return historyList.stream()
			.filter(h -> h.getReservationId() != null && reservationId.equals(h.getReservationId().toString()))  // null 체크 추가
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("오류: 해당 대기 정보를 찾을 수 없습니다."));
	}

	private HistoryResponse waitingToHistoryResponse(WaitingEntity entity, int waitingRegistrationId,int myWaitingPosition) {
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
			.myWaitingPosition(myWaitingPosition)
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

