package org.example.catch_line.booking.reservation.model.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.booking.reservation.validation.ValidReservationDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;  // Use @NotNull for LocalDateTime
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationRequest {

	@NotNull(message = "최소 인원 수는 1명입니다")  // Change to @NotNull
	@Min(value = 1, message = "최소 인원 수는 1명입니다")
	private Integer memberCount;  // Use Integer instead of int for @NotNull

	@ValidReservationDate
	@NotNull(message = "날짜를 선택해야 합니다")  // Change to @NotNull
	private LocalDateTime reservationDate;

	private Status status;
}
