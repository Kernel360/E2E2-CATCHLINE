package org.example.catch_line.statistics.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.catch_line.common.constant.ServiceType;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class StatisticsGraphResponse {

    private List<Integer> waitingCounts;
    private List<Integer> reservationCounts;
    private List<String> statisticsDate;
    private String restaurantName;
    private String serviceType;
}
