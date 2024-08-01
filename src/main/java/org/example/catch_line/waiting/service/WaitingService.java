package org.example.catch_line.waiting.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.catch_line.waiting.model.dto.WaitingRequest;
<<<<<<< HEAD
import org.example.catch_line.waiting.model.dto.WaitingResponse;
import org.example.catch_line.waiting.model.entity.WaitingEntity;
import org.example.catch_line.waiting.model.entity.WaitingStatus;
import org.example.catch_line.waiting.model.mapper.WaitingResponseMapper;
import org.example.catch_line.waiting.repository.WaitingRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
=======
import org.example.catch_line.waiting.model.entity.WaitingEntity;
import org.example.catch_line.waiting.model.entity.WaitingStatus;
import org.example.catch_line.waiting.model.entity.WaitingType;
import org.example.catch_line.waiting.repository.WaitingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

>>>>>>> 3c60282f0c8264a62e98221854c3026b2536d8ca
@Service
@RequiredArgsConstructor
public class WaitingService {

	private final WaitingRepository waitingRepository;
<<<<<<< HEAD
	private final WaitingResponseMapper waitingResponseMapper;

	public WaitingResponse addWaiting(WaitingRequest waitingRequest) {
		WaitingEntity waiting = WaitingEntity.builder()
			.memberCount(waitingRequest.getMemberCount())
			.waitingStatus(WaitingStatus.SCHEDULED)
			.waitingType(waitingRequest.getWaitingType())
			.build();
		WaitingEntity savedEntity = waitingRepository.save(waiting);

		return waitingResponseMapper.convertToResponse(savedEntity);
	}

	public List<WaitingResponse> getAllWaiting() {
		List<WaitingEntity> waitingEntities = waitingRepository.findAll();

		return waitingEntities.stream()
			.map(waitingResponseMapper::convertToResponse)
			.collect(Collectors.toList());
	}

	public WaitingResponse getWaitingById(Long id){
		WaitingEntity waitingEntity = waitingRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("정확한 아이디가 아닙니다: "+id));
		return waitingResponseMapper.convertToResponse(waitingEntity);
	}

	public void cancelWaiting(Long id) {
		WaitingEntity entity = waitingRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("웨이팅 아이디가 다릅니다: "+id));
		entity.setWaitingStatus(WaitingStatus.CANCELED);
		waitingRepository.save(entity);
	}



=======

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

>>>>>>> 3c60282f0c8264a62e98221854c3026b2536d8ca
}
