package org.example.catch_line.waiting.model.dto;

import org.example.catch_line.waiting.model.entity.WaitingType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class WaitingRequest {

	@NotBlank
	@Min(value = 1, message = "최소 인원 수는 1명입니다")
	private int memberCount;

	@NotBlank(message = "포장이나 매장을 선택해야 합니다")
	private WaitingType waitingType;
}
