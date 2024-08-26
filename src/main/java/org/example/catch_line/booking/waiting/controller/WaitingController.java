package org.example.catch_line.booking.waiting.controller;

import org.example.catch_line.booking.waiting.model.dto.WaitingRequest;
import org.example.catch_line.booking.waiting.model.dto.WaitingResponse;
import org.example.catch_line.booking.waiting.service.WaitingService;
import org.example.catch_line.common.session.SessionUtils;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.config.auth.MemberUserDetails;
import org.example.catch_line.exception.booking.WaitingException;
import org.example.catch_line.exception.session.InvalidSessionException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WaitingController {

	private final WaitingService waitingService;

	@GetMapping("/restaurants/{restaurantId}/waiting")
	public String addWaitingForm(@PathVariable Long restaurantId, Model model) {
		model.addAttribute("restaurantId", restaurantId);
		return "waiting/waiting";
	}

	//@RequestParam으로 변경
	@PostMapping("/restaurants/{restaurantId}/waiting")
	public String addWaiting(
		@PathVariable Long restaurantId,
		@ModelAttribute WaitingRequest waitingRequest,
		Model model,
		@AuthenticationPrincipal MemberUserDetails userDetails
	) {
		try {
			Long memberId = userDetails.getMember().getMemberId();

			boolean isExisting = waitingService.isExistingWaiting(memberId, Status.SCHEDULED);
			if (isExisting) {
				// 오류 메시지를 추가하고 현재 페이지로 리디렉션
				model.addAttribute("error", "이미 예약이 존재합니다");
				return "waiting/waiting"; // 현재 페이지를 보여주는 템플릿 이름으로 수정
			}

			WaitingResponse waitingResponse = waitingService.addWaiting(restaurantId, waitingRequest, memberId);

			model.addAttribute("restaurantId", restaurantId);
			model.addAttribute("waitingResponse", waitingResponse);
			return "redirect:/history";
		} catch (InvalidSessionException | WaitingException e) {
			// 오류 메시지를 추가하고 현재 페이지로 리디렉션
			model.addAttribute("error", "Waiting failed: " + e.getMessage());
			return "waiting/waiting"; // 현재 페이지를 보여주는 템플릿 이름으로 수정
		}
	}

}
