package org.example.catch_line.restaurant.controller;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.restaurant.service.RestaurantPreviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RestaurantPreviewController {

    private final RestaurantPreviewService restaurantPreviewService;

    @GetMapping("/restaurants")
    public String getRestaurantPreviewList(Model model) {
        List<RestaurantPreviewResponse> restaurantPreviewList = restaurantPreviewService.getRestaurantPreviewList();
        model.addAttribute("restaurantPreviewList", restaurantPreviewList);

        return "restaurant/restaurantPreview";
    }
}
