package org.example.catch_line.booking.reservation.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.booking.reservation.model.dto.ReservationRequest;
import org.example.catch_line.booking.reservation.model.dto.ReservationResponse;
import org.example.catch_line.common.session.SessionUtils;
import org.example.catch_line.booking.reservation.service.ReservationService;
import org.example.catch_line.config.auth.MemberUserDetails;
import org.example.catch_line.exception.booking.DuplicateReservationTimeException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	@GetMapping("/restaurants/{restaurantId}/reservation")
	public String addReservationForm(@PathVariable Long restaurantId, Model model) {
		model.addAttribute("restaurantId", restaurantId);
		return "reservation/reservation";
	}


	@PostMapping("/restaurants/{restaurantId}/reservation")
	public String addReservation(
		@ModelAttribute ReservationRequest reservationRequest,
		@PathVariable Long restaurantId,
		Model model,
		RedirectAttributes redirectAttributes,
		@AuthenticationPrincipal MemberUserDetails userDetails
	) {
		try {
			Long memberId = userDetails.getMember().getMemberId();
			ReservationResponse reservationResponse = reservationService.addReservation(memberId, restaurantId, reservationRequest);

			model.addAttribute("restaurantId", restaurantId);
			model.addAttribute("reservationResponse", reservationResponse);
			return "redirect:/history";
		} catch (DuplicateReservationTimeException e) {
			redirectAttributes.addFlashAttribute("error", "예약 실패: " + e.getMessage());
			return "redirect:/restaurants/" + restaurantId + "/reservation";
		}
	}

}

