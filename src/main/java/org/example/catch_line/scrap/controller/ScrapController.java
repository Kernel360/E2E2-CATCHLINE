package org.example.catch_line.scrap.controller;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.config.auth.MemberUserDetails;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.scrap.service.ScrapService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/restaurants/{restaurantId}/scraps")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping
    public ResponseEntity<?> scrapRestaurantByUser(@PathVariable Long restaurantId, @AuthenticationPrincipal MemberUserDetails memberUserDetails) {

        RestaurantResponse restaurantResponse = scrapService.createScrap(memberUserDetails.getMember().getMemberId(), restaurantId);
        return ResponseEntity.ok().body(restaurantResponse);
    }

    @DeleteMapping
    public ResponseEntity<RestaurantResponse> cancelScrapByUser(@PathVariable Long restaurantId, @AuthenticationPrincipal MemberUserDetails memberUserDetails) {

        RestaurantResponse restaurantResponse = scrapService.deleteScrap(memberUserDetails.getMember().getMemberId(), restaurantId);
        return ResponseEntity.ok().body(restaurantResponse);
    }

}
