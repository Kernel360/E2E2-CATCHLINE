package org.example.catch_line.booking.reservation.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservationEntity is a Querydsl query type for ReservationEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservationEntity extends EntityPathBase<ReservationEntity> {

    private static final long serialVersionUID = -1739060920L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservationEntity reservationEntity = new QReservationEntity("reservationEntity");

    public final org.example.catch_line.common.model.entity.QBaseTimeEntity _super = new org.example.catch_line.common.model.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final org.example.catch_line.user.member.model.entity.QMemberEntity member;

    public final NumberPath<Integer> memberCount = createNumber("memberCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final DateTimePath<java.time.LocalDateTime> reservationDate = createDateTime("reservationDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> reservationId = createNumber("reservationId", Long.class);

    public final org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity restaurant;

    public final EnumPath<org.example.catch_line.common.constant.Status> status = createEnum("status", org.example.catch_line.common.constant.Status.class);

    public QReservationEntity(String variable) {
        this(ReservationEntity.class, forVariable(variable), INITS);
    }

    public QReservationEntity(Path<? extends ReservationEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservationEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservationEntity(PathMetadata metadata, PathInits inits) {
        this(ReservationEntity.class, metadata, inits);
    }

    public QReservationEntity(Class<? extends ReservationEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.example.catch_line.user.member.model.entity.QMemberEntity(forProperty("member")) : null;
        this.restaurant = inits.isInitialized("restaurant") ? new org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

