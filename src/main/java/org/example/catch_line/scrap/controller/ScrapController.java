package org.example.catch_line.scrap.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.common.session.SessionUtils;
import org.example.catch_line.exception.session.InvalidSessionException;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.scrap.service.ScrapService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/restaurants/{restaurantId}/scraps")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping
    public ResponseEntity<?> scrapRestaurantByUser(
            @PathVariable Long restaurantId,
            HttpSession httpSession
    ) {
        Long memberId;
        try {
            memberId = SessionUtils.getMemberId(httpSession);
        } catch (InvalidSessionException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        RestaurantResponse restaurantResponse = scrapService.createScrap(memberId, restaurantId);

        return ResponseEntity.ok().body(restaurantResponse);
    }

    @DeleteMapping
    public ResponseEntity<RestaurantResponse> cancelScrapByUser(
            @PathVariable Long restaurantId,
            HttpSession httpSession
    ) {
        RestaurantResponse restaurantResponse = scrapService.deleteScrap(SessionUtils.getMemberId(httpSession), restaurantId);
        return ResponseEntity.ok().body(restaurantResponse);
    }

}
