package org.example.catch_line.statistics.model.mapper;

import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.statistics.model.dto.StatisticsGraphResponse;
import org.example.catch_line.statistics.model.dto.StatisticsResponse;
import org.example.catch_line.statistics.model.entity.StatisticsEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatisticsMapper {

    public StatisticsResponse entityToResponse(StatisticsEntity statisticsEntity) {
        return StatisticsResponse.builder()
                .statisticsId(statisticsEntity.getStatisticsId())
                .waitingCount(statisticsEntity.getWaitingCount())
                .reservationCount(statisticsEntity.getReservationCount())
                .restaurantId(statisticsEntity.getRestaurant().getRestaurantId())
                .restaurantName(statisticsEntity.getRestaurant().getName())
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
