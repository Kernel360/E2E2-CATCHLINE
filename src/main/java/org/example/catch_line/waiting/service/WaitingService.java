package org.example.catch_line.waiting.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.catch_line.waiting.model.dto.WaitingRequest;
import org.example.catch_line.waiting.model.entity.WaitingEntity;
import org.example.catch_line.waiting.model.entity.WaitingStatus;
import org.example.catch_line.waiting.model.entity.WaitingType;
import org.example.catch_line.waiting.repository.WaitingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WaitingService {

	private final WaitingRepository waitingRepository;

	public WaitingRequest addWaiting(int memberCount, WaitingType waitingType) {
		WaitingEntity waiting = WaitingEntity.builder()
			.memberCount(memberCount)
			.waitingStatus(WaitingStatus.SCHEDULED)
			.waitingType(waitingType)
			.build();
		waitingRepository.save(waiting);

		return convertToRequest(waiting);
	}

	public List<WaitingRequest> getAllWaiting() {
		List<WaitingEntity> waitingEntities = waitingRepository.findAll();

		return waitingEntities.stream()
			.map(this::convertToRequest)
			.collect(Collectors.toList());
	}

	private WaitingRequest convertToRequest(WaitingEntity entity) {

		return WaitingRequest.builder()
			.waitingId(entity.getWaitingId())
			.memberCount(entity.getMemberCount())
			.waitingStatus(entity.getWaitingStatus())
			.waitingType(entity.getWaitingType())
			.build();
	}

}
