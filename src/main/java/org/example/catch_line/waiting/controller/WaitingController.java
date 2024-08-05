package org.example.catch_line.waiting.controller;

import org.example.catch_line.common.constant.SessionConst;
import org.example.catch_line.waiting.model.dto.WaitingRequest;
import org.example.catch_line.waiting.model.dto.WaitingResponse;
import org.example.catch_line.waiting.model.entity.WaitingType;
import org.example.catch_line.waiting.service.WaitingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WaitingController {

	private final WaitingService waitingService;

	@GetMapping("/restaurants/{restaurantId}/waiting")
	public String addWaitingForm(
		@PathVariable("restaurantId") Long restaurantId
	) {

		return "waiting/waiting";
	}

	//세션을 사용해서 memberId를 갖고오고 싶은데 아직 구현이 안될거 같아서 일단 PathVariable 로 memberId를 입력하고 db에 저장되는 것 확인했습니다..!!
	//후에 다시 세션으로 받아올 수 있게 수정해야 할 것 같아요,,!
	@PostMapping("/restaurants/{restaurantId}/waiting")
	public String addWaiting(
		@PathVariable Long restaurantId,
		@RequestParam Integer memberCount,
		@RequestParam String waitingType,
		Model model,
		HttpSession session
	) {
		Long memberId = (Long) session.getAttribute(SessionConst.MEMBER_ID);
		if(memberId == null) {
			throw new RuntimeException("로그인이 필요합니다");
		}



		WaitingType type = "DINE_IN".equals(waitingType) ? WaitingType.DINE_IN : WaitingType.TAKE_OUT;

		WaitingRequest waitingRequest = WaitingRequest.builder()
			.memberCount(memberCount)
			.waitingType(type)
			.build();

		WaitingResponse waitingResponse = waitingService.addWaiting(memberId, restaurantId, waitingRequest);

		model.addAttribute("waitingResponse", waitingResponse);

		return "waiting/waiting";
	}

}
