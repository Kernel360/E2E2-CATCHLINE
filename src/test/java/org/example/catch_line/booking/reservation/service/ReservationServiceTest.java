package org.example.catch_line.booking.reservation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.example.catch_line.booking.reservation.model.dto.ReservationRequest;
import org.example.catch_line.booking.reservation.model.dto.ReservationResponse;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.model.mapper.ReservationResponseMapper;
import org.example.catch_line.booking.reservation.repository.ReservationRepository;
import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.exception.booking.ServiceTypeException;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.user.member.repository.MemberRepository;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

	@Mock
	private ReservationRepository reservationRepository;

	@Mock
	private ReservationResponseMapper reservationResponseMapper;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private RestaurantRepository restaurantRepository;

	@InjectMocks
	private ReservationService reservationService;

	private ReservationEntity reservationEntity;
	private RestaurantEntity restaurantEntity;
	private MemberEntity memberEntity;
	private LocalDateTime reservationDate;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@BeforeEach
	public void setUp() {
		reservationDate = LocalDateTime.of(2024, 8, 11, 12, 0);

		restaurantEntity = RestaurantEntity.builder()
			.name("새마을식당")
			.description("백종원의 새마을식당")
			.phoneNumber(new PhoneNumber("02-1234-1234"))
			.rating(new Rating(BigDecimal.ZERO))
			.serviceType(ServiceType.RESERVATION)
			.foodType(FoodType.KOREAN)
			.build();

		memberEntity = MemberEntity.builder()
			.email(new Email("abc@gmail.com"))
			.name("홍길동")
			.nickname("hong")
			.password(new Password(passwordEncoder.encode("123qwe!@Q")))
			.phoneNumber(new PhoneNumber("010-1234-1234"))
			.build();

		reservationEntity = ReservationEntity.builder()
			.memberCount(3)
			.status(Status.SCHEDULED)
			.reservationDate(reservationDate)
			.member(memberEntity)
			.restaurant(restaurantEntity)
			.build();
	}

	@Test
	@DisplayName("예약 등록 테스트")
	void add_reserve_test() {
		Long restaurantId = 1L;
		Long memberId = 1L;
		ReservationRequest reservationRequest = ReservationRequest.builder()
			.memberCount(3)
			.reservationDate(reservationDate)
			.build();

		when(memberRepository.findById(memberId)).thenReturn(Optional.of(memberEntity));
		when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));

		ReservationEntity savedEntity = ReservationEntity.builder()
			.memberCount(3)
			.status(Status.SCHEDULED)
			.reservationDate(reservationDate)
			.member(memberEntity)
			.restaurant(restaurantEntity)
			.build();

		when(reservationRepository.save(any(ReservationEntity.class))).thenReturn(savedEntity);

		ReservationResponse expectedResponse = ReservationResponse.builder()
			.reservationId(1L)
			.memberCount(3)
			.status(Status.SCHEDULED)
			.reservationDate(reservationDate)
			.build();
		when(reservationResponseMapper.convertToResponse(any(ReservationEntity.class))).thenReturn(expectedResponse);

		ReservationResponse reservationResponse = reservationService.addReserve(restaurantId, reservationRequest,
			memberId);

		assertNotNull(reservationResponse);
		assertEquals(expectedResponse.getReservationId(), reservationResponse.getReservationId());
		assertEquals(expectedResponse.getMemberCount(), reservationResponse.getMemberCount());
		assertEquals(expectedResponse.getStatus(), reservationResponse.getStatus());
		assertEquals(expectedResponse.getReservationDate(), reservationResponse.getReservationDate());
		verify(memberRepository, times(1)).findById(memberId);
		verify(restaurantRepository, times(1)).findById(restaurantId);
		verify(reservationRepository, times(1)).save(any(ReservationEntity.class));
		verify(reservationResponseMapper, times(1)).convertToResponse(any(ReservationEntity.class));
	}

	@Test
	@DisplayName("예약 등록 실패: 서비스 타입 예외")
	void add_reserve_failure_invalid_service_type() {
		Long restaurantId = 1L;
		Long memberId = 1L;
		ReservationRequest reservationRequest = ReservationRequest.builder()
			.memberCount(3)
			.reservationDate(reservationDate)
			.build();

		when(memberRepository.findById(memberId)).thenReturn(Optional.of(memberEntity));

		RestaurantEntity invalidRestaurant = RestaurantEntity.builder()
			.serviceType(ServiceType.WAITING) // Invalid service type
			.build();
		when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(invalidRestaurant));

		assertThrows(ServiceTypeException.class, () -> {
			reservationService.addReserve(restaurantId, reservationRequest, memberId);
		});
		verify(memberRepository, times(1)).findById(memberId);
		verify(restaurantRepository, times(1)).findById(restaurantId);
		verify(reservationRepository, never()).save(any(ReservationEntity.class));
		verify(reservationResponseMapper, never()).convertToResponse(any(ReservationEntity.class));
	}

	@Test
	@DisplayName("예약 취소 성공 테스트")
	void cancel_reservation_success() {
		Long id = 1L;
		ReservationEntity reservationEntity = ReservationEntity.builder()
			.status(Status.SCHEDULED)
			.build();

		when(reservationRepository.findById(id)).thenReturn(Optional.of(reservationEntity));

		reservationService.cancelReservation(id);

		assertEquals(Status.CANCELED, reservationEntity.getStatus());
		verify(reservationRepository, times(1)).save(reservationEntity);
	}

	@Test
	@DisplayName("예약 취소 실패 테스트: 엔티티를 찾지 못하는 경우")
	void cancel_reservation_failure() {
		Long id = 1L;

		when(reservationRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> {
			reservationService.cancelReservation(id);
		});
		verify(reservationRepository, never()).save(any(ReservationEntity.class));
	}
}
