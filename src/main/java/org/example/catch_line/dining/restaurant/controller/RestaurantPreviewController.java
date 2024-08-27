package org.example.catch_line.dining.restaurant.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.cookie.CookieUtils;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.dining.restaurant.service.RestaurantPreviewService;
import org.example.catch_line.exception.CatchLineException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RestaurantPreviewController {

    private final RestaurantPreviewService restaurantPreviewService;
    private final CookieUtils cookieUtils;

    @GetMapping("/restaurants")
    public String getRestaurantPreviewList(
            @RequestParam(required = false, defaultValue = "reviewCount") String criteria,
            @PageableDefault(page=0, size = 2) Pageable pageable,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            Model model,
            HttpServletRequest request
            ) {

        boolean isLoggedIn;
        try {
            cookieUtils.validateUserByToken(request);
            isLoggedIn = true;
        } catch (CatchLineException e) {
            isLoggedIn = false;
        }
        model.addAttribute("isLoggedIn", isLoggedIn);
      
        Page<RestaurantPreviewResponse> restaurantPreviewPage = restaurantPreviewService.restaurantPreviewPaging(pageable, criteria);

        if (Objects.nonNull(type) && Objects.nonNull(keyword)) {
            restaurantPreviewPage = restaurantPreviewService.restaurantPreviewSearchAndPaging(pageable, criteria, type, keyword);
        }

        int blockLimit = 5;
        int startPage = (((int) Math.ceil(((double) (pageable.getPageNumber() + 1) / blockLimit))) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), restaurantPreviewPage.getTotalPages());


        model.addAttribute("restaurantPreviewPage", restaurantPreviewPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("criteria", criteria);
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("restaurantPreviewPage",restaurantPreviewPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("criteria",criteria);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "restaurant/restaurantPreview";
    }
}
