package org.example.catch_line.user.member.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberEntity is a Querydsl query type for MemberEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberEntity extends EntityPathBase<MemberEntity> {

    private static final long serialVersionUID = 1685934570L;

    public static final QMemberEntity memberEntity = new QMemberEntity("memberEntity");

    public final org.example.catch_line.common.model.entity.QBaseTimeEntity _super = new org.example.catch_line.common.model.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final SimplePath<org.example.catch_line.common.model.vo.Email> email = createSimple("email", org.example.catch_line.common.model.vo.Email.class);

    public final BooleanPath isMemberDeleted = createBoolean("isMemberDeleted");

    public final StringPath kakaoMemberId = createString("kakaoMemberId");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final SimplePath<org.example.catch_line.common.model.vo.Password> password = createSimple("password", org.example.catch_line.common.model.vo.Password.class);

    public final SimplePath<org.example.catch_line.common.model.vo.PhoneNumber> phoneNumber = createSimple("phoneNumber", org.example.catch_line.common.model.vo.PhoneNumber.class);

    public final ListPath<org.example.catch_line.booking.reservation.model.entity.ReservationEntity, org.example.catch_line.booking.reservation.model.entity.QReservationEntity> reservations = this.<org.example.catch_line.booking.reservation.model.entity.ReservationEntity, org.example.catch_line.booking.reservation.model.entity.QReservationEntity>createList("reservations", org.example.catch_line.booking.reservation.model.entity.ReservationEntity.class, org.example.catch_line.booking.reservation.model.entity.QReservationEntity.class, PathInits.DIRECT2);

    public final ListPath<org.example.catch_line.review.model.entity.ReviewEntity, org.example.catch_line.review.model.entity.QReviewEntity> reviews = this.<org.example.catch_line.review.model.entity.ReviewEntity, org.example.catch_line.review.model.entity.QReviewEntity>createList("reviews", org.example.catch_line.review.model.entity.ReviewEntity.class, org.example.catch_line.review.model.entity.QReviewEntity.class, PathInits.DIRECT2);

    public final ListPath<org.example.catch_line.scrap.model.entity.ScrapEntity, org.example.catch_line.scrap.model.entity.QScrapEntity> scraps = this.<org.example.catch_line.scrap.model.entity.ScrapEntity, org.example.catch_line.scrap.model.entity.QScrapEntity>createList("scraps", org.example.catch_line.scrap.model.entity.ScrapEntity.class, org.example.catch_line.scrap.model.entity.QScrapEntity.class, PathInits.DIRECT2);

    public final ListPath<org.example.catch_line.booking.waiting.model.entity.WaitingEntity, org.example.catch_line.booking.waiting.model.entity.QWaitingEntity> waitings = this.<org.example.catch_line.booking.waiting.model.entity.WaitingEntity, org.example.catch_line.booking.waiting.model.entity.QWaitingEntity>createList("waitings", org.example.catch_line.booking.waiting.model.entity.WaitingEntity.class, org.example.catch_line.booking.waiting.model.entity.QWaitingEntity.class, PathInits.DIRECT2);

    public QMemberEntity(String variable) {
        super(MemberEntity.class, forVariable(variable));
    }

    public QMemberEntity(Path<? extends MemberEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberEntity(PathMetadata metadata) {
        super(MemberEntity.class, metadata);
    }

}

