package org.example.catch_line.scrap.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScrapEntity is a Querydsl query type for ScrapEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScrapEntity extends EntityPathBase<ScrapEntity> {

    private static final long serialVersionUID = -2097776269L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScrapEntity scrapEntity = new QScrapEntity("scrapEntity");

    public final org.example.catch_line.common.model.entity.QBaseTimeEntity _super = new org.example.catch_line.common.model.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final org.example.catch_line.user.member.model.entity.QMemberEntity member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity restaurant;

    public final org.example.catch_line.scrap.model.key.QScrapId scrapId;

    public QScrapEntity(String variable) {
        this(ScrapEntity.class, forVariable(variable), INITS);
    }

    public QScrapEntity(Path<? extends ScrapEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScrapEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScrapEntity(PathMetadata metadata, PathInits inits) {
        this(ScrapEntity.class, metadata, inits);
    }

    public QScrapEntity(Class<? extends ScrapEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.example.catch_line.user.member.model.entity.QMemberEntity(forProperty("member")) : null;
        this.restaurant = inits.isInitialized("restaurant") ? new org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity(forProperty("restaurant"), inits.get("restaurant")) : null;
        this.scrapId = inits.isInitialized("scrapId") ? new org.example.catch_line.scrap.model.key.QScrapId(forProperty("scrapId")) : null;
    }

}

