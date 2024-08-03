package org.example.catch_line.waiting.service;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.example.catch_line.common.constant.Role;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.member.model.dto.SignUpRequest;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.repository.MemberRepository;
import org.example.catch_line.member.service.MemberService;
import org.example.catch_line.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.entity.FoodType;
import org.example.catch_line.restaurant.model.entity.ServiceType;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.example.catch_line.restaurant.service.RestaurantService;
import org.example.catch_line.waiting.model.dto.WaitingRequest;
import org.example.catch_line.waiting.model.dto.WaitingResponse;
import org.example.catch_line.waiting.model.entity.WaitingEntity;
import org.example.catch_line.waiting.model.entity.WaitingType;
import org.example.catch_line.waiting.repository.WaitingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@Transactional
class WaitingServiceTest {

	@Autowired
	WaitingService waitingService;
	@Autowired
	WaitingRepository waitingRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	RestaurantRepository restaurantRepository;

	@Autowired
	MemberService memberService;

	@Autowired
	RestaurantService restaurantService;

	private Long memberId;
	private Long memberId2;
	private Long restaurantId;

	@BeforeEach
	void setUp() {
		SignUpRequest request = SignUpRequest.builder()
			.email("test@example.com")
			.name("Test User")
			.nickname("testnickname")
			.phoneNumber("01012345678")
			.password("password")
			.role(Role.USER)
			.build();

		memberService.signUp(request);

		MemberEntity member = memberRepository.findByEmail("test@example.com").orElse(null);

		memberId = member.getMemberId();

		SignUpRequest request2 = SignUpRequest.builder()
			.email("test1@example.com")
			.name("Test User")
			.nickname("testnickname")
			.phoneNumber("01012345678")
			.password("password")
			.role(Role.USER)
			.build();

		memberService.signUp(request2);

		MemberEntity member2 = memberRepository.findByEmail("test1@example.com").orElse(null);

		memberId2 = member2.getMemberId();

		RestaurantCreateRequest request1 = RestaurantCreateRequest.builder()
			.description("식당 소개")
			.name("한식집")
			.latitude(BigDecimal.ZERO)
			.longitude(BigDecimal.ZERO)
			.phoneNumber("02-1234-1234")
			.foodType(FoodType.KOREAN)
			.serviceType(ServiceType.WAITING)
			.build();

		RestaurantResponse restaurant = restaurantService.createRestaurant(request1);

		restaurantId = restaurant.getRestaurantId();

	}

	@Test
	@DisplayName("웨이팅 추가 테스트")
	void testAddWaiting() {

		WaitingRequest request = WaitingRequest.builder()
			.waitingType(WaitingType.DINE_IN)
			.memberCount(4)
			.build();

		WaitingResponse response = waitingService.addWaiting(memberId, restaurantId, request);

		WaitingEntity entity = waitingRepository.findById(response.getWaitingId()).orElseThrow();

		assertThat(response.getWaitingId()).isEqualTo(entity.getWaitingId());
		assertThat(response.getStatus()).isEqualTo(entity.getStatus());
		assertThat(response.getWaitingType()).isEqualTo(entity.getWaitingType());
		assertThat(response.getMemberCount()).isEqualTo(entity.getMemberCount());

	}

	@Test
	@DisplayName("웨이팅 전체 조회 테스트")
	void testGetAllWaiting() {
		WaitingRequest request1 = WaitingRequest.builder()
			.waitingType(WaitingType.DINE_IN)
			.memberCount(2)
			.build();
		WaitingRequest request2 = WaitingRequest.builder()
			.waitingType(WaitingType.TAKE_OUT)
			.memberCount(3)
			.build();
		WaitingRequest request3 = WaitingRequest.builder()
			.waitingType(WaitingType.TAKE_OUT)
			.memberCount(5)
			.build();

		waitingService.addWaiting(memberId, restaurantId, request1);
		waitingService.addWaiting(memberId, restaurantId, request2);
		waitingService.addWaiting(memberId2, restaurantId, request3);

		List<WaitingResponse> responses = waitingService.getAllWaiting(memberId);

		assertThat(responses).hasSize(2);
		WaitingResponse response1 = responses.get(0);
		WaitingResponse response2 = responses.get(1);

		assertThat(response1.getMemberCount()).isIn(2, 3);
		assertThat(response1.getWaitingType()).isIn(WaitingType.DINE_IN, WaitingType.TAKE_OUT);
		assertThat(response1.getStatus()).isEqualTo(Status.SCHEDULED);

		assertThat(response2.getMemberCount()).isIn(2, 3);
		assertThat(response2.getWaitingType()).isIn(WaitingType.DINE_IN, WaitingType.TAKE_OUT);
		assertThat(response2.getStatus()).isEqualTo(Status.SCHEDULED);

	}

	@Test
	@DisplayName("웨이팅 개별 조회 테스트")
	void testGetWaitingById() {
		WaitingRequest request = WaitingRequest.builder()
			.waitingType(WaitingType.DINE_IN)
			.memberCount(4)
			.build();

		WaitingResponse addedResponse = waitingService.addWaiting(memberId, restaurantId, request);
		Long id = addedResponse.getWaitingId();

		WaitingResponse response = waitingService.getWaitingById(id);

		assertThat(response.getWaitingId()).isEqualTo(id);
		assertThat(response.getMemberCount()).isEqualTo(request.getMemberCount());
		assertThat(response.getWaitingType()).isEqualTo(request.getWaitingType());
		assertThat(response.getStatus()).isEqualTo(Status.SCHEDULED);
	}

	@Test
	@DisplayName("웨이팅 취소 테스트")
	void testCancelWaiting() {
		WaitingRequest request = WaitingRequest.builder()
			.waitingType(WaitingType.DINE_IN)
			.memberCount(4)
			.build();

		WaitingResponse response = waitingService.addWaiting(memberId, restaurantId, request);
		Long id = response.getWaitingId();

		waitingService.cancelWaiting(id);

		WaitingEntity cancelledEntity = waitingRepository.findById(id).orElseThrow();

		Assertions.assertThat(cancelledEntity.getStatus()).isEqualTo(Status.CANCELED);
	}

}