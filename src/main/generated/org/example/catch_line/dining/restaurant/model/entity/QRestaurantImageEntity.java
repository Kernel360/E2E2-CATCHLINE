package org.example.catch_line.dining.restaurant.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestaurantImageEntity is a Querydsl query type for RestaurantImageEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurantImageEntity extends EntityPathBase<RestaurantImageEntity> {

    private static final long serialVersionUID = 104914911L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestaurantImageEntity restaurantImageEntity = new QRestaurantImageEntity("restaurantImageEntity");

    public final StringPath fileName = createString("fileName");

    public final StringPath fileType = createString("fileType");

    public final ArrayPath<byte[], Byte> imageBinaryData = createArray("imageBinaryData", byte[].class);

    public final QRestaurantEntity restaurant;

    public final NumberPath<Long> RestaurantImageId = createNumber("RestaurantImageId", Long.class);

    public QRestaurantImageEntity(String variable) {
        this(RestaurantImageEntity.class, forVariable(variable), INITS);
    }

    public QRestaurantImageEntity(Path<? extends RestaurantImageEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestaurantImageEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestaurantImageEntity(PathMetadata metadata, PathInits inits) {
        this(RestaurantImageEntity.class, metadata, inits);
    }

    public QRestaurantImageEntity(Class<? extends RestaurantImageEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.restaurant = inits.isInitialized("restaurant") ? new QRestaurantEntity(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

