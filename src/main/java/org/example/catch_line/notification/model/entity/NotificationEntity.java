package org.example.catch_line.notification.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waiting_id")
    private WaitingEntity waiting;

    private String content;

    private String url;

    private boolean isRead;

    public NotificationEntity(MemberEntity member, ReservationEntity reservation, String content, String url, boolean isRead) {
        this.member = member;
        this.reservation = reservation;
        this.content = content;
        this.url = url;
        this.isRead = isRead;
    }

    public NotificationEntity(MemberEntity member, WaitingEntity waiting, String content, String url, boolean isRead) {
        this.member = member;
        this.waiting = waiting;
        this.content = content;
        this.url = url;
        this.isRead = isRead;
    }

    public void read() {
        this.isRead = true;
    }
}
