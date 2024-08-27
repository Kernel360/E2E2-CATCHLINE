package org.example.catch_line.user.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.user.auth.details.MemberUserDetails;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.user.member.service.MyScrapService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal MemberUserDetails memberUserDetails
            ) {
        Long memberId = memberUserDetails.getMember().getMemberId();
        List<RestaurantPreviewResponse> myRestaurants = myScrapService.findMyRestaurants(memberId);
        model.addAttribute("myRestaurants", myRestaurants);
        return "member/my-page/scraps";
    }


}
