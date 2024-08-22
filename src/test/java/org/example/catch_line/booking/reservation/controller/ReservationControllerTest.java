package org.example.catch_line.booking.reservation.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.example.catch_line.booking.reservation.model.dto.ReservationRequest;
import org.example.catch_line.booking.reservation.model.dto.ReservationResponse;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.service.ReservationService;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

class ReservationControllerTest {

	private MockMvc mockMvc;

	@Mock
	private ReservationService reservationService;

	@InjectMocks
	private ReservationController reservationController;

	private ReservationEntity reservationEntity;
	private RestaurantEntity restaurantEntity;
	private MemberEntity memberEntity;
	private LocalDateTime reservationDate;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		// ViewResolver 설정 (thymeleaf 등 사용 시 필요)
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".html");

		mockMvc = MockMvcBuilders.standaloneSetup(reservationController)
			.setViewResolvers(viewResolver)
			.build();
	}

	@Test
	@DisplayName("GET /restaurants/{restaurantId}/reservation - 예약 폼 페이지로 이동")
	void testAddReservationForm() throws Exception {
		mockMvc.perform(get("/restaurants/1/reservation"))
			.andExpect(status().isOk())
			.andExpect(view().name("reservation/reservation"));
	}

	@Test
	@DisplayName("POST /restaurants/{restaurantId}/reservation - 예약 추가 성공")
	void testAddReservationSuccess() throws Exception {
		ReservationRequest reservationRequest = ReservationRequest.builder()
			.memberCount(3)
			.reservationDate(reservationDate)
			.build();
		ReservationResponse reservationResponse = ReservationResponse.builder()
			.reservationId(1L)
			.memberCount(3)
			.status(Status.SCHEDULED)
			.reservationDate(reservationDate)
			.build();

		when(reservationService.addReservation(anyLong(), anyLong(), any(ReservationRequest.class)))
			.thenReturn(reservationResponse);

		mockMvc.perform(post("/restaurants/1/reservation")
				.flashAttr("reservationRequest", reservationRequest)
				.sessionAttr("memberId", 1L)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/history")); // 리디렉션 URL만 확인
	}

	@Test
	@DisplayName("POST /restaurants/{restaurantId}/reservation - 예약 추가 실패")
	void testAddReservationFailure() throws Exception {
		ReservationRequest reservationRequest = ReservationRequest.builder()
			.memberCount(3)
			.reservationDate(reservationDate)
			.build();

		when(reservationService.addReservation(anyLong(), anyLong(), any(ReservationRequest.class)))
			.thenThrow(new IllegalArgumentException("Reservation failed"));

		mockMvc.perform(post("/restaurants/1/reservation")
				.flashAttr("reservationRequest", reservationRequest)
				.sessionAttr("memberId", 1L)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/restaurants/1/reservation"))
			.andExpect(flash().attributeExists("error"));
	}
}
