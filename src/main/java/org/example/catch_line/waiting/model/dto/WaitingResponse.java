package org.example.catch_line.waiting.model.dto;

import org.example.catch_line.waiting.model.entity.WaitingStatus;
import org.example.catch_line.waiting.model.entity.WaitingType;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingResponse {

	@NotBlank
	private Long waitingId;

	@NotBlank
	@Min(value = 1, message = "최소 인원 수는 1명입니다")
	private int memberCount;

	@NotBlank(message = "포장이나 매장을 선택해야 합니다")
	private WaitingStatus waitingStatus;

	@NotBlank(message = "현재 상태가 존재해야 합니다")
	private WaitingType waitingType;
}
