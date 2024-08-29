package org.example.catch_line.booking.reservation.controller;

import org.example.catch_line.booking.reservation.model.dto.ReservationRequest;
import org.example.catch_line.booking.reservation.model.dto.ReservationResponse;
import org.example.catch_line.booking.reservation.service.ReservationService;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.exception.booking.DuplicateReservationTimeException;
import org.example.catch_line.user.auth.details.MemberUserDetails;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

	@Mock
	private ReservationService reservationService;

	@Mock
	private Model model;

	@InjectMocks
	private ReservationController reservationController;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		// ViewResolver 설정
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");

		mockMvc = MockMvcBuilders.standaloneSetup(reservationController)
				.setViewResolvers(viewResolver)
				.build();
	}

	@Test
	@DisplayName("예약 폼 페이지 로드 테스트")
	void addReservationForm() throws Exception {
		mockMvc.perform(get("/restaurants/1/reservation"))
				.andExpect(status().isOk())
				.andExpect(view().name("reservation/reservation"))
				.andExpect(model().attributeExists("restaurantId"));
	}
//
//	@Test
//	@DisplayName("예약 등록 성공 테스트")
//	void addReservation_success() throws Exception {
//		// 테스트에 사용할 가짜 데이터 설정
//		ReservationRequest reservationRequest = new ReservationRequest(3, LocalDateTime.now(), Status.SCHEDULED);
//		ReservationResponse reservationResponse = new ReservationResponse(1L, 3, LocalDateTime.now(), Status.SCHEDULED, LocalDateTime.now(), LocalDateTime.now());
//
//		// Mock 객체 설정
//		MemberEntity member = mock(MemberEntity.class);
////		when(member.getMemberId()).thenReturn(1L);  // getMemberId()가 1L을 반환하도록 설정
//
//		MemberUserDetails userDetails = mock(MemberUserDetails.class);
//		when(userDetails.getMember()).thenReturn(member);  // getMember()가 member 객체를 반환하도록 설정
//
//		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//
//		when(reservationService.addReservation(anyLong(), anyLong(), any(ReservationRequest.class))).thenReturn(reservationResponse);
//
//		// POST 요청을 보냄
//		mockMvc.perform(post("/restaurants/1/reservation")
//						.flashAttr("reservationRequest", reservationRequest))
//				.andExpect(status().is3xxRedirection())
//				.andExpect(redirectedUrl("/history"))
//				.andExpect(model().attributeExists("reservationResponse"));
//
//		// 서비스 메서드가 올바르게 호출되었는지 검증
//		verify(reservationService, times(1)).addReservation(anyLong(), anyLong(), any(ReservationRequest.class));
//	}
//
//	@Test
//	@DisplayName("예약 중복 예외 테스트")
//	void addReservation_conflict() throws Exception {
//		ReservationRequest reservationRequest = new ReservationRequest(3, LocalDateTime.now(), Status.SCHEDULED);
//
//		MemberEntity member = mock(MemberEntity.class);
//		when(member.getMemberId()).thenReturn(1L);
//		MemberUserDetails userDetails = new MemberUserDetails(member);
//
//		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//
//		doThrow(new DuplicateReservationTimeException())
//				.when(reservationService).addReservation(anyLong(), anyLong(), any(ReservationRequest.class));
//
//		mockMvc.perform(post("/restaurants/1/reservation")
//						.flashAttr("reservationRequest", reservationRequest))
//				.andExpect(status().is3xxRedirection())
//				.andExpect(redirectedUrl("/restaurants/1/reservation"))
//				.andExpect(flash().attributeExists("error"));
//
//		verify(reservationService, times(1)).addReservation(anyLong(), anyLong(), any(ReservationRequest.class));
//	}
}
