package org.example.catch_line.reservation.controller;

import java.time.LocalDateTime;

import org.example.catch_line.common.constant.Status;
import org.example.catch_line.reservation.model.dto.ReservationRequest;
import org.example.catch_line.reservation.model.dto.ReservationResponse;
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

	@GetMapping("/restaurants/{restaurantId}/{memberId}/reservation")
	public String addReservationForm(
		@PathVariable("restaurantId") Long restaurantId,
		@PathVariable("memberId") Long memberId) {

		return "reservation/reservation";
	}

	@PostMapping("/restaurants/{restaurantId}/{memberId}/reservation")
	public String addReservation(
		@PathVariable("restaurantId") Long restaurantId,
		@PathVariable("memberId") Long memberId,
		@RequestParam(value = "memberCount") Integer memberCount,
		@RequestParam(value = "reservationDate") LocalDateTime reservationDate,
		Model model
		// HttpSession session

	) {

		ReservationRequest reservationRequest = new ReservationRequest();
		reservationRequest.setReservationDate(reservationDate);
		reservationRequest.setMemberCount(memberCount);
		reservationRequest.setStatus(Status.SCHEDULED);

		ReservationResponse reservationResponse = reservationService.addReserve(memberId, restaurantId,
			reservationRequest);

		model.addAttribute("reservationResponse", reservationResponse);

		return "reservation/reservation";
	}
}
