package org.example.catch_line.reservation.controller;

import java.time.LocalDateTime;

import org.example.catch_line.reservation.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	@GetMapping("/restaurants/{restaurantId}/{memberId}/reservations")
	public String addReservationForm(
		@PathVariable("restaurantId") Long restaurantId,
		@PathVariable("memberId") Long memberId, Model model) {
		model.addAttribute("restaurantId", restaurantId);
		model.addAttribute("memberId", memberId);
		return "reservation/reservation";
	}

	@PostMapping("/restaurants/{restaurantId}/{memberId}/reservations")
	public String addReservation(
		@PathVariable("restaurantId") Long restaurantId,
		@PathVariable("memberId") Long memberId,
		@RequestParam(value = "memberCount") Integer memberCount,
		@RequestParam(value = "reservationDate")LocalDateTime reservationDate,
		// HttpSession session,
		Model model
	) {
		return "reservation/reservation";
	}
}
