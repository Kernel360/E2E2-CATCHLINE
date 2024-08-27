package org.example.catch_line.booking.reservation.service;

import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.booking.reservation.model.dto.ReservationRequest;
import org.example.catch_line.booking.reservation.model.dto.ReservationResponse;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.model.mapper.ReservationResponseMapper;
import org.example.catch_line.booking.reservation.repository.ReservationRepository;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.exception.booking.DuplicateReservationTimeException;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.history.model.mapper.HistoryMapper;
import org.example.catch_line.history.validation.HistoryValidator;
import org.example.catch_line.notification.service.NotificationService;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.provider.validation.MemberValidator;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

	private final NotificationService notificationService;
	private final ReservationRepository reservationRepository;
	private final ReservationResponseMapper reservationResponseMapper;
	private final HistoryValidator historyValidator;
	private final MemberValidator memberValidator;
	private final RestaurantValidator restaurantValidator;
	private final HistoryMapper historyMapper;

	public ReservationResponse addReservation(Long memberId, Long restaurantId, ReservationRequest reservationRequest) {
		if(isReservationTimeConflict(restaurantId, reservationRequest.getReservationDate())) {
			throw new DuplicateReservationTimeException();
		}

		MemberEntity member = memberValidator.checkIfMemberPresent(memberId);
		RestaurantEntity restaurant = restaurantValidator.checkIfRestaurantPresent(restaurantId);

		ReservationEntity reservation = new ReservationEntity(reservationRequest.getMemberCount(),Status.SCHEDULED,reservationRequest.getReservationDate(),member,restaurant);

		ReservationEntity savedEntity = reservationRepository.save(reservation);

		notificationService.sendReservation(member, reservation, "예약에 성공하였습니다.");
		return reservationResponseMapper.convertToResponse(savedEntity);
	}

	public HistoryResponse updateReservation(Long memberId, Long reservationId, int memberCount, LocalDateTime reservationDate) {
		Long restaurantId = reservationRepository.findByReservationId(reservationId)
				.get()
				.getRestaurant()
				.getRestaurantId();
		MemberEntity member = memberValidator.checkIfMemberPresent(memberId);

		if(isReservationTimeConflict(restaurantId, reservationDate)) {
			throw new DuplicateReservationTimeException();
		}

		ReservationEntity reservationEntity = historyValidator.checkIfReservationPresent(reservationId);
		reservationEntity.updateReservation(memberCount, reservationDate);
		ReservationEntity savedEntity = reservationRepository.save(reservationEntity);

		notificationService.sendReservation(member, savedEntity, "예약이 수정되었습니다!");
		return historyMapper.reservationToHistoryResponse(savedEntity);
	}

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

	public void completedReservation(Long reservationId) {
		ReservationEntity reservationEntity = historyValidator.checkIfReservationPresent(reservationId);
		reservationEntity.changeReservationStatus(Status.COMPLETED);
		reservationRepository.save(reservationEntity);
	}

	public void cancelReservation(Long memberId, Long reservationId) {
		ReservationEntity reservationEntity = historyValidator.checkIfReservationPresent(reservationId);
		reservationEntity.changeReservationStatus(Status.CANCELED);
		MemberEntity member = memberValidator.checkIfMemberPresent(memberId);

		reservationRepository.save(reservationEntity);
		notificationService.sendReservation(member, reservationEntity, "예약이 취소되었습니다!");
	}

	public ReservationEntity findReservationById(Long reservationId) {
        return historyValidator.checkIfReservationPresent(reservationId);
	}

	private boolean isReservationTimeConflict(Long restaurantId, LocalDateTime reservationDate) {
		Optional<ReservationEntity> reservationEntityOptional = reservationRepository.findByRestaurantRestaurantIdAndReservationDate(restaurantId, reservationDate);
		return reservationEntityOptional.isPresent();
	}

}
