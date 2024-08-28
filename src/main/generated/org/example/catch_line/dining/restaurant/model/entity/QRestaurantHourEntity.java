package org.example.catch_line.dining.restaurant.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestaurantHourEntity is a Querydsl query type for RestaurantHourEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurantHourEntity extends EntityPathBase<RestaurantHourEntity> {

    private static final long serialVersionUID = -545826810L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestaurantHourEntity restaurantHourEntity = new QRestaurantHourEntity("restaurantHourEntity");

    public final TimePath<java.time.LocalTime> closeTime = createTime("closeTime", java.time.LocalTime.class);

    public final EnumPath<org.example.catch_line.common.constant.DayOfWeeks> dayOfWeek = createEnum("dayOfWeek", org.example.catch_line.common.constant.DayOfWeeks.class);

    public final EnumPath<org.example.catch_line.dining.restaurant.model.entity.constant.OpenStatus> openStatus = createEnum("openStatus", org.example.catch_line.dining.restaurant.model.entity.constant.OpenStatus.class);

    public final TimePath<java.time.LocalTime> openTime = createTime("openTime", java.time.LocalTime.class);

    public final QRestaurantEntity restaurant;

    public final NumberPath<Long> restaurantHourId = createNumber("restaurantHourId", Long.class);

    public QRestaurantHourEntity(String variable) {
        this(RestaurantHourEntity.class, forVariable(variable), INITS);
    }

    public QRestaurantHourEntity(Path<? extends RestaurantHourEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestaurantHourEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestaurantHourEntity(PathMetadata metadata, PathInits inits) {
        this(RestaurantHourEntity.class, metadata, inits);
    }

    public QRestaurantHourEntity(Class<? extends RestaurantHourEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.restaurant = inits.isInitialized("restaurant") ? new QRestaurantEntity(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

