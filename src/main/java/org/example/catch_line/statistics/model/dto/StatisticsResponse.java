package org.example.catch_line.statistics.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class StatisticsResponse {

    private Long statisticsId;
    private int waitingCount;
    private int reservationCount;
    private LocalDate statisticsDate;
    private Long restaurantId;
    private String restaurantName;
}
