package org.example.catch_line.restaurant.controller;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.restaurant.service.RestaurantPreviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@Controller
@RequiredArgsConstructor
public class RestaurantPreviewController {

    private final RestaurantPreviewService restaurantPreviewService;

    @GetMapping("/restaurants")
    public String getRestaurantPreviewList(@PageableDefault(page=0, size = 2)Pageable pageable,Model model) {

        Page<RestaurantPreviewResponse> restaurantPreviewPage = restaurantPreviewService.restaurantPreviewPaging(pageable);

        int blockLimit = 5;
        int startPage = (((int) Math.ceil(((double) (pageable.getPageNumber() +1) / blockLimit))) -1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), restaurantPreviewPage.getTotalPages());


        model.addAttribute("restaurantPreviewPage",restaurantPreviewPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        // List<RestaurantPreviewResponse> restaurantPreviewList = restaurantPreviewService.getRestaurantPreviewList();
        // model.addAttribute("restaurantPreviewList", restaurantPreviewList);

        return "restaurant/restaurantPreview";
    }
}
