package org.example.catch_line.history.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.example.catch_line.booking.reservation.service.ReservationService;
import org.example.catch_line.booking.waiting.service.WaitingService;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.history.service.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

class HistoryControllerTest {

	private MockMvc mockMvc;

	@Mock
	private ReservationService reservationService;

	@Mock
	private WaitingService waitingService;

	@Mock
	private HistoryService historyService;

	@InjectMocks
	private HistoryController historyController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		// ViewResolver 설정 (thymeleaf 등 사용 시 필요)
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".html");

		mockMvc = MockMvcBuilders.standaloneSetup(historyController)
			.setViewResolvers(viewResolver)
			.build();
	}

	@Test
	@DisplayName("GET /history - 모든 히스토리 가져오기")
	void testGetHistories() throws Exception {
		List<HistoryResponse> historyList = Collections.singletonList(
			HistoryResponse.builder()
				.restaurantName("Test Restaurant")
				.build()
		);

		when(historyService.getAllHistory(anyLong(), any(Status.class)))
			.thenReturn(historyList);

		mockMvc.perform(get("/history")
				.sessionAttr("memberId", 1L)
				.param("status", "SCHEDULED"))
			.andExpect(status().isOk())
			.andExpect(view().name("history/history"))
			.andExpect(model().attribute("history", historyList));  // 모델에 추가된 값 검증
	}

	@Test
	@DisplayName("GET /history/waiting/{waitingId} - 웨이팅 상세보기")
	void testGetWaitingDetail() throws Exception {
		HistoryResponse historyResponse = HistoryResponse.builder()
			.restaurantName("Test Restaurant")
			.build();

		when(historyService.findWaitingDetailById(any(List.class), anyLong()))
			.thenReturn(historyResponse);

		mockMvc.perform(get("/history/waiting/1")
				.sessionAttr("historyList", Collections.singletonList(historyResponse)))
			.andExpect(status().isOk())
			.andExpect(view().name("history/waitingDetail"))
			.andExpect(model().attributeExists("historyResponse"));
	}

	@Test
	@DisplayName("GET /history/reservation/{reservationId} - 예약 상세보기")
	void testGetReservationDetail() throws Exception {
		HistoryResponse historyResponse = HistoryResponse.builder()
			.restaurantName("Test Restaurant")
			.build();

		when(historyService.findReservationDetailById(any(List.class), anyLong()))
			.thenReturn(historyResponse);

		mockMvc.perform(get("/history/reservation/1")
				.sessionAttr("historyList", Collections.singletonList(historyResponse)))
			.andExpect(status().isOk())
			.andExpect(view().name("history/reservationDetail"))
			.andExpect(model().attributeExists("historyResponse"));
	}

	@Test
	@DisplayName("POST /history/reservation/{reservationId} - 예약 삭제")
	void testDeleteReservation() throws Exception {
		doNothing().when(reservationService).cancelReservation(anyLong());

		mockMvc.perform(post("/history/reservation/1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/history"));
	}

	@Test
	@DisplayName("POST /history/waiting/{waitingId} - 웨이팅 삭제")
	void testDeleteWaiting() throws Exception {
		doNothing().when(waitingService).cancelWaiting(anyLong());

		mockMvc.perform(post("/history/waiting/1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/history"));
	}
}
