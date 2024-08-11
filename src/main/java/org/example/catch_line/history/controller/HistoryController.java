package org.example.catch_line.history.controller;

import java.util.List;

import org.example.catch_line.booking.reservation.service.ReservationService;
import org.example.catch_line.booking.waiting.service.WaitingService;
import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.exception.booking.HistoryException;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.history.service.HistoryService;
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
public class HistoryController {

	private final ReservationService reservationService;
	private final WaitingService waitingService;
	private final HistoryService historyService;

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
			} catch (IllegalArgumentException e) {
				// 오류 처리: 메시지 표시 또는 로그 기록 등
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
				model.addAttribute("errorMessage", "지금은 상세정보를 조회할 수 없습니다");
				return "error";
			}
		}
		return "redirect:/history";
	}

	@PostMapping("/history/reservation/{reservationId}")
	public String deleteReservation(@PathVariable Long reservationId, Model model) {
		try {
			reservationService.cancelReservation(reservationId);
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", "예약 삭제 중 오류가 발생했습니다.");
			return "error"; // 오류 페이지로 리다이렉트
		}

		return "redirect:/history";
	}

	@PostMapping("/history/waiting/{waitingId}")
	public String deleteWaiting(@PathVariable Long waitingId, Model model) {
		try {
			waitingService.cancelWaiting(waitingId);
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", "웨이팅 삭제 중 오류가 발생했습니다.");
			return "error"; // 오류 페이지로 리다이렉트
		}

		return "redirect:/history";
	}

}
