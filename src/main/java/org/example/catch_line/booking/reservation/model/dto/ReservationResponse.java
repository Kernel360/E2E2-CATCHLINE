package org.example.catch_line.booking.reservation.model.dto;

import java.time.LocalDateTime;

import org.example.catch_line.common.constant.Status;
import org.example.catch_line.booking.reservation.validation.ValidReservationDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationResponse {

	private Long reservationId;

	private int memberCount;

	private LocalDateTime reservationDate;

	private Status status;

	private LocalDateTime createdAt;

	private LocalDateTime modifiedAt;

}
