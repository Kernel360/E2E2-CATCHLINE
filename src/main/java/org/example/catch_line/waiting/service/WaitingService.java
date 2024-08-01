package org.example.catch_line.waiting.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.catch_line.waiting.model.dto.WaitingRequest;
import org.example.catch_line.waiting.model.dto.WaitingResponse;
import org.example.catch_line.waiting.model.entity.WaitingEntity;
import org.example.catch_line.waiting.model.entity.WaitingStatus;
import org.example.catch_line.waiting.model.mapper.WaitingResponseMapper;
import org.example.catch_line.waiting.repository.WaitingRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingService {

	private final WaitingRepository waitingRepository;
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



}
