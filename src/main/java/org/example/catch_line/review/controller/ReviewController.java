package org.example.catch_line.review.controller;
import java.math.BigDecimal;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.session.SessionConst;
import org.example.catch_line.common.session.SessionUtils;
import org.example.catch_line.exception.authorizaion.UnauthorizedException;
import org.example.catch_line.exception.session.InvalidSessionException;
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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurants/{restaurantId}/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	@GetMapping
	public String getRestaurantReviews(
			@PathVariable Long restaurantId,
			Model model,
			HttpSession session
	) {
		Long memberId = (Long) session.getAttribute(SessionConst.MEMBER_ID);
		List<ReviewResponse> reviewList = reviewService.getRestaurantReviewList(restaurantId);
		BigDecimal averageRating = reviewService.getAverageRating(restaurantId).getRating();
		model.addAttribute("averageRating", averageRating);
		model.addAttribute("reviewList", reviewList);
		model.addAttribute("memberId", memberId);

		return "review/reviews";
	}


	@GetMapping("/create")
	public String showReviewForm(
			@PathVariable Long restaurantId,
			HttpSession session,
			RedirectAttributes redirectAttributes,
			Model model
	) {
		try {
			SessionUtils.getMemberId(session);
			model.addAttribute("restaurantId", restaurantId);
			model.addAttribute("reviewCreateRequest", new ReviewCreateRequest());
		} catch (InvalidSessionException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return String.format("redirect:/restaurants/%d/reviews", restaurantId);
		}

		return "review/reviewForm";
	}

	@PostMapping("/create")
	public String addReview(
		@PathVariable Long restaurantId,
		@ModelAttribute ReviewCreateRequest reviewCreateRequest,
		Model model,
		RedirectAttributes redirectAttributes,
		HttpSession session
	) {
		Long memberId;
		try {
			memberId = SessionUtils.getMemberId(session);
			ReviewResponse reviewResponse = reviewService.createReview(memberId, restaurantId, reviewCreateRequest);
			model.addAttribute("reviewResponse", reviewResponse);
		} catch (InvalidSessionException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		return String.format("redirect:/restaurants/%d/reviews", restaurantId);
	}

	@GetMapping("/{reviewId}/update")
	public String updateForm(
		@PathVariable Long restaurantId,
		@PathVariable Long reviewId,
		HttpSession session,
		RedirectAttributes redirectAttributes,
		Model model
	) {
		Long memberId;
		try {
			memberId = SessionUtils.getMemberId(session);
			ReviewResponse reviewResponse = reviewService.getReviewById(reviewId, memberId);
			model.addAttribute("review", reviewResponse);
			model.addAttribute("restaurantId", restaurantId);
		} catch (InvalidSessionException | UnauthorizedException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return String.format("redirect:/restaurants/%d/reviews", restaurantId);
		}

		return "review/reviewUpdateForm";
	}

	@PostMapping("/{reviewId}/update")
	public String updateReview(
		@PathVariable Long restaurantId,
		@PathVariable Long reviewId,
		@ModelAttribute ReviewUpdateRequest reviewUpdateRequest,
		HttpSession session,
		RedirectAttributes redirectAttributes
	) {
		Long memberId;
		try {
			memberId = SessionUtils.getMemberId(session);
			reviewService.updateReview(reviewId, memberId, reviewUpdateRequest.getRating(), reviewUpdateRequest.getContent());
		} catch (InvalidSessionException | UnauthorizedException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		return String.format("redirect:/restaurants/%d/reviews", restaurantId);
	}

	@DeleteMapping("/{reviewId}")
	public String deleteReview(
		@PathVariable Long restaurantId,
		@PathVariable Long reviewId,
		HttpSession session,
		RedirectAttributes redirectAttributes
	) {
		Long memberId;
		try {
			memberId = SessionUtils.getMemberId(session);
			reviewService.deleteReview(reviewId, memberId);
		} catch (InvalidSessionException | UnauthorizedException e) {
			log.info("exception");
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

        return String.format("redirect:/restaurants/%d/reviews", restaurantId);
	}
}
