package org.example.catch_line.booking.waiting.service;

import org.example.catch_line.booking.waiting.model.dto.WaitingRequest;
import org.example.catch_line.booking.waiting.model.dto.WaitingResponse;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.model.entity.WaitingType;
import org.example.catch_line.booking.waiting.model.mapper.WaitingResponseMapper;
import org.example.catch_line.booking.waiting.repository.WaitingRepository;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.example.catch_line.exception.booking.WaitingException;
import org.example.catch_line.history.validation.HistoryValidator;
import org.example.catch_line.notification.service.NotificationService;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.provider.validation.MemberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingServiceTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private WaitingRepository waitingRepository;

    @Mock
    private WaitingResponseMapper waitingResponseMapper;

    @Mock
    private HistoryValidator historyValidator;

    @Mock
    private MemberValidator memberValidator;

    @Mock
    private RestaurantValidator restaurantValidator;

    @InjectMocks
    private WaitingService waitingService;

    private MemberEntity memberEntity;
    private RestaurantEntity restaurantEntity;
    private WaitingEntity waitingEntity;

    @BeforeEach
    void setUp() {
        memberEntity = mock(MemberEntity.class);
        restaurantEntity = mock(RestaurantEntity.class);
        waitingEntity = mock(WaitingEntity.class);
    }

    @Test
    @DisplayName("웨이팅 등록 성공 테스트")
    void addWaiting_success() {
        WaitingRequest waitingRequest = new WaitingRequest(3, WaitingType.TAKE_OUT);
        WaitingResponse waitingResponse = new WaitingResponse(1L, 3, Status.SCHEDULED,waitingRequest.getWaitingType(), LocalDateTime.now(),LocalDateTime.now());

        when(memberValidator.checkIfMemberPresent(anyLong())).thenReturn(memberEntity);
        when(restaurantValidator.checkIfRestaurantPresent(anyLong())).thenReturn(restaurantEntity);
        when(waitingRepository.save(any(WaitingEntity.class))).thenReturn(waitingEntity);
        when(waitingResponseMapper.convertToResponse(any(WaitingEntity.class))).thenReturn(waitingResponse);

        WaitingResponse response = waitingService.addWaiting(1L, waitingRequest, 1L);

        assertNotNull(response);
        verify(notificationService, times(1)).sendWaiting(any(MemberEntity.class), any(WaitingEntity.class), anyString());
        verify(waitingRepository, times(1)).save(any(WaitingEntity.class));
    }

    @Test
    @DisplayName("웨이팅 취소 테스트")
    void cancelWaiting_success() {
        when(memberValidator.checkIfMemberPresent(anyLong())).thenReturn(memberEntity);
        when(historyValidator.checkIfWaitingPresent(anyLong())).thenReturn(waitingEntity);

        waitingService.cancelWaiting(1L, 1L);

        verify(waitingEntity, times(1)).canceled();
        verify(notificationService, times(1)).sendWaiting(any(MemberEntity.class), any(WaitingEntity.class), anyString());
    }

    @Test
    @DisplayName("웨이팅 완료 테스트")
    void completedWaiting_success() {
        when(historyValidator.checkIfWaitingPresent(anyLong())).thenReturn(waitingEntity);

        waitingService.completedWaiting(1L);

        verify(waitingEntity, times(1)).completed();
    }

    @Test
    @DisplayName("웨이팅 존재 여부 확인 테스트")
    void isExistingWaiting_success() {
        when(waitingRepository.existsByMemberMemberIdAndStatus(anyLong(), any(Status.class))).thenReturn(true);

        boolean exists = waitingService.isExistingWaiting(1L, Status.SCHEDULED);

        assertTrue(exists);
        verify(waitingRepository, times(1)).existsByMemberMemberIdAndStatus(anyLong(), any(Status.class));
    }

    @Test
    @DisplayName("웨이팅 엔티티 조회 실패 테스트")
    void getWaitingEntity_notFound() {
        when(waitingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(WaitingException.class, () -> waitingService.getWaitingEntity(1L));
    }

    @Test
    @DisplayName("웨이팅 엔티티 조회 성공 테스트")
    void getWaitingEntity_success() {
        when(waitingRepository.findById(anyLong())).thenReturn(Optional.of(waitingEntity));

        WaitingEntity foundEntity = waitingService.getWaitingEntity(1L);

        assertNotNull(foundEntity);
        assertEquals(waitingEntity, foundEntity);
    }
}
