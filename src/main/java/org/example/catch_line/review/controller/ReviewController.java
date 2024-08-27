package org.example.catch_line.review.controller;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.user.auth.details.MemberUserDetails;
import org.example.catch_line.exception.authorizaion.UnauthorizedException;
import org.example.catch_line.review.model.dto.ReviewCreateRequest;
import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.model.dto.ReviewUpdateRequest;
import org.example.catch_line.review.service.ReviewService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurants/{restaurantId}/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	@GetMapping
	public String getRestaurantReviews(@PathVariable Long restaurantId, Model model, @AuthenticationPrincipal MemberUserDetails memberUserDetails) {
		Long memberId;
		if(Objects.isNull(memberUserDetails)) {
			 memberId= null;
		} else memberId = memberUserDetails.getMember().getMemberId();

		List<ReviewResponse> reviewList = reviewService.getRestaurantReviewList(restaurantId);
		BigDecimal averageRating = reviewService.getAverageRating(restaurantId).getRating();
		model.addAttribute("averageRating", averageRating);
		model.addAttribute("reviewList", reviewList);
		model.addAttribute("memberId", memberId);

		return "review/reviews";
	}

	@GetMapping("/create")
	public String showReviewForm(@PathVariable Long restaurantId, Model model
								 ) {

		model.addAttribute("restaurantId", restaurantId);
		model.addAttribute("reviewCreateRequest", new ReviewCreateRequest(null, null));
		return "review/reviewCreateForm";
	}

	@PostMapping("/create")
	public String addReview(@PathVariable Long restaurantId,
							@Valid @ModelAttribute ReviewCreateRequest reviewCreateRequest,
							BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
							@AuthenticationPrincipal MemberUserDetails memberUserDetails) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("restaurantId", restaurantId);
			return "review/reviewCreateForm";
		}

		if (Objects.isNull(memberUserDetails)) {
			redirectAttributes.addFlashAttribute("errorMessage", "로그인하지 않은 사용자입니다. 로그인 후 이용 부탁드립니다.");
		} else {
			Long memberId = memberUserDetails.getMember().getMemberId();
			reviewService.createReview(memberId, restaurantId, reviewCreateRequest);

		}
		return String.format("redirect:/restaurants/%d/reviews", restaurantId);
	}


	@GetMapping("/{reviewId}/update")
	public String updateForm(@PathVariable Long restaurantId, @PathVariable Long reviewId,
							 RedirectAttributes redirectAttributes, Model model, @AuthenticationPrincipal MemberUserDetails memberUserDetails) {

		Long memberId;
		try {
			memberId = memberUserDetails.getMember().getMemberId();
			ReviewResponse reviewResponse = reviewService.getReviewById(reviewId, memberId);
			model.addAttribute("review", reviewResponse);
			model.addAttribute("reviewId", reviewId);
			model.addAttribute("restaurantId", restaurantId);
		} catch (NullPointerException | UnauthorizedException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return String.format("redirect:/restaurants/%d/reviews", restaurantId);
		}

		return "review/reviewUpdateForm";
	}

	@PutMapping("/{reviewId}")
	public String updateReview(@PathVariable Long restaurantId, @PathVariable Long reviewId,
							   @Valid @ModelAttribute("review") ReviewUpdateRequest reviewUpdateRequest, BindingResult bindingResult,
							   Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal MemberUserDetails memberUserDetails) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("reviewId", reviewId);
			model.addAttribute("restaurantId", restaurantId);
			return "review/reviewUpdateForm";
		}

		Long memberId;
		try {
			memberId = memberUserDetails.getMember().getMemberId();
			reviewService.updateReview(reviewId, memberId, reviewUpdateRequest.getRating(), reviewUpdateRequest.getContent());
		} catch (NullPointerException | UnauthorizedException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		return String.format("redirect:/restaurants/%d/reviews", restaurantId);
	}

	@DeleteMapping("/{reviewId}")
	public String deleteReview(@PathVariable Long restaurantId, @PathVariable Long reviewId,
							   RedirectAttributes redirectAttributes, @AuthenticationPrincipal MemberUserDetails memberUserDetails) {
		Long memberId;
		try {
			memberId = memberUserDetails.getMember().getMemberId();
			reviewService.deleteReview(reviewId, memberId);
		} catch (NullPointerException | UnauthorizedException e) {
			log.info("exception");
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		return String.format("redirect:/restaurants/%d/reviews", restaurantId);
	}
}
