package org.example.catch_line.user.member.controller.thymeleaf;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.common.constant.SessionConst;
import org.example.catch_line.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.user.member.service.MyScrapService;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/my-page/scraps")
public class MyScrapController {

    private final MyScrapService myScrapService;

    @GetMapping
    public String findMyRestaurantsByScrap(
            Model model,
            HttpSession httpSession
    ) {
        Long memberId = SessionUtils.getMemberId(httpSession);

        List<RestaurantPreviewResponse> myRestaurants = myScrapService.findMyRestaurants(memberId);
        model.addAttribute("myRestaurants", myRestaurants);
        return "member/my-page/scraps";
    }


}
