package org.example.catch_line.dining.menu.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMenuEntity is a Querydsl query type for MenuEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMenuEntity extends EntityPathBase<MenuEntity> {

    private static final long serialVersionUID = -1645267834L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMenuEntity menuEntity = new QMenuEntity("menuEntity");

    public final NumberPath<Long> menuId = createNumber("menuId", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity restaurant;

    public QMenuEntity(String variable) {
        this(MenuEntity.class, forVariable(variable), INITS);
    }

    public QMenuEntity(Path<? extends MenuEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMenuEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMenuEntity(PathMetadata metadata, PathInits inits) {
        this(MenuEntity.class, metadata, inits);
    }

    public QMenuEntity(Class<? extends MenuEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.restaurant = inits.isInitialized("restaurant") ? new org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

