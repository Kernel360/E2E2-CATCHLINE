package org.example.catch_line.restaurant.controller;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.restaurant.model.dto.RestaurantHourResponse;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.service.RestaurantHourService;
import org.example.catch_line.restaurant.service.RestaurantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.util.List;

@Controller
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantHourService restaurantHourService;

    // TODO: 리뷰 개수는 실제 DB 조회해서 count하고, 평점은 DB에서 리뷰 점수 다 가져와서 평균 계산해야함.
    @GetMapping("/{restaurantId}")
    public String viewRestaurant(
            @PathVariable Long restaurantId,
            Model model,
            @RequestParam(defaultValue = "MONDAY") DayOfWeek dayOfWeek
    ) {
        RestaurantResponse restaurant = restaurantService.findRestaurant(restaurantId);
        List<RestaurantHourResponse> restaurantHours = restaurantHourService.getAllRestaurantHours(restaurantId);
        RestaurantHourResponse hourResponse = restaurantHourService.getRestaurantHours(restaurantId, dayOfWeek);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("restaurantHours", restaurantHours);
        model.addAttribute("hourResponse", hourResponse);
        return "restaurant/restaurant";
    }

}
