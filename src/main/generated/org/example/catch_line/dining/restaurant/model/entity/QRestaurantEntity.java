package org.example.catch_line.dining.restaurant.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestaurantEntity is a Querydsl query type for RestaurantEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurantEntity extends EntityPathBase<RestaurantEntity> {

    private static final long serialVersionUID = 1954142338L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestaurantEntity restaurantEntity = new QRestaurantEntity("restaurantEntity");

    public final org.example.catch_line.common.model.entity.QBaseTimeEntity _super = new org.example.catch_line.common.model.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final EnumPath<org.example.catch_line.dining.restaurant.model.entity.constant.FoodType> foodType = createEnum("foodType", org.example.catch_line.dining.restaurant.model.entity.constant.FoodType.class);

    public final NumberPath<java.math.BigDecimal> latitude = createNumber("latitude", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> longitude = createNumber("longitude", java.math.BigDecimal.class);

    public final ListPath<org.example.catch_line.dining.menu.model.entity.MenuEntity, org.example.catch_line.dining.menu.model.entity.QMenuEntity> menus = this.<org.example.catch_line.dining.menu.model.entity.MenuEntity, org.example.catch_line.dining.menu.model.entity.QMenuEntity>createList("menus", org.example.catch_line.dining.menu.model.entity.MenuEntity.class, org.example.catch_line.dining.menu.model.entity.QMenuEntity.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final org.example.catch_line.user.owner.model.entity.QOwnerEntity owner;

    public final SimplePath<org.example.catch_line.common.model.vo.PhoneNumber> phoneNumber = createSimple("phoneNumber", org.example.catch_line.common.model.vo.PhoneNumber.class);

    public final SimplePath<org.example.catch_line.common.model.vo.Rating> rating = createSimple("rating", org.example.catch_line.common.model.vo.Rating.class);

    public final ListPath<org.example.catch_line.booking.reservation.model.entity.ReservationEntity, org.example.catch_line.booking.reservation.model.entity.QReservationEntity> reservations = this.<org.example.catch_line.booking.reservation.model.entity.ReservationEntity, org.example.catch_line.booking.reservation.model.entity.QReservationEntity>createList("reservations", org.example.catch_line.booking.reservation.model.entity.ReservationEntity.class, org.example.catch_line.booking.reservation.model.entity.QReservationEntity.class, PathInits.DIRECT2);

    public final ListPath<RestaurantHourEntity, QRestaurantHourEntity> restaurantHours = this.<RestaurantHourEntity, QRestaurantHourEntity>createList("restaurantHours", RestaurantHourEntity.class, QRestaurantHourEntity.class, PathInits.DIRECT2);

    public final NumberPath<Long> restaurantId = createNumber("restaurantId", Long.class);

    public final ListPath<RestaurantImageEntity, QRestaurantImageEntity> restaurantImages = this.<RestaurantImageEntity, QRestaurantImageEntity>createList("restaurantImages", RestaurantImageEntity.class, QRestaurantImageEntity.class, PathInits.DIRECT2);

    public final NumberPath<Long> reviewCount = createNumber("reviewCount", Long.class);

    public final ListPath<org.example.catch_line.review.model.entity.ReviewEntity, org.example.catch_line.review.model.entity.QReviewEntity> reviews = this.<org.example.catch_line.review.model.entity.ReviewEntity, org.example.catch_line.review.model.entity.QReviewEntity>createList("reviews", org.example.catch_line.review.model.entity.ReviewEntity.class, org.example.catch_line.review.model.entity.QReviewEntity.class, PathInits.DIRECT2);

    public final NumberPath<Long> scrapCount = createNumber("scrapCount", Long.class);

    public final ListPath<org.example.catch_line.scrap.model.entity.ScrapEntity, org.example.catch_line.scrap.model.entity.QScrapEntity> scraps = this.<org.example.catch_line.scrap.model.entity.ScrapEntity, org.example.catch_line.scrap.model.entity.QScrapEntity>createList("scraps", org.example.catch_line.scrap.model.entity.ScrapEntity.class, org.example.catch_line.scrap.model.entity.QScrapEntity.class, PathInits.DIRECT2);

    public final EnumPath<org.example.catch_line.common.constant.ServiceType> serviceType = createEnum("serviceType", org.example.catch_line.common.constant.ServiceType.class);

    public final ListPath<org.example.catch_line.booking.waiting.model.entity.WaitingEntity, org.example.catch_line.booking.waiting.model.entity.QWaitingEntity> waitings = this.<org.example.catch_line.booking.waiting.model.entity.WaitingEntity, org.example.catch_line.booking.waiting.model.entity.QWaitingEntity>createList("waitings", org.example.catch_line.booking.waiting.model.entity.WaitingEntity.class, org.example.catch_line.booking.waiting.model.entity.QWaitingEntity.class, PathInits.DIRECT2);

    public QRestaurantEntity(String variable) {
        this(RestaurantEntity.class, forVariable(variable), INITS);
    }

    public QRestaurantEntity(Path<? extends RestaurantEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestaurantEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestaurantEntity(PathMetadata metadata, PathInits inits) {
        this(RestaurantEntity.class, metadata, inits);
    }

    public QRestaurantEntity(Class<? extends RestaurantEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new org.example.catch_line.user.owner.model.entity.QOwnerEntity(forProperty("owner")) : null;
    }

}

