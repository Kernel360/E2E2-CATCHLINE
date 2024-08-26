package org.example.catch_line.notification.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.catch_line.notification.model.entity.NotificationEntity;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationResponse {

    private Long notificationId;
    private String content;
    // 알림 클릭 시 이동할 경로
    private String url;
    private LocalDateTime createdAt;
    // 알림 읽음 여부
    private boolean read;

    @Builder
    public NotificationResponse(Long notificationId, String content, String url, LocalDateTime createdAt, boolean read) {
        this.notificationId = notificationId;
        this.content = content;
        this.url = url;
        this.createdAt = createdAt;
        this.read = read;
    }

    public static NotificationResponse from(NotificationEntity notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .content(notification.getContent())
                .url(notification.getUrl())
                .createdAt(notification.getCreatedAt())
                .read(notification.isRead())
                .build();
    }
}
