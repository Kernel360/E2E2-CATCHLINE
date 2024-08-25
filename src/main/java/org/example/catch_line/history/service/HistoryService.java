package org.example.catch_line.history.service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.repository.ReservationRepository;
import org.example.catch_line.booking.reservation.service.ReservationService;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.repository.WaitingRepository;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.exception.booking.DuplicateReservationTimeException;
import org.example.catch_line.exception.booking.HistoryException;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.history.model.mapper.HistoryMapper;
import org.example.catch_line.history.validation.HistoryValidator;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryService {

	private final WaitingRepository waitingRepository;
	private final ReservationRepository reservationRepository;
	private final HistoryMapper historyMapper;

	public List<HistoryResponse> getAllHistory(Long memberId, Status status) {
		List<HistoryResponse> historyResponseList = new ArrayList<>();

		// 오늘 등록된 상태가 SCHEDULED인 웨이팅 리스트 가져오기
		List<WaitingEntity> scheduledWaitingEntities = getWaitingEntitiesScheduledForToday(memberId, status);
		List<ReservationEntity> allReservation = getReservationEntities(memberId, status);

		// 각 웨이팅 엔티티를 HistoryResponse로 변환
		scheduledWaitingEntities.forEach(waiting ->
			historyResponseList.add(historyMapper.entityToHistoryResponse(waiting, calculateWaitingRegistrationId(waiting, getStartOfDay(), getEndOfDay()), calculateMyWaitingPosition(waiting)))
		);

		// 각 예약 엔티티를 HistoryResponse로 변환
		allReservation.forEach(reservation ->
			historyResponseList.add(historyMapper.reservationToHistoryResponse(reservation))
		);

		// 생성일 기준으로 내림차순 정렬
		sortHistoryResponsesByCreatedAt(historyResponseList);

		return historyResponseList;
	}

	public List<HistoryResponse> findByRestaurantId(Long restaurantId,Status status) {
		List<ReservationEntity> reservationEntities = reservationRepository.findAllByRestaurantRestaurantIdAndStatus(
			restaurantId,status);
		List<WaitingEntity> waitingEntities = waitingRepository.findAllByRestaurantRestaurantIdAndStatus(
			restaurantId,status);

		List<HistoryResponse> reservationResponses = reservationEntities.stream()
			.map(historyMapper::reservationToHistoryResponse)
			.toList();

		List<HistoryResponse> waitingResponses = waitingEntities.stream()
			.map(waiting -> historyMapper.entityToHistoryResponse(waiting, calculateWaitingRegistrationId(waiting, getStartOfDay(), getEndOfDay()), calculateMyWaitingPosition(waiting)))
			.toList();

		List<HistoryResponse> allHistoryResponses = new ArrayList<>();
		allHistoryResponses.addAll(reservationResponses);
		allHistoryResponses.addAll(waitingResponses);

		return allHistoryResponses;
	}

	// 예약 상세 정보 조회
	public HistoryResponse findReservationDetailById(List<HistoryResponse> historyList, Long reservationId) {
		return historyList.stream()
			.filter(h -> h.getReservationId() != null && reservationId.equals(h.getReservationId()))  // null 체크 추가
			.findFirst()
			.orElseThrow(HistoryException::new);
	}

	// 웨이팅 상세 정보 조회
	public HistoryResponse findWaitingDetailById(List<HistoryResponse> historyList, Long waitingId) {
		return historyList.stream()
			.filter(h -> h.getWaitingId() != null && waitingId.equals(h.getWaitingId()))  // null 체크 추가
			.findFirst()
			.orElseThrow(HistoryException::new);
	}

	private int calculateWaitingRegistrationId(WaitingEntity waiting, LocalDateTime startOfDay, LocalDateTime endOfDay) {
		long count = waitingRepository.countByRestaurantAndCreatedAtBetweenAndCreatedAtBefore(
				waiting.getRestaurant(), startOfDay, endOfDay, waiting.getCreatedAt());
		return (int)count + 1;
	}

	private int calculateMyWaitingPosition(WaitingEntity waiting) {
		long count = waitingRepository.countByRestaurantAndStatusAndCreatedAtBefore(
				waiting.getRestaurant(), Status.SCHEDULED, waiting.getCreatedAt());
		return (int)count + 1;
	}

	private void sortHistoryResponsesByCreatedAt(List<HistoryResponse> historyResponseList) {
		historyResponseList.sort(Comparator.comparing(HistoryResponse::getCreatedAt).reversed());
	}

	private LocalDateTime getStartOfDay() {
		return LocalDate.now().atStartOfDay();
	}

	private LocalDateTime getEndOfDay() {
		return LocalDate.now().atTime(LocalTime.MAX);
	}

	// 예약 리스트 가져오기
	private List<ReservationEntity> getReservationEntities(Long memberId, Status status) {
		return reservationRepository.findByMemberMemberIdAndStatus(memberId, status);
	}

	// 상태가 예정이고 날짜가 오늘인 웨이팅 리스트
	private List<WaitingEntity> getWaitingEntitiesScheduledForToday(Long memberId, Status status) {
		return waitingRepository.findByMemberMemberIdAndStatus(memberId, status);
	}

}


