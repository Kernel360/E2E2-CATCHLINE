package org.example.catch_line.review.controller;
import java.math.BigDecimal;
import java.util.List;

import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.review.model.dto.ReviewCreateRequest;
import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.model.dto.ReviewUpdateRequest;
import org.example.catch_line.review.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurants/{restaurantId}/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	@GetMapping
	public String getRestaurantReviews(@PathVariable Long restaurantId, Model model) {
		List<ReviewResponse> reviewList = reviewService.getRestaurantReviewList(restaurantId);
		BigDecimal averageRating = reviewService.getAverageRating(restaurantId).getRating();
		model.addAttribute("averageRating", averageRating);
		model.addAttribute("reviewList", reviewList);
		return "review/reviews";
	}


	@GetMapping("/create")
	public String showReviewForm(@PathVariable Long restaurantId, Model model) {
		model.addAttribute("restaurantId", restaurantId);
		model.addAttribute("reviewCreateRequest", new ReviewCreateRequest());
		return "review/reviewForm";
	}

	@PostMapping("/create")
	public String addReview(
		@PathVariable Long restaurantId,
		@ModelAttribute ReviewCreateRequest reviewCreateRequest,
		Model model,
		HttpSession session
	) {
		Long memberId = SessionUtils.getMemberId(session);
		ReviewResponse reviewResponse = reviewService.createReview(memberId, restaurantId, reviewCreateRequest);
		model.addAttribute("reviewResponse", reviewResponse);
		return "redirect:/restaurants/" + restaurantId + "/reviews";
	}

	@GetMapping("{reviewId}/update")
	public String updateForm(
		@PathVariable Long restaurantId,
		@PathVariable Long reviewId,
		HttpSession session,
		RedirectAttributes redirectAttributes,
		Model model
	) {
		Long memberId = SessionUtils.getMemberId(session);

		if (reviewService.isReviewOwner(reviewId, memberId)) {
			ReviewResponse reviewResponse = reviewService.getReviewById(reviewId);
			model.addAttribute("review", reviewResponse);
			model.addAttribute("restaurantId", restaurantId);
			return "review/reviewUpdateForm";
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "이 리뷰에 대한 권한이 없습니다.");
			return "redirect:/restaurants/" + restaurantId + "/reviews";
		}
	}

	@PostMapping("{reviewId}/update")
	public String updateReview(
		@PathVariable Long restaurantId,
		@PathVariable Long reviewId,
		@ModelAttribute ReviewUpdateRequest reviewUpdateRequest,
		HttpSession session,
		RedirectAttributes redirectAttributes
	) {
		Long memberId = SessionUtils.getMemberId(session);
		if (reviewService.isReviewOwner(reviewId, memberId)) {
			reviewService.updateReview(reviewId, reviewUpdateRequest.getRating(), reviewUpdateRequest.getContent());
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "이 리뷰에 대한 권한이 없습니다.");
		}

		return "redirect:/restaurants/" + restaurantId + "/reviews";
	}

	@DeleteMapping("{reviewId}")
	public String deleteReview(
		@PathVariable Long restaurantId,
		@PathVariable Long reviewId,
		HttpSession session,
		RedirectAttributes redirectAttributes
	) {
		Long memberId = SessionUtils.getMemberId(session);
		if (reviewService.isReviewOwner(reviewId, memberId)) {
			reviewService.deleteReview(reviewId);
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "이 리뷰에 대한 권한이 없습니다.");
		}

		return "redirect:/restaurants/" + restaurantId + "/reviews";
	}
}
