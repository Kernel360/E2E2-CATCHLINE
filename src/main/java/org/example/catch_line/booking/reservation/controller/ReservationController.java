package org.example.catch_line.booking.reservation.controller;

import jakarta.validation.Valid;
import org.example.catch_line.booking.reservation.model.dto.ReservationRequest;
import org.example.catch_line.booking.reservation.model.dto.ReservationResponse;
import org.example.catch_line.common.session.SessionUtils;
import org.example.catch_line.booking.reservation.service.ReservationService;
import org.example.catch_line.exception.CatchLineException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	@GetMapping("/restaurants/{restaurantId}/reservation")
	public String addReservationForm(
		@PathVariable Long restaurantId
	) {

		return "reservation/reservation";
	}

	//@RequestParam으로 변경
	@PostMapping("/restaurants/{restaurantId}/reservation")
	public String addReservation(
		@ModelAttribute ReservationRequest reservationRequest,
		@PathVariable Long restaurantId,
		Model model,
		HttpSession session,
		RedirectAttributes redirectAttributes
	) {
		try {
			Long memberId = SessionUtils.getMemberId(session);

			ReservationResponse reservationResponse = reservationService.addReserve(restaurantId,
				reservationRequest, memberId);

			model.addAttribute("reservationResponse", reservationResponse);

			return "redirect:/history";
		} catch (CatchLineException e) {
			redirectAttributes.addFlashAttribute("error", "예약 실패: " + e.getMessage());
			return "redirect:/restaurants/" + restaurantId + "/reservation";
		}
	}

}

