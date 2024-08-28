package org.example.catch_line.booking.waiting.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWaitingEntity is a Querydsl query type for WaitingEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWaitingEntity extends EntityPathBase<WaitingEntity> {

    private static final long serialVersionUID = -1790642456L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWaitingEntity waitingEntity = new QWaitingEntity("waitingEntity");

    public final org.example.catch_line.common.model.entity.QBaseTimeEntity _super = new org.example.catch_line.common.model.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final org.example.catch_line.user.member.model.entity.QMemberEntity member;

    public final NumberPath<Integer> memberCount = createNumber("memberCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity restaurant;

    public final EnumPath<org.example.catch_line.common.constant.Status> status = createEnum("status", org.example.catch_line.common.constant.Status.class);

    public final NumberPath<Long> waitingId = createNumber("waitingId", Long.class);

    public final EnumPath<WaitingType> waitingType = createEnum("waitingType", WaitingType.class);

    public QWaitingEntity(String variable) {
        this(WaitingEntity.class, forVariable(variable), INITS);
    }

    public QWaitingEntity(Path<? extends WaitingEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWaitingEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWaitingEntity(PathMetadata metadata, PathInits inits) {
        this(WaitingEntity.class, metadata, inits);
    }

    public QWaitingEntity(Class<? extends WaitingEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.example.catch_line.user.member.model.entity.QMemberEntity(forProperty("member")) : null;
        this.restaurant = inits.isInitialized("restaurant") ? new org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

