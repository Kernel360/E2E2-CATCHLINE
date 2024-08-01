package org.example.catch_line.waiting.model.dto;

import org.example.catch_line.waiting.model.entity.WaitingStatus;
import org.example.catch_line.waiting.model.entity.WaitingType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class WaitingRequest {

<<<<<<< HEAD
=======
	@NotBlank(message = "waitingId 는 채워져있어야 합니다!")
	private Long waitingId;

>>>>>>> 3c60282f0c8264a62e98221854c3026b2536d8ca
	@NotBlank
	@Min(value = 1, message = "최소 인원 수는 1명입니다")
	private int memberCount;

<<<<<<< HEAD
=======
	@NotBlank(message = "상태는 빈 값일 수 없습니다.")
	private WaitingStatus waitingStatus;

>>>>>>> 3c60282f0c8264a62e98221854c3026b2536d8ca
	@NotBlank(message = "포장이나 매장을 선택해야 합니다")
	private WaitingType waitingType;
}
