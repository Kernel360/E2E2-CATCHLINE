package org.example.catch_line.booking.waiting.service;

import org.example.catch_line.booking.waiting.model.dto.WaitingRequest;
import org.example.catch_line.booking.waiting.model.dto.WaitingResponse;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.model.mapper.WaitingResponseMapper;
import org.example.catch_line.booking.waiting.repository.WaitingRepository;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.exception.booking.ServiceTypeException;
import org.example.catch_line.exception.booking.WaitingException;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.repository.MemberRepository;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingService {

	private final WaitingRepository waitingRepository;
	private final WaitingResponseMapper waitingResponseMapper;
	private final MemberRepository memberRepository;
	private final RestaurantRepository restaurantRepository;

	public WaitingResponse addWaiting(Long restaurantId, WaitingRequest waitingRequest, Long memberId) {

		MemberEntity member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("member 아이디가 틀립니다: " + memberId));
		RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("식당 아이디가 틀립니다: " + restaurantId));

		// 서비스 타입이 WAITING이 아니면 예외를 발생시킴
		if (restaurant.getServiceType() != ServiceType.WAITING) {
			throw new ServiceTypeException();
		}

		WaitingEntity waiting = WaitingEntity.builder()
			.memberCount(waitingRequest.getMemberCount())
			.status(Status.SCHEDULED)
			.waitingType(waitingRequest.getWaitingType())
			.member(member)
			.restaurant(restaurant)
			.build();
		WaitingEntity savedEntity = waitingRepository.save(waiting);

		return waitingResponseMapper.convertToResponse(savedEntity);
	}

	public void cancelWaiting(Long id) {
		WaitingEntity entity = waitingRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("웨이팅 아이디가 다릅니다: " + id));
		entity.changeWaitingStatus(Status.CANCELED);
		waitingRepository.save(entity);
	}

	public boolean isExistingWaiting(Long memberId,Status status) {
		return waitingRepository.existsByMemberMemberIdAndStatus(memberId, status);
	}

}
