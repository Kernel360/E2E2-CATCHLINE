package org.example.catch_line.history.controller;

import java.util.List;

import org.example.catch_line.common.constant.SessionConst;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.history.service.HistoryService;
import org.example.catch_line.reservation.service.ReservationService;
import org.example.catch_line.waiting.service.WaitingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
		List<HistoryResponse> allHistory = historyService.getAllHistory(session,status);
		model.addAttribute("history", allHistory);

		return "history/history";
	}

	@GetMapping("/history/waitings/{waitingId}")
	public String getHistory() {
		return "history/detail";
	}

}
