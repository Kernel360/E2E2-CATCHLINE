package org.example.catch_line.restaurant.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.common.constant.SessionConst;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.service.RestaurantScrapService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantScrapController {

    private final RestaurantScrapService restaurantScrapService;

    @PostMapping("/{restaurantId}/scraps")
    public ResponseEntity<RestaurantResponse> scrapRestaurantByUser(
            @PathVariable Long restaurantId,
            HttpSession httpSession
    ) {
        RestaurantResponse restaurantResponse = restaurantScrapService.saveRestaurantScrap(SessionUtils.getMemberId(httpSession), restaurantId);
        return ResponseEntity.ok().body(restaurantResponse);
    }

    @DeleteMapping("/{restaurantId}/scraps")
    public ResponseEntity<RestaurantResponse> cancelScrapByUser(
            @PathVariable Long restaurantId,
            HttpSession httpSession
    ) {
        RestaurantResponse restaurantResponse = restaurantScrapService.deleteRestaurantScrap(SessionUtils.getMemberId(httpSession), restaurantId);
        return ResponseEntity.ok().body(restaurantResponse);
    }

}
