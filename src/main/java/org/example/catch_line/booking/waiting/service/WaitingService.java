package org.example.catch_line.booking.waiting.service;

import org.example.catch_line.booking.waiting.model.dto.WaitingRequest;
import org.example.catch_line.booking.waiting.model.dto.WaitingResponse;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.model.mapper.WaitingResponseMapper;
import org.example.catch_line.booking.waiting.repository.WaitingRepository;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.exception.booking.WaitingException;
import org.example.catch_line.history.validation.HistoryValidator;
import org.example.catch_line.notification.service.NotificationService;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.provider.validation.MemberValidator;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingService {

	private final NotificationService notificationService;
	private final WaitingRepository waitingRepository;
	private final WaitingResponseMapper waitingResponseMapper;
	private final HistoryValidator historyValidator;
	private final MemberValidator memberValidator;
	private final RestaurantValidator restaurantValidator;

	public WaitingEntity getWaitingEntity(Long waitingId) {
		return waitingRepository.findById(waitingId)
				.orElseThrow(() -> new WaitingException("웨이팅이 존재하지 않습니다."));
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public WaitingResponse addWaiting(Long restaurantId, WaitingRequest waitingRequest, Long memberId) {
		MemberEntity member = memberValidator.checkIfMemberPresent(memberId);
		RestaurantEntity restaurant = restaurantValidator.checkIfRestaurantPresent(restaurantId);

		WaitingEntity waiting = requestToEntity(waitingRequest, member, restaurant);
		WaitingEntity savedEntity = waitingRepository.save(waiting);

		notificationService.sendWaiting(member, waiting, "웨이팅에 성공하였습니다.");
		return waitingResponseMapper.convertToResponse(savedEntity);
	}

	public void cancelWaiting(Long memberId, Long waitingId) {
		MemberEntity member = memberValidator.checkIfMemberPresent(memberId);
		WaitingEntity waiting = historyValidator.checkIfWaitingPresent(waitingId);

		waiting.changeWaitingStatus(Status.CANCELED);
		waitingRepository.save(waiting);
		notificationService.sendWaiting(member, waiting, "웨이팅이 취소되었습니다.");
	}

	public void completedWaiting(Long waitingId) {
		WaitingEntity entity = historyValidator.checkIfWaitingPresent(waitingId);

		entity.changeWaitingStatus(Status.COMPLETED);
		waitingRepository.save(entity);
	}

	@Scheduled(cron = "0 0 0 * * ?")
	public void updateScheduledWaiting() {
		List<WaitingEntity> waitingEntities = waitingRepository.findAllByStatus(Status.SCHEDULED);

		for (WaitingEntity waitingEntity : waitingEntities) {
			waitingEntity.changeWaitingStatus(Status.CANCELED);
		}

		waitingRepository.saveAll(waitingEntities);
	}

	public boolean isExistingWaiting(Long memberId, Status status) {
		return waitingRepository.existsByMemberMemberIdAndStatus(memberId, status);
	}

	private WaitingEntity requestToEntity(WaitingRequest waitingRequest, MemberEntity member, RestaurantEntity restaurant) {
		return WaitingEntity.builder()
				.memberCount(waitingRequest.getMemberCount())
				.status(Status.SCHEDULED)
				.waitingType(waitingRequest.getWaitingType())
				.member(member)
				.restaurant(restaurant)
				.build();
	}

}
