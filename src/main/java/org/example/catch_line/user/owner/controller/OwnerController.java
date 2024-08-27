package org.example.catch_line.user.owner.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.service.ReservationService;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.service.WaitingService;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.common.kakao.service.KakaoAddressService;
import org.example.catch_line.common.session.SessionConst;
import org.example.catch_line.common.session.SessionUtils;
import org.example.catch_line.dining.menu.service.MenuService;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.dining.restaurant.service.RestaurantHourService;
import org.example.catch_line.dining.restaurant.service.RestaurantImageService;
import org.example.catch_line.dining.restaurant.service.RestaurantService;
import org.example.catch_line.exception.booking.BookingErrorException;
import org.example.catch_line.exception.booking.HistoryException;
import org.example.catch_line.exception.session.InvalidSessionException;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.history.service.HistoryService;
import org.example.catch_line.review.service.ReviewService;
import org.example.catch_line.user.owner.service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {


	private final OwnerService ownerService;
	private final HistoryService historyService;
	private final WaitingService waitingService;
	private final ReservationService reservationService;



	@GetMapping
	public String viewOwnerPage(HttpSession httpSession, Model model) {
		boolean isLoggedIn = httpSession.getAttribute(SessionConst.OWNER_ID) != null;
		model.addAttribute("isLoggedIn", isLoggedIn);

		return "owner/owner";
	}

	@GetMapping("/restaurants/listHistory")
	public String showRestaurantListPage2(HttpSession session, Model model) {
		try {
			List<RestaurantResponse> restaurantResponseList = getRestaurantResponseList(session);
			model.addAttribute("restaurantList", restaurantResponseList);

			return "owner/restaurantListHistory";
		} catch (InvalidSessionException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "redirect:/owner";
		}
	}

	@GetMapping("/restaurants/{restaurantId}/history")
	public String showHistory(@PathVariable Long restaurantId, @RequestParam(defaultValue = "SCHEDULED") Status status ,Model model, HttpSession session) {
		List<HistoryResponse> historyResponses = ownerService.findHistoryByRestaurantIdAndStatus(restaurantId,status);
		session.setAttribute("historyList",historyResponses);
		model.addAttribute("history",historyResponses);
		model.addAttribute("restaurantId",restaurantId);

		return "owner/history";
	}

	@GetMapping("/restaurants/{restaurantId}/history/waiting/{waitingId}")
	public String getWaitingDetails(@PathVariable Long restaurantId, @PathVariable Long waitingId, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		List<HistoryResponse> historyList = (List<HistoryResponse>)session.getAttribute("historyList");
		model.addAttribute("restaurantId", restaurantId);

		if (historyList != null) {
			try {
				HistoryResponse historyResponse = historyService.findWaitingDetailById(historyList, waitingId);
				model.addAttribute("historyResponse", historyResponse);
				model.addAttribute("restaurantId",restaurantId);
				return "owner/waitingDetail";
			} catch (HistoryException e) {
				redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
				return "redirect:/owner";
			}
		}

		return "redirect:/owner";
	}

	@PostMapping("/restaurants/{restaurantId}/history/reservation/{reservationId}")
	public String deleteReservation(@PathVariable Long restaurantId, @PathVariable Long reservationId, Model model,	RedirectAttributes redirectAttributes) {
		try {
			ReservationEntity reservation = reservationService.findReservationById(reservationId);
			Long memberId = reservation.getMember().getMemberId();
			reservationService.cancelReservation(memberId, reservationId);
		} catch (BookingErrorException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/owner";
		}

		return "redirect:/owner";
	}

	@PostMapping("/restaurants/{restaurantId}/history/reservation/{reservationId}/completed")
	public String completedReservation(@PathVariable Long restaurantId, @PathVariable Long reservationId, Model model, RedirectAttributes redirectAttributes) {
		try {
			reservationService.completedReservation(reservationId);
		} catch (BookingErrorException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/owner";
		}

		return "redirect:/owner";
	}

	@PostMapping("/restaurants/{restaurantId}/history/waiting/{waitingId}")
	public String deleteWaiting(@PathVariable Long restaurantId, @PathVariable Long waitingId, Model model,RedirectAttributes redirectAttributes) {
		try {
			WaitingEntity waiting = waitingService.getWaitingEntity(waitingId);
			Long memberId = waiting.getMember().getMemberId();
			waitingService.cancelWaiting(memberId, waitingId);
		} catch (BookingErrorException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/owner";
		}

		return "redirect:/owner";
	}

	@PostMapping("/restaurants/{restaurantId}/history/waiting/{waitingId}/completed")
	public String completeWaiting(@PathVariable Long restaurantId, @PathVariable Long waitingId, Model model, RedirectAttributes redirectAttributes) {
		try {
			waitingService.completedWaiting(waitingId);
		} catch (BookingErrorException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/owner";
		}

		return "redirect:/owner";
	}

	@GetMapping("/restaurants/{restaurantId}/history/reservation/{reservationId}")
	public String getReservationDetails(@PathVariable Long restaurantId, @PathVariable Long reservationId,
										Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		List<HistoryResponse> historyList = (List<HistoryResponse>) session.getAttribute("historyList");

		if (historyList != null) {
			try {
				HistoryResponse historyResponse = historyService.findReservationDetailById(historyList, reservationId);
				model.addAttribute("historyResponse", historyResponse);
				model.addAttribute("restaurantId", restaurantId);

				return "owner/reservationDetail";
			} catch (HistoryException e) {
				redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
				return "redirect:/owner/reservationDetail";
			}
		}
		return "redirect:/owner";
	}

	@GetMapping("/restaurants/list")
	public String showRestaurantListPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		try {
			List<RestaurantResponse> restaurantResponseList = getRestaurantResponseList(session);
			model.addAttribute("restaurantList", restaurantResponseList);

			return "owner/restaurantList";
		} catch (InvalidSessionException e) {
			log.info("error : {}", e.getClass());
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/owner";
		}
	}

	private List<RestaurantResponse> getRestaurantResponseList(HttpSession session) {
		Long ownerId = SessionUtils.getOwnerId(session);
		return ownerService.findAllRestaurantByOwnerId(ownerId);
	}

}
