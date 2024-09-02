package org.example.catch_line.dining.restaurant.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.dining.restaurant.model.entity.QRestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.constant.FoodType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class RestaurantRepositoryCustomImpl implements RestaurantRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // querydsl gradle 후 Q 클래스들이 제가 사용하려는 클래스들만이 아니라 모든 클래스들에 대한 Q 클래스가 생성되는데 다른 방법은 없는 것인지 궁금합니다!
    @Override
    public Page<RestaurantEntity> findRestaurantsByCriteria(Pageable pageable, String criteria, String type, String keyword) {
        QRestaurantEntity restaurant = QRestaurantEntity.restaurantEntity; // Q 클래스 사용

        BooleanBuilder builder = new BooleanBuilder();

        if ("name".equalsIgnoreCase(type) && keyword != null) {
            builder.and(restaurant.name.containsIgnoreCase(keyword));
        } else if ("foodType".equalsIgnoreCase(type) && keyword != null) {
            builder.and(restaurant.foodType.eq(FoodType.fromKoreanName(keyword)));
        }

        QueryResults<RestaurantEntity> queryResults;

        if ("scrapCount".equals(criteria)) {
            queryResults = queryFactory
                    .selectFrom(restaurant)
                    .where(builder)
                    .orderBy(restaurant.scrapCount.desc(), restaurant.restaurantId.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        } else if ("rating".equals(criteria)) {
            queryResults = queryFactory
                    .selectFrom(restaurant)
                    .where(builder)
                    .orderBy(Expressions.numberTemplate(BigDecimal.class, "{0}", restaurant.rating).desc(), restaurant.restaurantId.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        } else {
            queryResults = queryFactory
                    .selectFrom(restaurant)
                    .where(builder)
                    .orderBy(restaurant.reviewCount.desc(), restaurant.restaurantId.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }

        List<RestaurantEntity> content = queryResults.getResults();
        long total = queryResults.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
}
