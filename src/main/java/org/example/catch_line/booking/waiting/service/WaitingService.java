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

	// 다른 사이트의 코드들을 참고해보면 서비스 메소드들에 트랜잭션을 붙이지 않은 메소드들도 많던데 트랜잭션을 붙여야 하는 경우와 붙히지 않는 경우의 차이가 궁금합니다
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public WaitingResponse addWaiting(Long restaurantId, WaitingRequest waitingRequest, Long memberId) {
		MemberEntity member = memberValidator.checkIfMemberPresent(memberId);
		RestaurantEntity restaurant = restaurantValidator.checkIfRestaurantPresent(restaurantId);

		WaitingEntity waiting = requestToEntity(waitingRequest, member, restaurant);
		WaitingEntity savedEntity = waitingRepository.save(waiting);

		notificationService.sendWaiting(member, waiting, "웨이팅에 성공하였습니다.");
		return waitingResponseMapper.convertToResponse(savedEntity);
	}

	@Transactional
	public void cancelWaiting(Long memberId, Long waitingId) {
		MemberEntity member = memberValidator.checkIfMemberPresent(memberId);
		WaitingEntity waiting = historyValidator.checkIfWaitingPresent(waitingId);

		waiting.canceled();
		notificationService.sendWaiting(member, waiting, "웨이팅이 취소되었습니다.");
	}

	@Transactional
	public void completedWaiting(Long waitingId) {
		WaitingEntity entity = historyValidator.checkIfWaitingPresent(waitingId);
		entity.completed();
	}


	public boolean isExistingWaiting(Long memberId, Status status) {
		return waitingRepository.existsByMemberMemberIdAndStatus(memberId, status);
	}

	private WaitingEntity requestToEntity(WaitingRequest waitingRequest, MemberEntity member, RestaurantEntity restaurant) {
        return new WaitingEntity(waitingRequest.getMemberCount(), Status.SCHEDULED, waitingRequest.getWaitingType(), member, restaurant);
	}

}
