package org.example.catch_line.statistics.model.mapper;

import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.statistics.model.dto.StatisticsGraphResponse;
import org.example.catch_line.statistics.model.dto.StatisticsResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatisticsMapper {

    public StatisticsResponse entityToResponse(RestaurantEntity restaurantEntity) {
        return StatisticsResponse.builder()
                .restaurantId(restaurantEntity.getRestaurantId())
                .restaurantName(restaurantEntity.getName())
                .build();
    }

    public StatisticsGraphResponse entityToGraphResponse(List<String> dates, List<Integer> reservationCounts, List<Integer> waitingCounts, String restaurantName, String serviceType) {
        return StatisticsGraphResponse.builder()
                .waitingCounts(waitingCounts)
                .reservationCounts(reservationCounts)
                .statisticsDate(dates)
                .restaurantName(restaurantName)
                .serviceType(serviceType)
                .build();
    }
}
