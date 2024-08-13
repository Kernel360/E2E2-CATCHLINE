package org.example.catch_line.booking.waiting.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.example.catch_line.booking.waiting.model.dto.WaitingRequest;
import org.example.catch_line.booking.waiting.model.dto.WaitingResponse;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.model.entity.WaitingType;
import org.example.catch_line.booking.waiting.model.mapper.WaitingResponseMapper;
import org.example.catch_line.booking.waiting.repository.WaitingRepository;
import org.example.catch_line.common.constant.Role;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.exception.booking.ServiceTypeException;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.vo.Email;
import org.example.catch_line.user.member.model.vo.Password;
import org.example.catch_line.user.member.model.vo.PhoneNumber;
import org.example.catch_line.user.member.repository.MemberRepository;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
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
public class WaitingServiceTest {

	@Mock
	private WaitingRepository waitingRepository;

	@Mock
	private WaitingResponseMapper waitingResponseMapper;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private RestaurantRepository restaurantRepository;

	@InjectMocks
	private WaitingService waitingService;

	private WaitingEntity waitingEntity;

	private RestaurantEntity restaurantEntity;

	private MemberEntity memberEntity;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@BeforeEach
	public void setUp() {
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

	}

	@Test
	@DisplayName("웨이팅 등록 테스트")
	void add_waiting_test() {

		//given
		Long restaurantId = 1L;
		Long memberId = 1L;
		WaitingRequest waitingRequest = WaitingRequest.builder()
			.memberCount(3)
			.waitingType(WaitingType.TAKE_OUT)
			.build();

		when(memberRepository.findById(memberId)).thenReturn(Optional.of(memberEntity));
		when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));

		WaitingEntity savedEntity = WaitingEntity.builder()
			.memberCount(3)
			.status(Status.SCHEDULED)
			.waitingType(WaitingType.TAKE_OUT)
			.member(memberEntity)
			.restaurant(restaurantEntity)
			.build();

		when(waitingRepository.save(any(WaitingEntity.class))).thenReturn(savedEntity);

		WaitingResponse expectedResponse = WaitingResponse.builder()
			.waitingId(1L)
			.memberCount(3)
			.status(Status.SCHEDULED)
			.waitingType(WaitingType.TAKE_OUT)
			.build();
		when(waitingResponseMapper.convertToResponse(any(WaitingEntity.class))).thenReturn(expectedResponse);

		//when
		WaitingResponse waitingResponse = waitingService.addWaiting(restaurantId, waitingRequest, memberId);

		//then
		assertNotNull(waitingResponse);
		assertEquals(expectedResponse.getWaitingId(), waitingResponse.getWaitingId());
		assertEquals(expectedResponse.getMemberCount(), waitingResponse.getMemberCount());
		assertEquals(expectedResponse.getStatus(), waitingResponse.getStatus());
		assertEquals(expectedResponse.getWaitingType(), waitingResponse.getWaitingType());
		verify(memberRepository, times(1)).findById(memberId);
		verify(restaurantRepository, times(1)).findById(restaurantId);
		verify(waitingRepository, times(1)).save(any(WaitingEntity.class));
		verify(waitingResponseMapper, times(1)).convertToResponse(any(WaitingEntity.class));
	}

	@Test
	@DisplayName("웨이팅 등록 예외 처리 테스트")
	void add_waiting_failure_invalid_service_type() {
		// given
		Long restaurantId = 1L;
		Long memberId = 1L;
		WaitingRequest waitingRequest = WaitingRequest.builder()
			.memberCount(3)
			.waitingType(WaitingType.TAKE_OUT)
			.build();

		// Mock the member entity
		when(memberRepository.findById(memberId)).thenReturn(Optional.of(memberEntity));

		// Mock an invalid restaurant entity
		RestaurantEntity invalidRestaurant = RestaurantEntity.builder()
			.serviceType(ServiceType.RESERVATION) // Invalid service type
			.build();
		when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(invalidRestaurant));

		// when & then
		assertThrows(ServiceTypeException.class, () -> {
			waitingService.addWaiting(restaurantId, waitingRequest, memberId);
		});
		verify(memberRepository, times(1)).findById(memberId);
		verify(restaurantRepository, times(1)).findById(restaurantId);
		verify(waitingRepository, never()).save(any(WaitingEntity.class));
		verify(waitingResponseMapper, never()).convertToResponse(any(WaitingEntity.class));
	}

	@Test
	@DisplayName("웨이팅 취소 테스트")
	void cancel_waiting_success() {
		// given
		Long id = 1L;
		WaitingEntity waitingEntity = WaitingEntity.builder()
			.status(Status.SCHEDULED) // 현재 상태는 SCHEDULED
			.build();

		// Mock the repository to return the waiting entity
		when(waitingRepository.findById(id)).thenReturn(Optional.of(waitingEntity));

		// when
		waitingService.cancelWaiting(id);

		// then
		assertEquals(Status.CANCELED, waitingEntity.getStatus()); // 상태가 CANCELED로 변경되었는지 확인
		verify(waitingRepository, times(1)).save(waitingEntity); // save 메서드가 호출되었는지 확인
	}

	@Test
	@DisplayName("웨이팅 취소 예외 처리 테스트")
	void cancel_waiting_failure() {
		// given
		Long id = 1L;

		// Mock the repository to return an empty Optional
		when(waitingRepository.findById(id)).thenReturn(Optional.empty());

		// when & then
		assertThrows(IllegalArgumentException.class, () -> {
			waitingService.cancelWaiting(id);
		});
		verify(waitingRepository, never()).save(any(WaitingEntity.class)); // save 메서드가 호출되지 않았는지 확인
	}

}
