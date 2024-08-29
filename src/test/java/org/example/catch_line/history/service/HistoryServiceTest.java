package org.example.catch_line.history.service;

import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.repository.ReservationRepository;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.model.entity.WaitingType;
import org.example.catch_line.booking.waiting.repository.WaitingRepository;
import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.exception.booking.HistoryException;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.history.model.mapper.HistoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @Mock
    private WaitingRepository waitingRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private HistoryMapper historyMapper;

    @InjectMocks
    private HistoryService historyService;

    private ReservationEntity reservationEntity;
    private WaitingEntity waitingEntity;

    @BeforeEach
    void setUp() {
        reservationEntity = mock(ReservationEntity.class);
        waitingEntity = mock(WaitingEntity.class);
    }

    @Test
    @DisplayName("모든 이력 조회 테스트")
    void getAllHistory_success() {
        List<WaitingEntity> waitingEntities = new ArrayList<>();
        waitingEntities.add(waitingEntity);

        List<ReservationEntity> reservationEntities = new ArrayList<>();
        reservationEntities.add(reservationEntity);

        when(waitingRepository.findByMemberMemberIdAndStatus(anyLong(), any(Status.class))).thenReturn(waitingEntities);
        when(reservationRepository.findByMemberMemberIdAndStatus(anyLong(), any(Status.class))).thenReturn(reservationEntities);

        // Mock the HistoryResponse for waiting and reservation entities
        HistoryResponse waitingHistoryResponse = createMockHistoryResponse(1L, null);
        HistoryResponse reservationHistoryResponse = createMockHistoryResponse(null, 1L);

        when(historyMapper.waitingToHistoryResponse(any(WaitingEntity.class), anyInt(), anyInt())).thenReturn(waitingHistoryResponse);
        when(historyMapper.reservationToHistoryResponse(any(ReservationEntity.class))).thenReturn(reservationHistoryResponse);

        List<HistoryResponse> historyResponses = historyService.getAllHistory(1L, Status.SCHEDULED);

        assertNotNull(historyResponses);
        assertEquals(2, historyResponses.size()); // waitingEntity와 reservationEntity 각 1개씩
    }

    @Test
    @DisplayName("예약 ID로 이력 상세 조회 테스트")
    void findReservationDetailById_success() {
        List<HistoryResponse> historyResponses = new ArrayList<>();
        HistoryResponse historyResponse = createMockHistoryResponse(null, 1L);
        historyResponses.add(historyResponse);

        HistoryResponse result = historyService.findReservationDetailById(historyResponses, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getReservationId());
    }

    @Test
    @DisplayName("예약 ID로 이력 상세 조회 실패 테스트")
    void findReservationDetailById_notFound() {
        List<HistoryResponse> historyResponses = new ArrayList<>();

        assertThrows(HistoryException.class, () -> historyService.findReservationDetailById(historyResponses, 1L));
    }

    @Test
    @DisplayName("웨이팅 ID로 이력 상세 조회 테스트")
    void findWaitingDetailById_success() {
        List<HistoryResponse> historyResponses = new ArrayList<>();
        HistoryResponse historyResponse = createMockHistoryResponse(1L, null);
        historyResponses.add(historyResponse);

        HistoryResponse result = historyService.findWaitingDetailById(historyResponses, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getWaitingId());
    }

    @Test
    @DisplayName("웨이팅 ID로 이력 상세 조회 실패 테스트")
    void findWaitingDetailById_notFound() {
        List<HistoryResponse> historyResponses = new ArrayList<>();

        assertThrows(HistoryException.class, () -> historyService.findWaitingDetailById(historyResponses, 1L));
    }

    @Test
    @DisplayName("웨이팅 ID로 웨이팅 상세 조회 성공 테스트")
    void findWaitingDetailByWaitingId_success() {
        when(waitingRepository.findByWaitingId(anyLong())).thenReturn(Optional.of(waitingEntity));
        HistoryResponse historyResponse = createMockHistoryResponse(1L, null);
        when(historyMapper.waitingToHistoryResponse(any(WaitingEntity.class), anyInt(), anyInt())).thenReturn(historyResponse);

        HistoryResponse result = historyService.findWaitingDetailByWaitingId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getWaitingId());
    }

    @Test
    @DisplayName("웨이팅 ID로 웨이팅 상세 조회 실패 테스트")
    void findWaitingDetailByWaitingId_notFound() {
        when(waitingRepository.findByWaitingId(anyLong())).thenReturn(Optional.empty());

        assertThrows(HistoryException.class, () -> historyService.findWaitingDetailByWaitingId(1L));
    }

    @Test
    @DisplayName("예약 ID로 예약 상세 조회 성공 테스트")
    void findReservationDetailByReservationId_success() {
        when(reservationRepository.findByReservationId(anyLong())).thenReturn(Optional.of(reservationEntity));
        HistoryResponse historyResponse = createMockHistoryResponse(null, 1L);
        when(historyMapper.reservationToHistoryResponse(any(ReservationEntity.class))).thenReturn(historyResponse);

        HistoryResponse result = historyService.findReservationDetailByReservationId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getReservationId());
    }

    @Test
    @DisplayName("예약 ID로 예약 상세 조회 실패 테스트")
    void findReservationDetailByReservationId_notFound() {
        when(reservationRepository.findByReservationId(anyLong())).thenReturn(Optional.empty());

        assertThrows(HistoryException.class, () -> historyService.findReservationDetailByReservationId(1L));
    }

    @Test
    @DisplayName("레스토랑 ID로 이력 조회 테스트")
    void findByRestaurantId_success() {
        List<ReservationEntity> reservationEntities = new ArrayList<>();
        reservationEntities.add(reservationEntity);

        List<WaitingEntity> waitingEntities = new ArrayList<>();
        waitingEntities.add(waitingEntity);

        when(reservationRepository.findAllByRestaurantRestaurantIdAndStatus(anyLong(), any(Status.class))).thenReturn(reservationEntities);
        when(waitingRepository.findAllByRestaurantRestaurantIdAndStatus(anyLong(), any(Status.class))).thenReturn(waitingEntities);

        HistoryResponse waitingHistoryResponse = createMockHistoryResponse(1L, null);
        HistoryResponse reservationHistoryResponse = createMockHistoryResponse(null, 1L);

        when(historyMapper.reservationToHistoryResponse(any(ReservationEntity.class))).thenReturn(reservationHistoryResponse);
        when(historyMapper.waitingToHistoryResponse(any(WaitingEntity.class), anyInt(), anyInt())).thenReturn(waitingHistoryResponse);

        List<HistoryResponse> historyResponses = historyService.findByRestaurantId(1L, Status.SCHEDULED);

        assertNotNull(historyResponses);
        assertEquals(2, historyResponses.size());
    }

    private HistoryResponse createMockHistoryResponse(Long waitingId, Long reservationId) {
        return HistoryResponse.builder()
                .restaurantId(1L)
                .waitingId(waitingId)
                .reservationId(reservationId)
                .memberCount(3)
                .restaurantName("Mock Restaurant")
                .status(Status.SCHEDULED)
                .waitingType(WaitingType.TAKE_OUT)
                .serviceType(ServiceType.WAITING)
                .reservationDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .waitingRegistrationId(1)
                .myWaitingPosition(1)
                .build();
    }
}
