package org.example.catch_line.scrap.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.scrap.service.ScrapService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping("/{restaurantId}/scraps")
    public ResponseEntity<RestaurantResponse> scrapRestaurantByUser(
            @PathVariable Long restaurantId,
            HttpSession httpSession
    ) {
        RestaurantResponse restaurantResponse = scrapService.saveScrap(SessionUtils.getMemberId(httpSession), restaurantId);
        return ResponseEntity.ok().body(restaurantResponse);
    }

    @DeleteMapping("/{restaurantId}/scraps")
    public ResponseEntity<RestaurantResponse> cancelScrapByUser(
            @PathVariable Long restaurantId,
            HttpSession httpSession
    ) {
        RestaurantResponse restaurantResponse = scrapService.deleteScrap(SessionUtils.getMemberId(httpSession), restaurantId);
        return ResponseEntity.ok().body(restaurantResponse);
    }

}
