package org.example.catch_line.waiting.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.catch_line.common.constant.SessionConst;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.repository.MemberRepository;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.example.catch_line.waiting.model.dto.WaitingRequest;
import org.example.catch_line.waiting.model.dto.WaitingResponse;
import org.example.catch_line.waiting.model.entity.WaitingEntity;
import org.example.catch_line.waiting.model.mapper.WaitingResponseMapper;
import org.example.catch_line.waiting.repository.WaitingRepository;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
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

	public WaitingResponse addWaiting(Long restaurantId, WaitingRequest waitingRequest, HttpSession session) {

		Long memberId = (Long)session.getAttribute(SessionConst.MEMBER_ID);

		MemberEntity member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("member 아이디가 틀립니다: " + memberId));
		RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("식당 아이디가 틀립니다: " + restaurantId));

		// 서비스 타입이 WAITING이 아니면 예외를 발생시킴
		if (restaurant.getServiceType() != ServiceType.WAITING) {
			throw new IllegalArgumentException("해당 식당은 WAITING 타입이 아닙니다: " + restaurantId);
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

	public List<WaitingResponse> getAllWaiting(Long memberId) {
		List<WaitingEntity> waitingEntities = waitingRepository.findByMemberMemberId(memberId);

		return waitingEntities.stream()
			.map(waitingResponseMapper::convertToResponse)
			.collect(Collectors.toList());
	}

	public WaitingResponse getWaitingById(Long id) {
		WaitingEntity waitingEntity = waitingRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("정확한 아이디가 아닙니다: " + id));
		return waitingResponseMapper.convertToResponse(waitingEntity);
	}

	public void cancelWaiting(Long id) {
		WaitingEntity entity = waitingRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("웨이팅 아이디가 다릅니다: " + id));
		entity.changeWaitingStatus(Status.CANCELED);
		waitingRepository.save(entity);
	}

}
