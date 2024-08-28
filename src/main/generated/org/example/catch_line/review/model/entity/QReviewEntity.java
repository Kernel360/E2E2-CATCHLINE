package org.example.catch_line.review.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewEntity is a Querydsl query type for ReviewEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewEntity extends EntityPathBase<ReviewEntity> {

    private static final long serialVersionUID = -1992032269L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewEntity reviewEntity = new QReviewEntity("reviewEntity");

    public final org.example.catch_line.common.model.entity.QBaseTimeEntity _super = new org.example.catch_line.common.model.entity.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final org.example.catch_line.user.member.model.entity.QMemberEntity member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Integer> rating = createNumber("rating", Integer.class);

    public final org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity restaurant;

    public final NumberPath<Long> reviewId = createNumber("reviewId", Long.class);

    public QReviewEntity(String variable) {
        this(ReviewEntity.class, forVariable(variable), INITS);
    }

    public QReviewEntity(Path<? extends ReviewEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewEntity(PathMetadata metadata, PathInits inits) {
        this(ReviewEntity.class, metadata, inits);
    }

    public QReviewEntity(Class<? extends ReviewEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.example.catch_line.user.member.model.entity.QMemberEntity(forProperty("member")) : null;
        this.restaurant = inits.isInitialized("restaurant") ? new org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

