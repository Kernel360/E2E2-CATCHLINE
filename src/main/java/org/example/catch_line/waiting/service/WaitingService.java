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

public class WaitingService {


	private final WaitingRepository waitingRepository;

	@Autowired
	public WaitingService(WaitingRepository waitingRepository) {
		this.waitingRepository = waitingRepository;
	}

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

		WaitingRequest request = new WaitingRequest();
		request.setWaitingId(entity.getWaitingId());
		request.setMemberCount(entity.getMemberCount());
		request.setWaitingStatus(entity.getWaitingStatus());
		request.setWaitingType(entity.getWaitingType());

		return request;
	}

}
