package org.example.catch_line.notification.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotificationEntity is a Querydsl query type for NotificationEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotificationEntity extends EntityPathBase<NotificationEntity> {

    private static final long serialVersionUID = 1273507033L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotificationEntity notificationEntity = new QNotificationEntity("notificationEntity");

    public final org.example.catch_line.common.model.entity.QBaseTimeEntity _super = new org.example.catch_line.common.model.entity.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath isRead = createBoolean("isRead");

    public final org.example.catch_line.user.member.model.entity.QMemberEntity member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Long> notificationId = createNumber("notificationId", Long.class);

    public final org.example.catch_line.booking.reservation.model.entity.QReservationEntity reservation;

    public final StringPath url = createString("url");

    public final org.example.catch_line.booking.waiting.model.entity.QWaitingEntity waiting;

    public QNotificationEntity(String variable) {
        this(NotificationEntity.class, forVariable(variable), INITS);
    }

    public QNotificationEntity(Path<? extends NotificationEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotificationEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotificationEntity(PathMetadata metadata, PathInits inits) {
        this(NotificationEntity.class, metadata, inits);
    }

    public QNotificationEntity(Class<? extends NotificationEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.example.catch_line.user.member.model.entity.QMemberEntity(forProperty("member")) : null;
        this.reservation = inits.isInitialized("reservation") ? new org.example.catch_line.booking.reservation.model.entity.QReservationEntity(forProperty("reservation"), inits.get("reservation")) : null;
        this.waiting = inits.isInitialized("waiting") ? new org.example.catch_line.booking.waiting.model.entity.QWaitingEntity(forProperty("waiting"), inits.get("waiting")) : null;
    }

}

