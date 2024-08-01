package org.example.catch_line.waiting.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.example.catch_line.waiting.model.dto.WaitingRequest;
import org.example.catch_line.waiting.model.dto.WaitingResponse;
import org.example.catch_line.waiting.model.entity.WaitingEntity;
import org.example.catch_line.waiting.model.entity.WaitingStatus;
import org.example.catch_line.waiting.model.entity.WaitingType;
import org.example.catch_line.waiting.repository.WaitingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class WaitingServiceTest {

	@Autowired
	WaitingService waitingService;
	@Autowired
	WaitingRepository waitingRepository;

	@BeforeEach
	void setUp() {
		waitingRepository.deleteAll();
	}

	@Test
	@DisplayName("웨이팅 추가 테스트")
	void testAddWaiting() {

		WaitingRequest request = WaitingRequest.builder()
			.waitingType(WaitingType.DINE_IN)
			.memberCount(4)
			.build();

		WaitingResponse response = waitingService.addWaiting(request);

		WaitingEntity entity = waitingRepository.findById(response.getWaitingId()).orElseThrow();

		assertThat(response.getWaitingId()).isEqualTo(entity.getWaitingId());
		assertThat(response.getWaitingStatus()).isEqualTo(entity.getWaitingStatus());
		assertThat(response.getWaitingType()).isEqualTo(entity.getWaitingType());
		assertThat(response.getMemberCount()).isEqualTo(entity.getMemberCount());

	}

	@Test
	@DisplayName("웨이팅 전체 조회 테스트")
	void testGetAllWaiting() {
		WaitingRequest request1 = WaitingRequest.builder()
			.waitingType(WaitingType.DINE_IN)
			.memberCount(2)
			.build();
		WaitingRequest request2 = WaitingRequest.builder()
			.waitingType(WaitingType.TAKE_OUT)
			.memberCount(3)
			.build();

		waitingService.addWaiting(request1);
		waitingService.addWaiting(request2);

		List<WaitingResponse> responses = waitingService.getAllWaiting();

		assertThat(responses).hasSize(2);
		WaitingResponse response1 = responses.get(0);
		WaitingResponse response2 = responses.get(1);

		assertThat(response1.getMemberCount()).isIn(2, 3);
		assertThat(response1.getWaitingType()).isIn(WaitingType.DINE_IN, WaitingType.TAKE_OUT);
		assertThat(response1.getWaitingStatus()).isEqualTo(WaitingStatus.SCHEDULED);

		assertThat(response2.getMemberCount()).isIn(2, 3);
		assertThat(response2.getWaitingType()).isIn(WaitingType.DINE_IN, WaitingType.TAKE_OUT);
		assertThat(response2.getWaitingStatus()).isEqualTo(WaitingStatus.SCHEDULED);

	}

	@Test
	@DisplayName("웨이팅 개별 조회 테스트")
	void testGetWaitingById() {
		WaitingRequest request = WaitingRequest.builder()
			.waitingType(WaitingType.DINE_IN)
			.memberCount(4)
			.build();

		WaitingResponse addedResponse = waitingService.addWaiting(request);
		Long id = addedResponse.getWaitingId();

		WaitingResponse response = waitingService.getWaitingById(id);

		assertThat(response.getWaitingId()).isEqualTo(id);
		assertThat(response.getMemberCount()).isEqualTo(request.getMemberCount());
		assertThat(response.getWaitingType()).isEqualTo(request.getWaitingType());
		assertThat(response.getWaitingStatus()).isEqualTo(WaitingStatus.SCHEDULED);
	}

	@Test
	@DisplayName("웨이팅 취소 테스트")
	void testCancelWaiting() {
		WaitingRequest request = WaitingRequest.builder()
			.waitingType(WaitingType.DINE_IN)
			.memberCount(4)
			.build();

		WaitingResponse response = waitingService.addWaiting(request);
		Long id = response.getWaitingId();

		waitingService.cancelWaiting(id);

		WaitingEntity cancelledEntity = waitingRepository.findById(id).orElseThrow();

		Assertions.assertThat(cancelledEntity.getWaitingStatus()).isEqualTo(WaitingStatus.CANCELED);
	}

}