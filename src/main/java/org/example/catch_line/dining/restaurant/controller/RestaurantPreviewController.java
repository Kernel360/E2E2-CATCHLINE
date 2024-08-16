package org.example.catch_line.dining.restaurant.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.common.session.SessionConst;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.dining.restaurant.service.RestaurantPreviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class RestaurantPreviewController {

    private final RestaurantPreviewService restaurantPreviewService;

    @GetMapping("/restaurants")
    public String getRestaurantPreviewList(
            @RequestParam(required = false, defaultValue = "reviewCount") String criteria,
            @PageableDefault(page=0, size = 2)Pageable pageable,
            Model model,
            HttpSession httpSession
    ) {

        boolean isLoggedIn = httpSession.getAttribute(SessionConst.MEMBER_ID) != null; // "user" 세션 속성으로 로그인 상태 확인
        model.addAttribute("isLoggedIn", isLoggedIn);

        Page<RestaurantPreviewResponse> restaurantPreviewPage = restaurantPreviewService.restaurantPreviewPaging(pageable, criteria);

        int blockLimit = 5;
        int startPage = (((int) Math.ceil(((double) (pageable.getPageNumber() +1) / blockLimit))) -1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), restaurantPreviewPage.getTotalPages());


        model.addAttribute("restaurantPreviewPage",restaurantPreviewPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("criteria",criteria);

        // List<RestaurantPreviewResponse> restaurantPreviewList = restaurantPreviewService.getRestaurantPreviewList();
        // model.addAttribute("restaurantPreviewList", restaurantPreviewList);

        return "restaurant/restaurantPreview";
    }
}
