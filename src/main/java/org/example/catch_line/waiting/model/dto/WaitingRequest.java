package org.example.catch_line.waiting.model.dto;

import org.example.catch_line.waiting.model.entity.WaitingStatus;
import org.example.catch_line.waiting.model.entity.WaitingType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WaitingRequest {
	private Long waitingId;
	private int memberCount;
	private WaitingStatus waitingStatus;
	private WaitingType waitingType;
}
