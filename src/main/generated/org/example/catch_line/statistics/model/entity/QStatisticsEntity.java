package org.example.catch_line.statistics.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStatisticsEntity is a Querydsl query type for StatisticsEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStatisticsEntity extends EntityPathBase<StatisticsEntity> {

    private static final long serialVersionUID = -680289783L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStatisticsEntity statisticsEntity = new QStatisticsEntity("statisticsEntity");

    public final NumberPath<Integer> reservationCount = createNumber("reservationCount", Integer.class);

    public final org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity restaurant;

    public final DatePath<java.time.LocalDate> statisticsDate = createDate("statisticsDate", java.time.LocalDate.class);

    public final NumberPath<Long> statisticsId = createNumber("statisticsId", Long.class);

    public final NumberPath<Integer> waitingCount = createNumber("waitingCount", Integer.class);

    public QStatisticsEntity(String variable) {
        this(StatisticsEntity.class, forVariable(variable), INITS);
    }

    public QStatisticsEntity(Path<? extends StatisticsEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStatisticsEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStatisticsEntity(PathMetadata metadata, PathInits inits) {
        this(StatisticsEntity.class, metadata, inits);
    }

    public QStatisticsEntity(Class<? extends StatisticsEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.restaurant = inits.isInitialized("restaurant") ? new org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

