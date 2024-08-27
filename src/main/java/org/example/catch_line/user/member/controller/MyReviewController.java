package org.example.catch_line.user.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.user.auth.details.MemberUserDetails;
import org.example.catch_line.exception.authorizaion.UnauthorizedException;
import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.model.dto.ReviewUpdateRequest;
import org.example.catch_line.review.service.ReviewService;
import org.example.catch_line.user.member.model.dto.MyReviewResponse;
import org.example.catch_line.user.member.service.MyReviewService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/my-page/reviews")
public class MyReviewController {

    private final MyReviewService myReviewService;
    private final ReviewService reviewService;

    @GetMapping
    public String myReviewList(
            Model model,
            @AuthenticationPrincipal MemberUserDetails memberUserDetails
            ) {
        Long memberId = memberUserDetails.getMember().getMemberId();
        List<MyReviewResponse> reviewList = myReviewService.getMyReviewList(memberId);

        model.addAttribute("reviewList", reviewList);
        model.addAttribute("memberId", memberId);
        return "member/my-page/reviews";
    }

    @GetMapping("/{reviewId}/update")
    public String updateForm(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal MemberUserDetails memberUserDetails,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        Long memberId;
        try {
            memberId = memberUserDetails.getMember().getMemberId();
            ReviewResponse reviewResponse = reviewService.getReviewById(reviewId, memberId);
            model.addAttribute("review", reviewResponse);
            model.addAttribute("reviewId", reviewId);
        } catch (NullPointerException | UnauthorizedException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/my-page/reviews";
        }

        return "member/my-page/reviewUpdateForm";
    }

    @PutMapping("/{reviewId}")
    public String updateReview(
            @PathVariable Long reviewId,
            @Valid @ModelAttribute("review") ReviewUpdateRequest reviewUpdateRequest,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal MemberUserDetails memberUserDetails,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("reviewId", reviewId);
            return "member/my-page/reviewUpdateForm";
        }

        Long memberId;
        try {
            memberId = memberUserDetails.getMember().getMemberId();
            reviewService.updateReview(reviewId, memberId, reviewUpdateRequest.getRating(), reviewUpdateRequest.getContent());
        } catch (NullPointerException | UnauthorizedException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/my-page/reviews";
    }

    @DeleteMapping("/{reviewId}")
    public String deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal MemberUserDetails memberUserDetails,
            RedirectAttributes redirectAttributes
    ) {
        Long memberId;
        try {
            memberId = memberUserDetails.getMember().getMemberId();
            reviewService.deleteReview(reviewId, memberId);
        } catch (NullPointerException | UnauthorizedException e) {
            log.info("exception");
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/my-page/reviews";
    }
}
