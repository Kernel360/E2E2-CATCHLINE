package org.example.catch_line.booking.reservation.service;

import org.example.catch_line.booking.reservation.model.dto.ReservationRequest;
import org.example.catch_line.booking.reservation.model.dto.ReservationResponse;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.model.mapper.ReservationResponseMapper;
import org.example.catch_line.booking.reservation.repository.ReservationRepository;
import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.example.catch_line.exception.booking.DuplicateReservationTimeException;
import org.example.catch_line.history.validation.HistoryValidator;
import org.example.catch_line.notification.service.NotificationService;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.provider.validation.MemberValidator;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

	@Mock
	private NotificationService notificationService;

	@Mock
	private ReservationRepository reservationRepository;

	@Mock
	private ReservationResponseMapper reservationResponseMapper;

	@Mock
	private HistoryValidator historyValidator;

	@Mock
	private MemberValidator memberValidator;

	@Mock
	private RestaurantValidator restaurantValidator;

	@InjectMocks
	private ReservationService reservationService;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private MemberEntity memberEntity;
	private RestaurantEntity restaurantEntity;
	private ReservationEntity reservationEntity;
	private LocalDateTime reservationDate;

	@BeforeEach
	public void setUp() {
		reservationDate = LocalDateTime.of(2024, 8, 11, 12, 0);

		OwnerEntity owner = new OwnerEntity("qwer1111", "철수", new Password(passwordEncoder.encode("123qwe!@Q")), new PhoneNumber("010-1111-1111"));

		restaurantEntity = new RestaurantEntity("새마을식당", "백종원의 새마을식당", new Rating(BigDecimal.ZERO), new PhoneNumber("010-2111-1111"),
				FoodType.KOREAN, ServiceType.RESERVATION, owner, new BigDecimal("37.50828251273050000000"), new BigDecimal("127.06548046585200000000"));

		memberEntity = new MemberEntity(new Email("abc@gmail.com"), "홍길동", "hong", new Password(passwordEncoder.encode("123qwe!@Q")),
				new PhoneNumber("010-1234-1234"));
		reservationEntity = new ReservationEntity(3, Status.SCHEDULED, reservationDate, memberEntity, restaurantEntity);
	}

	@Test
	@DisplayName("예약 등록 성공 테스트")
	void addReservation_success() {
		ReservationRequest reservationRequest = new ReservationRequest(3, reservationDate, Status.SCHEDULED);

		when(reservationRepository.findByRestaurantRestaurantIdAndReservationDate(anyLong(), any(LocalDateTime.class)))
				.thenReturn(Optional.empty());
		when(memberValidator.checkIfMemberPresent(anyLong())).thenReturn(memberEntity);
		when(restaurantValidator.checkIfRestaurantPresent(anyLong())).thenReturn(restaurantEntity);
		when(reservationRepository.save(any(ReservationEntity.class))).thenReturn(reservationEntity);
		when(reservationResponseMapper.convertToResponse(any(ReservationEntity.class))).thenReturn(new ReservationResponse(1L, 3, reservationDate, Status.SCHEDULED, LocalDateTime.now(), LocalDateTime.now()));

		ReservationResponse response = reservationService.addReservation(1L, 1L, reservationRequest);

		assertNotNull(response);
		verify(notificationService, times(1)).sendReservation(any(MemberEntity.class), any(ReservationEntity.class), anyString());
		verify(reservationRepository, times(1)).save(any(ReservationEntity.class));
	}

	@Test
	@DisplayName("예약 시간 중복 예외 테스트")
	void addReservation_conflict() {
		ReservationRequest reservationRequest = new ReservationRequest(3, reservationDate, Status.SCHEDULED);

		when(reservationRepository.findByRestaurantRestaurantIdAndReservationDate(anyLong(), any(LocalDateTime.class)))
				.thenReturn(Optional.of(reservationEntity));

		assertThrows(DuplicateReservationTimeException.class, () -> {
			reservationService.addReservation(1L, 1L, reservationRequest);
		});

		verify(reservationRepository, never()).save(any(ReservationEntity.class));
		verify(notificationService, never()).sendReservation(any(MemberEntity.class), any(ReservationEntity.class), anyString());
	}

	@Test
	@DisplayName("예약 수정 성공 테스트")
	void updateReservation_success() {
		LocalDateTime newDate = reservationDate.plusDays(1);

		when(historyValidator.checkIfReservationPresent(anyLong())).thenReturn(reservationEntity);
		when(memberValidator.checkIfMemberPresent(anyLong())).thenReturn(memberEntity);
		when(reservationRepository.save(any(ReservationEntity.class))).thenReturn(reservationEntity);

		reservationService.updateReservation(1L, 1L, 4, newDate);

		verify(notificationService, times(1)).sendReservation(any(MemberEntity.class), any(ReservationEntity.class), anyString());
		verify(reservationRepository, times(1)).save(any(ReservationEntity.class));
	}

	@Test
	@DisplayName("예약 완료 테스트")
	void completedReservation_success() {
		when(historyValidator.checkIfReservationPresent(anyLong())).thenReturn(reservationEntity);

		reservationService.completedReservation(1L);

		assertEquals(Status.COMPLETED, reservationEntity.getStatus());
		verify(reservationRepository, times(1)).save(reservationEntity);
	}

	@Test
	@DisplayName("예약 취소 성공 테스트")
	void cancelReservation_success() {
		when(historyValidator.checkIfReservationPresent(anyLong())).thenReturn(reservationEntity);
		when(memberValidator.checkIfMemberPresent(anyLong())).thenReturn(memberEntity);

		reservationService.cancelReservation(1L, 1L);

		assertEquals(Status.CANCELED, reservationEntity.getStatus());
		verify(reservationRepository, times(1)).save(reservationEntity);
		verify(notificationService, times(1)).sendReservation(any(MemberEntity.class), any(ReservationEntity.class), anyString());
	}
}
