package org.example.catch_line.booking.waiting.model.mapper;

import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.model.dto.WaitingResponse;
import org.springframework.stereotype.Service;

@Service
public class WaitingResponseMapper {

	public WaitingResponse convertToResponse(WaitingEntity entity) {
		return WaitingResponse.builder()
			.waitingId(entity.getWaitingId())
			.memberCount(entity.getMemberCount())
			.status(entity.getStatus())
			.waitingType(entity.getWaitingType())
			.createdAt(entity.getCreatedAt())
			.modifiedAt(entity.getModifiedAt())
			.build();
	}

}
