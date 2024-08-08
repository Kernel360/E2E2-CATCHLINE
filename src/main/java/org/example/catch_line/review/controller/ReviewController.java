package org.example.catch_line.review.controller;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurants/{restaurantId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public String getRestaurantReviews(@PathVariable Long restaurantId, Model model) {
        List<ReviewResponse> reviewList = reviewService.getRestaurantReviewList(restaurantId);
        BigDecimal averageRating = reviewService.getAverageRating(restaurantId);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("reviewList", reviewList);
        return "review/reviews";
    }
}
