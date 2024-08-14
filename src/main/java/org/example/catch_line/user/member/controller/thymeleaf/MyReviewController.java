package org.example.catch_line.user.member.controller.thymeleaf;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.user.member.service.MyReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/my-page/reviews")
public class MyReviewController {

    private final MyReviewService myReviewService;

    @GetMapping
    public String myReviewList(
            Model model,
            HttpSession httpSession
    ) {
        Long memberId = SessionUtils.getMemberId(httpSession);
        List<ReviewResponse> reviewList = myReviewService.getMyReviewList(memberId);

        model.addAttribute("reviewList", reviewList);
        return "member/my-page/reviews";
    }

}
