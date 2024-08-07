package org.example.catch_line.history.model.dto;

import java.time.LocalDateTime;

import org.example.catch_line.common.constant.Status;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.waiting.model.entity.WaitingType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class HistoryResponse {

	private Long restaurantId;

	private Long waitingId;

	private Long reservationId;

	private int memberCount;

	private String restaurantName;

	private Status status;

	private WaitingType waitingType;

	private ServiceType serviceType;

	private LocalDateTime reservationDate;

	private LocalDateTime createdAt;

	private LocalDateTime modifiedAt;

	private int waitingRegistrationId;

}
