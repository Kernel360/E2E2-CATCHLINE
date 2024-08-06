package org.example.catch_line.reservation.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.example.catch_line.common.constant.Role;
import org.example.catch_line.common.constant.SessionConst;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.member.model.dto.LoginRequest;
import org.example.catch_line.member.model.dto.SignUpRequest;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.repository.MemberRepository;
import org.example.catch_line.member.service.AuthService;
import org.example.catch_line.reservation.model.dto.ReservationRequest;
import org.example.catch_line.reservation.model.dto.ReservationResponse;
import org.example.catch_line.reservation.model.entity.ReservationEntity;
import org.example.catch_line.reservation.repository.ReservationRepository;
import org.example.catch_line.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.restaurant.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;

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
	private RestaurantService restaurantService;

	@Autowired
	private AuthService authService;

	private HttpSession session;

	private Long memberId;
	private Long restaurantId;

	private Long restaurantId2;

	@BeforeEach
	void setUp() {

		session = mock(HttpSession.class);

		SignUpRequest request = SignUpRequest.builder()
			.email("test@example.com")
			.name("Test User")
			.nickname("testnickname")
			.phoneNumber("01012345678")
			.password("password")
			.role(Role.USER)
			.build();

		authService.signUp(request);

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

		authService.signUp(request2);



		RestaurantCreateRequest request1 = RestaurantCreateRequest.builder()
			.description("식당 소개")
			.name("한식집")
			.latitude(BigDecimal.ZERO)
			.longitude(BigDecimal.ZERO)
			.phoneNumber("02-1234-1234")
			.foodType(FoodType.KOREAN)
			.serviceType(ServiceType.WAITING)
			.build();

		RestaurantCreateRequest restaurant2 = RestaurantCreateRequest.builder()
			.description("식당 소개")
			.name("중식집")
			.latitude(BigDecimal.ZERO)
			.longitude(BigDecimal.ZERO)
			.phoneNumber("02-1234-1234")
			.foodType(FoodType.KOREAN)
			.serviceType(ServiceType.RESERVATION)
			.build();

		RestaurantResponse restaurant = restaurantService.createRestaurant(request1);
		RestaurantResponse restaurant3 = restaurantService.createRestaurant(restaurant2);

		restaurantId = restaurant.getRestaurantId();
		restaurantId2 = restaurant3.getRestaurantId();

		LoginRequest loginRequest = LoginRequest.builder()
			.email("test1@example.com")
			.password("password")
			.role(Role.USER)
			.build();

		authService.login(loginRequest);

		when(session.getAttribute(SessionConst.MEMBER_ID)).thenReturn(memberId);
	}

	@Test
	@DisplayName("예약 추가 테스트")
	void testAddReserve() {

		ReservationRequest request = ReservationRequest.builder()
			.memberCount(4)
			.reservationDate(LocalDateTime.now().plusDays(1))
			.status(Status.SCHEDULED)
			.build();

		ReservationResponse response = reservationService.addReserve(restaurantId2, request, session);

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

		reservationService.addReserve(restaurantId2, request1, session);

		reservationService.addReserve(restaurantId2, request2, session);

		reservationService.addReserve(restaurantId2, request3, session);

		List<ReservationResponse> responses = reservationService.getAllReservation(memberId);

		assertThat(responses).hasSize(3);
	}

	@Test
	@DisplayName("예약 개별 조회 테스트")
	void testGetReservationById() {
		ReservationRequest request = ReservationRequest.builder()
			.memberCount(4)
			.reservationDate(LocalDateTime.now().plusDays(2))
			.status(Status.SCHEDULED)
			.build();

		ReservationResponse addedResponse = reservationService.addReserve(restaurantId2, request, session);
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

		ReservationResponse response = reservationService.addReserve(restaurantId2, request, session);
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

		ReservationResponse response = reservationService.addReserve(restaurantId2, request, session);
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
