package org.example.catch_line.user.owner.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOwnerEntity is a Querydsl query type for OwnerEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOwnerEntity extends EntityPathBase<OwnerEntity> {

    private static final long serialVersionUID = 1223605862L;

    public static final QOwnerEntity ownerEntity = new QOwnerEntity("ownerEntity");

    public final StringPath loginId = createString("loginId");

    public final StringPath name = createString("name");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final SimplePath<org.example.catch_line.common.model.vo.Password> password = createSimple("password", org.example.catch_line.common.model.vo.Password.class);

    public final SimplePath<org.example.catch_line.common.model.vo.PhoneNumber> phoneNumber = createSimple("phoneNumber", org.example.catch_line.common.model.vo.PhoneNumber.class);

    public final ListPath<org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity, org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity> restaurants = this.<org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity, org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity>createList("restaurants", org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity.class, org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity.class, PathInits.DIRECT2);

    public QOwnerEntity(String variable) {
        super(OwnerEntity.class, forVariable(variable));
    }

    public QOwnerEntity(Path<? extends OwnerEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOwnerEntity(PathMetadata metadata) {
        super(OwnerEntity.class, metadata);
    }

}

