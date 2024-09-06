package org.example.catch_line.booking.waiting.controller;

import org.example.catch_line.booking.waiting.model.dto.WaitingRequest;
import org.example.catch_line.booking.waiting.model.dto.WaitingResponse;
import org.example.catch_line.booking.waiting.service.WaitingService;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.user.auth.details.MemberUserDetails;
import org.example.catch_line.exception.booking.WaitingException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WaitingController {

	private final WaitingService waitingService;

	@GetMapping("/restaurants/{restaurantId}/waitings")
	public String addWaitingForm(@PathVariable Long restaurantId, Model model) {
		model.addAttribute("restaurantId", restaurantId);
		return "waiting/waiting";
	}

	@PostMapping("/restaurants/{restaurantId}/waitings")
	public String addWaiting(
		@PathVariable Long restaurantId,
		@ModelAttribute WaitingRequest waitingRequest,
		@AuthenticationPrincipal MemberUserDetails userDetails,
		Model model
	) {
		try {
			Long memberId = userDetails.getMember().getMemberId();

			boolean isExisting = waitingService.isExistingWaiting(memberId, Status.SCHEDULED);
			if (isExisting) {
				model.addAttribute("error", "이미 웨이팅이 존재합니다");
				return "waiting/waiting";
			}

			WaitingResponse waitingResponse = waitingService.addWaiting(restaurantId, waitingRequest, memberId);

			model.addAttribute("restaurantId", restaurantId);
			model.addAttribute("waitingResponse", waitingResponse);
			return "redirect:/history";
		} catch (WaitingException e) {
			model.addAttribute("error", "Waiting failed: " + e.getMessage());
			return "waiting/waiting";
		}
	}

}
