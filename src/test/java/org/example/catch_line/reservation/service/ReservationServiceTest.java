package org.example.catch_line.reservation.service;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.example.catch_line.common.constant.Role;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.member.model.dto.SignUpRequest;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.repository.MemberRepository;
import org.example.catch_line.member.service.MemberService;
import org.example.catch_line.reservation.model.dto.ReservationRequest;
import org.example.catch_line.reservation.model.dto.ReservationResponse;
import org.example.catch_line.reservation.model.entity.ReservationEntity;
import org.example.catch_line.reservation.repository.ReservationRepository;
import org.example.catch_line.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.example.catch_line.restaurant.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReservationServiceTest {

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private MemberService memberService;

	@Autowired
	private RestaurantService restaurantService;

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
	@DisplayName("예약 추가 테스트")
	void testAddReserve() {

		ReservationRequest request = ReservationRequest.builder()
			.memberCount(4)
			.reservationDate(LocalDateTime.now().plusDays(1))
			.status(Status.SCHEDULED)
			.build();

		ReservationResponse response = reservationService.addReserve(memberId, restaurantId, request);

		assertThat(response).isNotNull();
		assertThat(response.getMemberCount()).isEqualTo(4);
		assertThat(response.getStatus()).isEqualTo(Status.SCHEDULED);
		assertThat(response.getReservationDate()).isEqualTo(request.getReservationDate());
	}

	@Test
	@DisplayName("모든 예약 조회 테스트")
	void testGetAllReservation() {
		ReservationRequest request1 = ReservationRequest.builder()
			.memberCount(2)
			.reservationDate(LocalDateTime.now().plusDays(1))
			.status(Status.SCHEDULED)
			.build();
		ReservationRequest request2 = ReservationRequest.builder()
			.memberCount(5)
			.reservationDate(LocalDateTime.now().plusDays(3))
			.status(Status.SCHEDULED)
			.build();
		ReservationRequest request3 = ReservationRequest.builder()
			.memberCount(7)
			.reservationDate(LocalDateTime.now().plusDays(7))
			.status(Status.SCHEDULED)
			.build();

		reservationService.addReserve(memberId, restaurantId, request1);

		reservationService.addReserve(memberId, restaurantId, request2);

		reservationService.addReserve(memberId2, restaurantId, request3);

		List<ReservationResponse> responses = reservationService.getAllReservation(memberId);

		assertThat(responses).hasSize(2);
	}

	@Test
	@DisplayName("예약 개별 조회 테스트")
	void testGetReservationById() {
		ReservationRequest request = ReservationRequest.builder()
			.memberCount(4)
			.reservationDate(LocalDateTime.now().plusDays(2))
			.status(Status.SCHEDULED)
			.build();

		ReservationResponse addedResponse = reservationService.addReserve(memberId, restaurantId, request);
		Long id = addedResponse.getReservationId();

		ReservationResponse response = reservationService.getReservationById(id);

		assertThat(response).isNotNull();
		assertThat(response.getReservationId()).isEqualTo(id);
		assertThat(response.getMemberCount()).isEqualTo(request.getMemberCount());
		assertThat(response.getReservationDate()).isEqualTo(request.getReservationDate());
	}

	@Test
	@DisplayName("예약 취소 테스트")
	void testCancelReservation() {
		ReservationRequest request = ReservationRequest.builder()
			.memberCount(4)
			.reservationDate(LocalDateTime.now().plusDays(2))
			.status(Status.SCHEDULED)
			.build();

		ReservationResponse response = reservationService.addReserve(memberId, restaurantId, request);
		Long id = response.getReservationId();

		reservationService.cancelReservation(id);

		ReservationEntity cancelledEntity = reservationRepository.findById(id).orElseThrow();
		assertThat(cancelledEntity.getStatus()).isEqualTo(Status.CANCELED);
	}

	@Test
	@DisplayName("예약 수정 테스트")
	void testEditReservation() {
		ReservationRequest request = ReservationRequest.builder()
			.memberCount(4)
			.reservationDate(LocalDateTime.now().plusDays(2))
			.status(Status.SCHEDULED)
			.build();

		ReservationResponse response = reservationService.addReserve(memberId, restaurantId, request);
		Long id = response.getReservationId();

		ReservationRequest updatedRequest = ReservationRequest.builder()
			.memberCount(5)
			.reservationDate(LocalDateTime.now().plusDays(3))
			.status(Status.SCHEDULED)
			.build();

		reservationService.editReservation(id, updatedRequest);

		ReservationEntity updatedEntity = reservationRepository.findById(id).orElseThrow();
		assertThat(updatedEntity.getMemberCount()).isEqualTo(5);
		assertThat(updatedEntity.getReservationDate()).isEqualTo(updatedRequest.getReservationDate());
		assertThat(updatedEntity.getStatus()).isEqualTo(updatedRequest.getStatus());
	}
}
