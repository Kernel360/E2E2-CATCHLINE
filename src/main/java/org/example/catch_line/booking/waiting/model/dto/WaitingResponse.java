package org.example.catch_line.booking.waiting.model.dto;

import java.time.LocalDateTime;

import org.example.catch_line.booking.waiting.model.entity.WaitingType;
import org.example.catch_line.common.constant.Status;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WaitingResponse {

	@NotBlank
	private Long waitingId;

	@NotBlank
	@Min(value = 1, message = "최소 인원 수는 1명입니다")
	private int memberCount;

	@NotBlank(message = "현재 상태가 존재해야 합니다")
	private Status status;

	@NotBlank(message = "포장이나 매장을 선택해야 합니다")
	private WaitingType waitingType;

	private LocalDateTime createdAt;

	private LocalDateTime modifiedAt;
}
