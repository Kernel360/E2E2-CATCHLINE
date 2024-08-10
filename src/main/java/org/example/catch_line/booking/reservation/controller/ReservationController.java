package org.example.catch_line.booking.reservation.controller;

import java.time.LocalDateTime;

import org.example.catch_line.booking.reservation.model.dto.ReservationRequest;
import org.example.catch_line.booking.reservation.model.dto.ReservationResponse;
import org.example.catch_line.booking.waiting.model.dto.WaitingRequest;
import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.booking.reservation.service.ReservationService;
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
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("error", "Reservation failed: " + e.getMessage());
			return "redirect:/restaurants/" + restaurantId + "/reservation";
		}
	}
}

