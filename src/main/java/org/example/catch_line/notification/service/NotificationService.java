package org.example.catch_line.notification.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.notification.model.dto.NotificationResponse;
import org.example.catch_line.notification.model.entity.NotificationEntity;
import org.example.catch_line.notification.repository.EmitterRepository;
import org.example.catch_line.notification.repository.NotificationRepository;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter connect(Long memberId, String lastEventId) {
        // 데이터가 유실된 시점 파악이 가능해짐. -> 저장된 key 값 비교를 통해 유실된 데이터만 재전송 가능해짐.
        String id = memberId + "_" + System.currentTimeMillis();

        // 유효 시간만큼 유지되고, 시간이 지나면 자동으로 클라이언트에서 재연결 요청
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> {
            emitterRepository.deleteById(id);
        });

        emitter.onTimeout(() -> {
            emitterRepository.deleteById(id);
        });

        emitter.onError((Throwable e) -> {
            log.info("연결 중 오류가 발생했습니다. ID: {}, 오류: {}", id, e.getMessage());
            emitterRepository.deleteById(id);
        });

        // 503 에러를 방지하기 위해 더미 데이터 전송
        sendToClient(emitter, id, "EventStream Created. [memberId=" + memberId + "]");

        // 클라이언트가 미수신한 Event 목록이 있는 경우 전송하여 Event 유실 예방
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(memberId));
            events.entrySet().stream()
                    .filter(e -> lastEventId.compareTo(e.getKey()) < 0)
                    .forEach(e -> sendToClient(emitter, e.getKey(), e.getValue()));
        }

        return emitter;
    }

    public void sendReservation(MemberEntity member, ReservationEntity reservation, String content) {
        NotificationEntity notification = createReservationNotification(member, reservation, content);
        String id = String.valueOf(member.getMemberId());
        notificationRepository.save(notification);

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter, key, NotificationResponse.from(notification));
                }
        );
    }

    public void sendWaiting(MemberEntity member, WaitingEntity waiting, String content) {
        NotificationEntity notification = createWaitingNotification(member, waiting, content);
        String id = String.valueOf(member.getMemberId());
        notificationRepository.save(notification);

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter, key, NotificationResponse.from(notification));
                }
        );
    }

    // TODO: 예약 생성, 수정, 삭제에 같은 url 넣는 중, 현재 url을 쓰진 않지만 추후에 쓰게 된다면 url 올바른 값 넣어야 함. 모두 같은 url 넣는 중
    private NotificationEntity createReservationNotification(MemberEntity member, ReservationEntity reservation, String content) {
        String url = String.format("/restaurants/%d/reservation", reservation.getRestaurant().getRestaurantId());
        return new NotificationEntity(member, reservation, content, url, false);
    }

    // TODO: 웨이팅 생성, 삭제에 같은 url 넣는 중, 현재 url을 쓰진 않지만 추후에 쓰게 된다면 url 올바른 값 넣어야 함. 모두 같은 url 넣는 중
    private NotificationEntity createWaitingNotification(MemberEntity member, WaitingEntity waiting, String content) {
        String url = String.format("/restaurants/%d/waiting", waiting.getRestaurant().getRestaurantId());
        return new NotificationEntity(member, waiting, content, url, false);
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(id);
            log.info("연결이 끊어졌습니다 : " + id);
        }
    }
}
