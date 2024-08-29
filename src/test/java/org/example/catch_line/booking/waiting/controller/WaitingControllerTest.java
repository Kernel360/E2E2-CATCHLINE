package org.example.catch_line.booking.waiting.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.assertj.core.api.Assertions;
import org.example.catch_line.booking.waiting.model.dto.WaitingRequest;
import org.example.catch_line.booking.waiting.model.dto.WaitingResponse;
import org.example.catch_line.booking.waiting.model.entity.WaitingType;
import org.example.catch_line.booking.waiting.service.WaitingService;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.user.auth.details.MemberUserDetails;
import org.example.catch_line.user.member.model.entity.MemberEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.lang.reflect.Field;

class WaitingControllerTest {

	private MockMvc mockMvc;

	@Mock
	private WaitingService waitingService;

	@InjectMocks
	private WaitingController waitingController;

	@Mock
	private Authentication authentication;

	@Mock
	private SecurityContext securityContext;

	@Mock
	Model model;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private MemberUserDetails mockUserDetails;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		// ViewResolver 설정 (thymeleaf 등 사용 시 필요)
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".html");

		mockMvc = MockMvcBuilders.standaloneSetup(waitingController)
				.setViewResolvers(viewResolver)
				.build();

		// SecurityContext 설정
		MemberEntity memberEntity = mock(MemberEntity.class);

		// MemberUserDetails 초기화
		mockUserDetails = mock(MemberUserDetails.class);
		when(mockUserDetails.getMember()).thenReturn(memberEntity);

		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(mockUserDetails);
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	@DisplayName("GET /restaurants/{restaurantId}/waiting - 웨이팅 폼 페이지로 이동")
	void testAddWaitingForm() throws Exception {
		mockMvc.perform(get("/restaurants/1/waiting"))
				.andExpect(status().isOk())
				.andExpect(view().name("waiting/waiting"));
	}

	@Test
	@DisplayName("POST /restaurants/{restaurantId}/waiting - 웨이팅 등록 테스트")
	void testAddWaitingSuccess() throws Exception {
		WaitingRequest waitingRequest = WaitingRequest.builder()
				.memberCount(3)
				.waitingType(WaitingType.DINE_IN)
				.build();

		WaitingResponse waitingResponse = WaitingResponse.builder()
				.waitingId(1L)
				.memberCount(3)
				.waitingType(WaitingType.DINE_IN)
				.build();

		Model mockModel = mock(Model.class);

		// Mock WaitingService의 메서드 호출 설정
		when(waitingService.isExistingWaiting(anyLong(), any(Status.class))).thenReturn(false);
		when(waitingService.addWaiting(anyLong(), any(WaitingRequest.class), anyLong())).thenReturn(waitingResponse);

		String response = waitingController.addWaiting(1L, waitingRequest, mockUserDetails, mockModel);

		assertNotNull(response);
	}

	@Test
	@DisplayName("POST /restaurants/{restaurantId}/waiting - 웨이팅 등록 예외처리 테스트")
	void testAddWaitingFailure() throws Exception {
		WaitingRequest waitingRequest = WaitingRequest.builder()
				.memberCount(3)
				.waitingType(WaitingType.DINE_IN)
				.build();

		// WaitingService가 예외를 던지도록 설정
		when(waitingService.addWaiting(anyLong(), any(WaitingRequest.class), anyLong()))
				.thenThrow(new IllegalArgumentException("Waiting failed"));


	}
}