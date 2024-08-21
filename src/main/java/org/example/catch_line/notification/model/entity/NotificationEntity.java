package org.example.catch_line.notification.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.common.model.entity.BaseTimeEntity;
import org.example.catch_line.user.member.model.entity.MemberEntity;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private ReservationEntity reservation;

    private String content;

    private String url;

    private boolean isRead;

    @Builder
    public NotificationEntity(MemberEntity member, ReservationEntity reservation, String content, String url, boolean isRead) {
        this.member = member;
        this.reservation = reservation;
        this.content = content;
        this.url = url;
        this.isRead = isRead;
    }

    public void read() {
        this.isRead = true;
    }
}
