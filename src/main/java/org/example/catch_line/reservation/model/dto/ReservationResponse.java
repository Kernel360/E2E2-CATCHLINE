package org.example.catch_line.reservation.model.dto;

import java.time.LocalDateTime;

import org.example.catch_line.common.constant.Status;
import org.example.catch_line.reservation.validation.ValidReservationDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationResponse {

	@NotBlank
	private Long reservationId;

	@NotBlank
	@Min(value = 1, message = "최소 인원 수는 1명입니다")
	private int memberCount;

	@ValidReservationDate
	@NotBlank(message = "날짜를 선택해야 합니다")
	private LocalDateTime reservationDate;

	@NotBlank(message = "현재 상태가 존재해야 합니다")
	private Status status;

	private LocalDateTime createdAt;

	private LocalDateTime modifiedAt;

}
