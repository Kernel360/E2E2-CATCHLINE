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

    @GetMapping
    public String statistics(Model model) {
        List<StatisticsResponse> statisticsList = statisticsService.getStatisticsList();
        model.addAttribute("statisticsList", statisticsList);
        return "statistics/statistics";
    }

    @GetMapping("/{restaurantId}")
    @ResponseBody
    public StatisticsGraphResponse getStatisticsData(@PathVariable Long restaurantId) {
        return statisticsService.getStatisticsByRestaurantId(restaurantId);
    }
}
