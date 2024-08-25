package org.example.catch_line.history.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.example.catch_line.booking.reservation.model.dto.ReservationRequest;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.repository.ReservationRepository;
import org.example.catch_line.booking.reservation.service.ReservationService;
import org.example.catch_line.booking.waiting.service.WaitingService;
import org.example.catch_line.common.session.SessionUtils;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.exception.booking.BookingErrorException;
import org.example.catch_line.exception.booking.DuplicateReservationTimeException;
import org.example.catch_line.exception.booking.HistoryException;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.history.service.HistoryService;
import org.example.catch_line.history.validation.HistoryValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HistoryController {

	private final ReservationService reservationService;
	private final WaitingService waitingService;
	private final HistoryService historyService;
	private final HistoryValidator historyValidator;

	@GetMapping("/history")
	public String getHistories(
		Model model,
		HttpSession session,
		@RequestParam(defaultValue = "SCHEDULED") Status status
	) {
		Long memberId = SessionUtils.getMemberId(session);
		List<HistoryResponse> allHistory = historyService.getAllHistory(memberId, status);
		session.setAttribute("historyList", allHistory);
		model.addAttribute("history", allHistory);

		return "history/history";
	}

	@GetMapping("/history/waiting/{waitingId}")
	public String getWaitingDetail(@PathVariable Long waitingId, HttpSession session, Model model) {

		List<HistoryResponse> historyList = (List<HistoryResponse>)session.getAttribute("historyList");

		if (historyList != null) {
			try {
				HistoryResponse historyResponse = historyService.findWaitingDetailById(historyList, waitingId);
				model.addAttribute("historyResponse", historyResponse);
				return "history/waitingDetail";
			} catch (HistoryException e) {
				model.addAttribute("errorMessage", "지금은 상세정보를 조회할 수 없습니다");
				return "error";
			}
		}

		return "redirect:/history";
	}

	@GetMapping("/history/reservation/{reservationId}")
	public String getReservationDetail(
		@PathVariable Long reservationId,
		Model model,
		HttpSession session
	) {
		List<HistoryResponse> historyList = (List<HistoryResponse>)session.getAttribute("historyList");

		if (historyList != null) {
			try {
				HistoryResponse historyResponse = historyService.findReservationDetailById(historyList, reservationId);
				model.addAttribute("historyResponse", historyResponse);
				return "history/reservationDetail";
			} catch (HistoryException e) {
				model.addAttribute("errorMessage", e.getMessage());
				return "error";
			}
		}
		return "redirect:/history";
	}

	@PostMapping("/history/reservation/{reservationId}")
	public String deleteReservation(@PathVariable Long reservationId, Model model, HttpSession session) {
		Long memberId = SessionUtils.getMemberId(session);
		try {
			historyValidator.validateReservationOwnership(memberId, reservationId);
			reservationService.cancelReservation(memberId, reservationId);
		} catch (BookingErrorException | HistoryException e) {
			model.addAttribute( "errorMessage", e.getMessage());
		}
		return "redirect:/history";
	}

	@PostMapping("/history/waiting/{waitingId}")
	public String deleteWaiting(@PathVariable Long waitingId, Model model, HttpSession session) {
		Long memberId = SessionUtils.getMemberId(session);
		try {
			historyValidator.validateWaitingOwnership(memberId, waitingId);
			waitingService.cancelWaiting(memberId, waitingId);
		} catch (BookingErrorException | HistoryException e) {
			model.addAttribute( "errorMessage", e.getMessage());
		}

		return "redirect:/history";
	}

	@GetMapping("/history/reservation/{reservationId}/edit")
	public String updateForm(@PathVariable Long reservationId, Model model) {
		ReservationEntity reservationEntity = reservationService.findReservationById(reservationId);

		ReservationRequest reservationRequest = ReservationRequest.builder()
			.memberCount(reservationEntity.getMemberCount())
			.reservationDate(reservationEntity.getReservationDate())
			.build();

		model.addAttribute("reservationRequest", reservationRequest);
		model.addAttribute("reservationId", reservationId);

		return "reservation/updateReservation";
	}

	@PutMapping("/history/reservation/{reservationId}")
	public String updateReservation(@PathVariable Long reservationId, @Valid @ModelAttribute ReservationRequest updateRequest,
									RedirectAttributes redirectAttributes, HttpSession session) {
		Long memberId = SessionUtils.getMemberId(session);
		try {
			historyValidator.validateReservationOwnership(memberId, reservationId);
			reservationService.updateReservation(memberId, reservationId, updateRequest.getMemberCount(), updateRequest.getReservationDate());
			redirectAttributes.addFlashAttribute("message", "예약이 업데이트 되었습니다");
		} catch (DuplicateReservationTimeException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/history/reservation/" + reservationId + "/edit";
		}

		return "redirect:/history";
	}

}
