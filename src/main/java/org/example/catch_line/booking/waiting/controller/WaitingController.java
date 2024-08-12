package org.example.catch_line.booking.waiting.controller;

import org.example.catch_line.booking.waiting.model.dto.WaitingRequest;
import org.example.catch_line.booking.waiting.model.dto.WaitingResponse;
import org.example.catch_line.booking.waiting.model.entity.WaitingType;
import org.example.catch_line.booking.waiting.repository.WaitingRepository;
import org.example.catch_line.booking.waiting.service.WaitingService;
import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.exception.booking.WaitingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WaitingController {

	private final WaitingService waitingService;
	private final WaitingRepository waitingRepository;

	@GetMapping("/restaurants/{restaurantId}/waiting")
	public String addWaitingForm(
		@PathVariable("restaurantId") Long restaurantId
	) {

		return "waiting/waiting";
	}

	//@RequestParam으로 변경
	@PostMapping("/restaurants/{restaurantId}/waiting")
	public String addWaiting(
		@PathVariable Long restaurantId,
		@ModelAttribute WaitingRequest waitingRequest,
		Model model,
		HttpSession session,
		RedirectAttributes redirectAttributes
	) {
		try {
			Long memberId = SessionUtils.getMemberId(session);

			boolean isExisting = waitingRepository.existsByMemberMemberIdAndStatus(memberId, Status.SCHEDULED);
			if (isExisting) {
				// 오류 메시지를 추가하고 현재 페이지로 리디렉션
				model.addAttribute("error", "이미 예약이 존재합니다");
				return "waiting/waiting"; // 현재 페이지를 보여주는 템플릿 이름으로 수정
			}

			WaitingResponse waitingResponse = waitingService.addWaiting(restaurantId, waitingRequest, memberId);

			model.addAttribute("waitingResponse", waitingResponse);

			return "redirect:/history";
		} catch (IllegalArgumentException e) {
			// 오류 메시지를 추가하고 현재 페이지로 리디렉션
			model.addAttribute("error", "Waiting failed: " + e.getMessage());
			return "waiting/waiting"; // 현재 페이지를 보여주는 템플릿 이름으로 수정
		}
	}


}
