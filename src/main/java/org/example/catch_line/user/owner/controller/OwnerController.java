package org.example.catch_line.user.owner.controller;


import java.util.List;
import java.util.Objects;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.service.ReservationService;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.service.WaitingService;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.exception.booking.BookingErrorException;
import org.example.catch_line.exception.booking.HistoryException;
import org.example.catch_line.exception.session.InvalidSessionException;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.history.service.HistoryService;
import org.example.catch_line.user.auth.details.MemberUserDetails;
import org.example.catch_line.user.auth.details.OwnerUserDetails;
import org.example.catch_line.user.owner.service.OwnerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String viewOwnerPage(@AuthenticationPrincipal OwnerUserDetails ownerUserDetails, Model model) {
		boolean isLoggedIn = Objects.nonNull(ownerUserDetails); // "user" 세션 속성으로 로그인 상태 확인
		model.addAttribute("isLoggedIn", isLoggedIn);
		return "owner/owner";
	}

	@GetMapping("/restaurants/listHistory")
	public String showRestaurantListPage2(@AuthenticationPrincipal OwnerUserDetails ownerUserDetails, Model model) {
		try {
			List<RestaurantResponse> restaurantResponseList = getRestaurantResponseList(ownerUserDetails.getOwner().getOwnerId());
			model.addAttribute("restaurantList", restaurantResponseList);
			return "owner/restaurantListHistory";
		} catch (NullPointerException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "redirect:/owner";
		}
	}

	// TODO: session 삭제 필요
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

		if (Objects.nonNull(historyList)) {
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
	@ResponseBody
	public String deleteReservation(@PathVariable Long restaurantId, @PathVariable Long reservationId, Model model,	RedirectAttributes redirectAttributes) {
		try {
			ReservationEntity reservation = reservationService.findReservationById(reservationId);
			Long memberId = reservation.getMember().getMemberId();
			reservationService.cancelReservation(memberId, reservationId);
		} catch (BookingErrorException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/owner";
		}

		return "ok";
	}

	@PostMapping("/restaurants/{restaurantId}/history/reservation/{reservationId}/completed")
	@ResponseBody
	public String completedReservation(@PathVariable Long restaurantId, @PathVariable Long reservationId, Model model, RedirectAttributes redirectAttributes) {
		try {
			reservationService.completedReservation(reservationId);
		} catch (BookingErrorException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/owner";
		}

		return "ok";
	}

	@PostMapping("/restaurants/{restaurantId}/history/waiting/{waitingId}")
	@ResponseBody
	public String deleteWaiting(@PathVariable Long restaurantId, @PathVariable Long waitingId, Model model,RedirectAttributes redirectAttributes) {
		try {
			WaitingEntity waiting = waitingService.getWaitingEntity(waitingId);
			Long memberId = waiting.getMember().getMemberId();
			waitingService.cancelWaiting(memberId, waitingId);
		} catch (BookingErrorException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/owner";
		}

		return "ok";
	}

	@PostMapping("/restaurants/{restaurantId}/history/waiting/{waitingId}/completed")
	@ResponseBody
	public String completeWaiting(@PathVariable Long restaurantId, @PathVariable Long waitingId, Model model, RedirectAttributes redirectAttributes) {
		try {
			waitingService.completedWaiting(waitingId);
		} catch (BookingErrorException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/owner";
		}

		return "ok";
	}

	@GetMapping("/restaurants/{restaurantId}/history/reservation/{reservationId}")
	public String getReservationDetails(@PathVariable Long restaurantId, @PathVariable Long reservationId,
										Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		List<HistoryResponse> historyList = (List<HistoryResponse>) session.getAttribute("historyList");

		if (Objects.nonNull(historyList)) {
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
	public String showRestaurantListPage(@AuthenticationPrincipal OwnerUserDetails ownerUserDetails, Model model, RedirectAttributes redirectAttributes) {
		try {
			List<RestaurantResponse> restaurantResponseList = getRestaurantResponseList(ownerUserDetails.getOwner().getOwnerId());
			model.addAttribute("restaurantList", restaurantResponseList);

			return "owner/restaurantList";
		} catch (InvalidSessionException e) {
			log.info("error : {}", e.getClass());
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/owner";
		}
	}

	private List<RestaurantResponse> getRestaurantResponseList(Long ownerId) {
		return ownerService.findAllRestaurantByOwnerId(ownerId);
	}

}
