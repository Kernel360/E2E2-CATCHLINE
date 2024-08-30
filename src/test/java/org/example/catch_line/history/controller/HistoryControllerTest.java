package org.example.catch_line.history.controller;

import org.example.catch_line.booking.reservation.model.dto.ReservationRequest;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.service.ReservationService;
import org.example.catch_line.booking.waiting.service.WaitingService;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.exception.booking.DuplicateReservationTimeException;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.history.service.HistoryService;
import org.example.catch_line.history.validation.HistoryValidator;
import org.example.catch_line.user.auth.details.MemberUserDetails;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HistoryControllerTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private WaitingService waitingService;

    @Mock
    private HistoryService historyService;

    @Mock
    private HistoryValidator historyValidator;

    @InjectMocks
    private HistoryController historyController;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private MemberUserDetails userDetails;

    @Mock
    private MemberEntity member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Mocking MemberEntity and setting it to userDetails
        when(userDetails.getMember()).thenReturn(member);
        when(member.getMemberId()).thenReturn(1L);
    }

    @Test
    @DisplayName("이력 조회 성공 테스트")
    void getHistories_success() {
        List<HistoryResponse> historyResponses = new ArrayList<>();
        historyResponses.add(mock(HistoryResponse.class));

        when(historyService.getAllHistory(anyLong(), any(Status.class))).thenReturn(historyResponses);

        String viewName = historyController.getHistories(model, userDetails, Status.SCHEDULED);

        assertEquals("history/history", viewName);
        verify(model, times(1)).addAttribute("history", historyResponses);
    }

    @Test
    @DisplayName("웨이팅 상세 조회 성공 테스트")
    void getWaitingDetail_success() {
        List<HistoryResponse> historyResponses = new ArrayList<>();
        historyResponses.add(mock(HistoryResponse.class));

        when(historyService.getAllHistory(anyLong(), any(Status.class))).thenReturn(historyResponses);
        when(historyService.findWaitingDetailById(anyList(), anyLong())).thenReturn(mock(HistoryResponse.class));

        String viewName = historyController.getWaitingDetail(1L, model, userDetails, Status.SCHEDULED);

        assertEquals("history/waitingDetail", viewName);
        verify(model, times(1)).addAttribute(eq("historyResponse"), any(HistoryResponse.class));
    }

    @Test
    @DisplayName("예약 상세 조회 성공 테스트")
    void getReservationDetail_success() {
        List<HistoryResponse> historyResponses = new ArrayList<>();
        historyResponses.add(mock(HistoryResponse.class));

        when(historyService.getAllHistory(anyLong(), any(Status.class))).thenReturn(historyResponses);
        when(historyService.findReservationDetailById(anyList(), anyLong())).thenReturn(mock(HistoryResponse.class));

        String viewName = historyController.getReservationDetail(1L, model, userDetails, Status.SCHEDULED);

        assertEquals("history/reservationDetail", viewName);
        verify(model, times(1)).addAttribute(eq("historyResponse"), any(HistoryResponse.class));
    }

    @Test
    @DisplayName("예약 삭제 성공 테스트")
    void deleteReservation_success() {
        String result = historyController.deleteReservation(1L, model, userDetails);

        assertEquals("ok", result);
        verify(historyValidator, times(1)).validateReservationOwnership(anyLong(), anyLong());
        verify(reservationService, times(1)).cancelReservation(anyLong(), anyLong());
    }

    @Test
    @DisplayName("웨이팅 삭제 성공 테스트")
    void deleteWaiting_success() {
        String result = historyController.deleteWaiting(1L, model, userDetails);

        assertEquals("ok", result);
        verify(historyValidator, times(1)).validateWaitingOwnership(anyLong(), anyLong());
        verify(waitingService, times(1)).cancelWaiting(anyLong(), anyLong());
    }

    @Test
    @DisplayName("예약 업데이트 폼 조회 테스트")
    void updateForm_success() {
        when(reservationService.findReservationById(anyLong())).thenReturn(mock(ReservationEntity.class));

        String viewName = historyController.updateForm(1L, model);

        assertEquals("reservation/updateReservation", viewName);
        verify(model, times(1)).addAttribute(eq("reservationRequest"), any());
        verify(model, times(1)).addAttribute(eq("reservationId"), anyLong());
    }

    @Test
    @DisplayName("예약 업데이트 성공 테스트")
    void updateReservation_success() {
        ReservationRequest updateRequest = mock(ReservationRequest.class);
        when(updateRequest.getMemberCount()).thenReturn(2);
        when(updateRequest.getReservationDate()).thenReturn(mock(LocalDateTime.class));

        String viewName = historyController.updateReservation(1L, updateRequest, redirectAttributes, userDetails);

        assertEquals("redirect:/history", viewName);
        verify(historyValidator, times(1)).validateReservationOwnership(anyLong(), anyLong());
        verify(reservationService, times(1)).updateReservation(anyLong(), anyLong(), anyInt(), any(LocalDateTime.class));
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("message"), anyString());
    }

    @Test
    @DisplayName("예약 업데이트 중복 시간 예외 처리 테스트")
    void updateReservation_duplicateTimeException() {
        when(userDetails.getMember().getMemberId()).thenReturn(1L);

        // 예외를 던지도록 설정
        doThrow(new DuplicateReservationTimeException())
                .when(reservationService).updateReservation(anyLong(), anyLong(), anyInt(), any(LocalDateTime.class));

        ReservationRequest updateRequest = mock(ReservationRequest.class);
        when(updateRequest.getMemberCount()).thenReturn(2);
        when(updateRequest.getReservationDate()).thenReturn(mock(LocalDateTime.class));

        String viewName = historyController.updateReservation(1L, updateRequest, redirectAttributes, userDetails);

        assertEquals("redirect:/history/reservation/{reservationId}/edit", viewName);  // 예상된 리다이렉트 경로 확인
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("error"), anyString());
    }

}
