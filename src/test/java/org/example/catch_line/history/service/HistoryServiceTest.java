package org.example.catch_line.history.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.repository.ReservationRepository;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.model.entity.WaitingType;
import org.example.catch_line.booking.waiting.repository.WaitingRepository;
import org.example.catch_line.common.constant.Role;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.exception.booking.HistoryException;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.model.vo.Email;
import org.example.catch_line.member.model.vo.Password;
import org.example.catch_line.member.model.vo.PhoneNumber;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class HistoryServiceTest {

	@Mock
	private WaitingRepository waitingRepository;

	@Mock
	private ReservationRepository reservationRepository;

	@InjectMocks
	private HistoryService historyService;

	private WaitingEntity waitingEntity;

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
			.serviceType(ServiceType.WAITING)
			.foodType(FoodType.KOREAN)
			.build();

		memberEntity = MemberEntity.builder()
			.email(new Email("abc@gmail.com"))
			.name("홍길동")
			.nickname("hong")
			.password(new Password(passwordEncoder.encode("123qwe!@Q")))
			.phoneNumber(new PhoneNumber("010-1234-1234"))
			.role(Role.USER)
			.build();

		waitingEntity = WaitingEntity.builder()
			.memberCount(3)
			.status(Status.SCHEDULED)
			.waitingType(WaitingType.TAKE_OUT)
			.member(memberEntity)
			.restaurant(restaurantEntity)
			.build();

		reservationEntity = ReservationEntity.builder()
			.memberCount(3)
			.status(Status.SCHEDULED)
			.reservationDate(reservationDate)
			.member(memberEntity)
			.restaurant(restaurantEntity)
			.build();

		// RuntimeException으로 예외를 래핑하여 던짐
		setCreatedAt(waitingEntity, LocalDateTime.now().minusDays(1));
		setCreatedAt(reservationEntity, LocalDateTime.now());
	}

	private void setCreatedAt(Object entity, LocalDateTime createdAt) {
		try {
			// 상속된 필드를 포함하여 "createdAt" 필드를 찾음
			Field createdAtField = findField(entity.getClass(), "createdAt");
			if (createdAtField != null) {
				createdAtField.setAccessible(true);
				createdAtField.set(entity, createdAt);
			} else {
				throw new RuntimeException(
					"Field 'createdAt' not found in class hierarchy: " + entity.getClass().getName());
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to access 'createdAt' field in class: " + entity.getClass().getName(),
				e);
		}
	}

	private Field findField(Class<?> clazz, String fieldName) {
		while (clazz != null) {
			try {
				return clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass(); // 부모 클래스를 검색
			}
		}
		return null;
	}

	@Test
	@DisplayName("getAllHistory returns a list of history responses")
	void getAllHistory_returnsHistoryResponses() {
		// Given
		when(waitingRepository.findByMemberMemberIdAndStatusAndCreatedAtBetween(anyLong(), any(Status.class),
			any(LocalDateTime.class), any(LocalDateTime.class)))
			.thenReturn(Collections.singletonList(waitingEntity));

		when(reservationRepository.findByMemberMemberIdAndStatus(anyLong(), any(Status.class)))
			.thenReturn(Collections.singletonList(reservationEntity));

		// When
		List<HistoryResponse> historyResponses = historyService.getAllHistory(1L, Status.SCHEDULED);

		// Then
		assertNotNull(historyResponses); // 반환된 리스트가 null이 아님을 확인합니다.
		assertEquals(2, historyResponses.size()); // 반환된 리스트의 크기가 2인지 확인합니다.

		// 리스트의 첫 번째 객체의 레스토랑 이름이 예상대로인지 확인합니다.
		assertEquals("새마을식당", historyResponses.get(0).getRestaurantName());

		// waitingRepository와 reservationRepository가 각각 1회 호출되었는지 확인합니다.
		verify(waitingRepository, times(1)).findByMemberMemberIdAndStatusAndCreatedAtBetween(anyLong(),
			any(Status.class), any(LocalDateTime.class), any(LocalDateTime.class));
		verify(reservationRepository, times(1)).findByMemberMemberIdAndStatus(anyLong(), any(Status.class));
	}

	@Test
	@DisplayName("findReservationDetailById returns the correct reservation")
	void findReservationDetailById_returnsReservationDetail() {
		// Given
		HistoryResponse reservationHistoryResponse = HistoryResponse.builder()
			.reservationId(1L)
			.restaurantName("새마을식당")
			.build();

		List<HistoryResponse> historyResponses = Collections.singletonList(reservationHistoryResponse);

		// When
		HistoryResponse foundReservation = historyService.findReservationDetailById(historyResponses, 1L);

		// Then
		assertNotNull(foundReservation);
		assertEquals(1L, foundReservation.getReservationId());
	}

	@Test
	@DisplayName("findReservationDetailById throws exception if reservation not found")
	void findReservationDetailById_throwsException() {
		// Given
		List<HistoryResponse> historyResponses = Collections.emptyList();

		// When / Then
		assertThrows(HistoryException.class, () -> {
			historyService.findReservationDetailById(historyResponses, 1L);
		});
	}

	@Test
	@DisplayName("findWaitingDetailById returns the correct waiting")
	void findWaitingDetailById_returnsWaitingDetail() {
		// Given
		HistoryResponse waitingHistoryResponse = HistoryResponse.builder()
			.waitingId(1L)
			.restaurantName("새마을식당")
			.build();

		List<HistoryResponse> historyResponses = Collections.singletonList(waitingHistoryResponse);

		// When
		HistoryResponse foundWaiting = historyService.findWaitingDetailById(historyResponses, 1L);

		// Then
		assertNotNull(foundWaiting);
		assertEquals(1L, foundWaiting.getWaitingId());
	}

	@Test
	@DisplayName("findWaitingDetailById throws exception if waiting not found")
	void findWaitingDetailById_throwsException() {
		// Given
		List<HistoryResponse> historyResponses = Collections.emptyList();

		// When / Then
		assertThrows(HistoryException.class, () -> {
			historyService.findWaitingDetailById(historyResponses, 1L);
		});
	}
}


