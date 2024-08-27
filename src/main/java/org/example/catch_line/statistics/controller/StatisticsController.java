package org.example.catch_line.statistics.controller;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.repository.RestaurantRepository;
import org.example.catch_line.dining.restaurant.service.RestaurantService;
import org.example.catch_line.statistics.model.dto.StatisticsGraphResponse;
import org.example.catch_line.statistics.model.dto.StatisticsResponse;
import org.example.catch_line.statistics.service.StatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/owner/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final RestaurantRepository restaurantRepository;

    @GetMapping
    public String statistics(Model model) {
        List<StatisticsResponse> statisticsList = statisticsService.getStatisticsList();

        // 식당 ID를 기준으로 고유한 식당 리스트 추출
        Set<Long> uniqueRestaurantIds = statisticsList.stream()
                .map(StatisticsResponse::getRestaurantId)
                .collect(Collectors.toSet());

        // 고유한 식당 ID로 식당 정보를 가져옴
        List<RestaurantEntity> uniqueRestaurants = uniqueRestaurantIds.stream()
                .map(restaurantId -> restaurantRepository.findById(restaurantId).orElse(null))
                .toList();

        model.addAttribute("statisticsList", statisticsList);
        model.addAttribute("restaurantList", uniqueRestaurants);
        return "statistics/statistics";
    }

    @GetMapping("/{restaurantId}")
    @ResponseBody
    public StatisticsGraphResponse getStatisticsData(@PathVariable Long restaurantId) {
        return statisticsService.getStatisticsByRestaurantId(restaurantId);
    }
}
