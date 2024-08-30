package org.example.catch_line.notification.service;

import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.notification.model.entity.NotificationEntity;
import org.example.catch_line.notification.repository.EmitterRepository;
import org.example.catch_line.notification.repository.NotificationRepository;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock EmitterRepository emitterRepository;
    @Mock NotificationRepository notificationRepository;
    @InjectMocks NotificationService notificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConnect() {
        Long memberId = 1L;
        String lastEventId = "";
        final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

        when(emitterRepository.save(anyString(), any(SseEmitter.class)))
                .thenReturn(new SseEmitter(DEFAULT_TIMEOUT));

        SseEmitter emitter = notificationService.connect(memberId, lastEventId);

        assertNotNull(emitter);
        verify(emitterRepository, times(1)).save(anyString(), any(SseEmitter.class));
    }

    @Test
    public void testConnectWithLastEventId() {
        Long memberId = 1L;
        String lastEventId = "123456";
        final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

        when(emitterRepository.save(anyString(), any(SseEmitter.class)))
                .thenReturn(new SseEmitter(DEFAULT_TIMEOUT));
        Map<String, Object> cachedEvents = new HashMap<>();
        cachedEvents.put("123457", "Event 1");
        when(emitterRepository.findAllEventCacheStartWithId(anyString())).thenReturn(cachedEvents);

        SseEmitter emitter = notificationService.connect(memberId, lastEventId);

        assertNotNull(emitter);
        verify(emitterRepository, times(1)).findAllEventCacheStartWithId(anyString());
    }

    @Test
    public void testSendReservation() {
        // mock 객체 생성
        MemberEntity member = mock(MemberEntity.class);
        ReservationEntity reservation = mock(ReservationEntity.class);
        String content = "Reservation notification";

        // mock 객체의 메서드 호출 시 반환값 설정
        when(member.getMemberId()).thenReturn(1L);
        when(reservation.getRestaurant()).thenReturn(mock(RestaurantEntity.class));
        when(reservation.getRestaurant().getRestaurantId()).thenReturn(100L);

        // NotificationEntity의 mock 객체 생성 및 저장 동작 설정
        NotificationEntity notification = mock(NotificationEntity.class);
        when(notificationRepository.save(any(NotificationEntity.class))).thenReturn(notification);

        // SseEmitter의 mock 객체 및 리포지토리 동작 설정
        Map<String, SseEmitter> emitters = new HashMap<>();
        SseEmitter emitter = new SseEmitter();
        emitters.put("1_123456789", emitter);
        when(emitterRepository.findAllStartWithById(anyString())).thenReturn(emitters);

        // 테스트 대상 메서드 호출
        notificationService.sendReservation(member, reservation, content);

        // 검증: notificationRepository에 save가 1회 호출되었는지 확인
        verify(notificationRepository, times(1)).save(any(NotificationEntity.class));
        // 검증: emitterRepository의 findAllStartWithById가 1회 호출되었는지 확인
        verify(emitterRepository, times(1)).findAllStartWithById(anyString());

        // Note: 직접적으로 SseEmitter를 테스트하기는 어렵기 때문에, mock과 verify를 사용하여 메서드 호출을 확인합니다.
    }

    @Test
    public void testSendWaiting() {
        // mock 객체 생성
        MemberEntity member = mock(MemberEntity.class);
        WaitingEntity waiting = mock(WaitingEntity.class);
        String content = "Waiting notification";

        // mock 객체의 메서드 호출 시 반환값 설정
        when(member.getMemberId()).thenReturn(1L);
        when(waiting.getRestaurant()).thenReturn(mock(RestaurantEntity.class));
        when(waiting.getRestaurant().getRestaurantId()).thenReturn(100L);

        // NotificationEntity의 mock 객체 생성 및 저장 동작 설정
        NotificationEntity notification = mock(NotificationEntity.class);
        when(notificationRepository.save(any(NotificationEntity.class))).thenReturn(notification);

        // SseEmitter의 mock 객체 및 리포지토리 동작 설정
        Map<String, SseEmitter> emitters = new HashMap<>();
        SseEmitter emitter = new SseEmitter();
        emitters.put("1_123456789", emitter);
        when(emitterRepository.findAllStartWithById(anyString())).thenReturn(emitters);

        // 테스트 대상 메서드 호출
        notificationService.sendWaiting(member, waiting, content);

        // 검증: notificationRepository에 save가 1회 호출되었는지 확인
        verify(notificationRepository, times(1)).save(any(NotificationEntity.class));
        // 검증: emitterRepository의 findAllStartWithById가 1회 호출되었는지 확인
        verify(emitterRepository, times(1)).findAllStartWithById(anyString());
        // 검증: saveEventCache와 sendToClient 호출
        verify(emitterRepository, times(1)).saveEventCache(anyString(), any(NotificationEntity.class));
    }

}