package org.example.catch_line.reservation.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.example.catch_line.common.constant.Status;
import org.example.catch_line.reservation.model.dto.ReservationRequest;
import org.example.catch_line.reservation.model.dto.ReservationResponse;
import org.example.catch_line.reservation.model.entity.ReservationEntity;
import org.example.catch_line.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ReservationServiceTest {

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private ReservationRepository reservationRepository;

	@Test
	@DisplayName("예약 추가 테스트")
	void testAddReserve() {
		ReservationRequest request = ReservationRequest.builder()
			.memberCount(4)
			.reservationDate(LocalDateTime.now().plusDays(1))
			.status(Status.SCHEDULED)
			.build();

		ReservationResponse response = reservationService.addReserve(request);

		assertThat(response).isNotNull();
		assertThat(response.getMemberCount()).isEqualTo(4);
		assertThat(response.getStatus()).isEqualTo(Status.SCHEDULED);
		assertThat(response.getReservationDate()).isEqualTo(request.getReservationDate());
	}

	@Test
	@DisplayName("모든 예약 조회 테스트")
	void testGetAllReservation() {
		ReservationRequest request1 = ReservationRequest.builder()
			.memberCount(2)
			.reservationDate(LocalDateTime.now().plusDays(1))
			.status(Status.SCHEDULED)
			.build();
		ReservationRequest request2 = ReservationRequest.builder()
			.memberCount(7)
			.reservationDate(LocalDateTime.now().plusDays(7))
			.status(Status.SCHEDULED)
			.build();

		reservationService.addReserve(request1);

		reservationService.addReserve(request2);

		List<ReservationResponse> responses = reservationService.getAllReservation();

		assertThat(responses).hasSize(2);
	}

	@Test
	@DisplayName("예약 ID로 조회 테스트")
	void testGetReservationById() {
		ReservationRequest request = ReservationRequest.builder()
			.memberCount(4)
			.reservationDate(LocalDateTime.now().plusDays(2))
			.status(Status.SCHEDULED)
			.build();

		ReservationResponse addedResponse = reservationService.addReserve(request);
		Long id = addedResponse.getReservationId();

		ReservationResponse response = reservationService.getReservationById(id);

		assertThat(response).isNotNull();
		assertThat(response.getReservationId()).isEqualTo(id);
		assertThat(response.getMemberCount()).isEqualTo(request.getMemberCount());
		assertThat(response.getReservationDate()).isEqualTo(request.getReservationDate());
	}

	@Test
	@DisplayName("예약 취소 테스트")
	void testCancelReservation() {
		ReservationRequest request = ReservationRequest.builder()
			.memberCount(4)
			.reservationDate(LocalDateTime.now().plusDays(2))
			.status(Status.SCHEDULED)
			.build();

		ReservationResponse response = reservationService.addReserve(request);
		Long id = response.getReservationId();

		reservationService.cancelReservation(id);

		ReservationEntity cancelledEntity = reservationRepository.findById(id).orElseThrow();
		assertThat(cancelledEntity.getStatus()).isEqualTo(Status.CANCELED);
	}

	@Test
	@DisplayName("예약 수정 테스트")
	void testEditReservation() {
		ReservationRequest request = ReservationRequest.builder()
			.memberCount(4)
			.reservationDate(LocalDateTime.now().plusDays(2))
			.status(Status.SCHEDULED)
			.build();

		ReservationResponse response = reservationService.addReserve(request);
		Long id = response.getReservationId();

		ReservationRequest updatedRequest = ReservationRequest.builder()
			.memberCount(5)
			.reservationDate(LocalDateTime.now().plusDays(3))
			.status(Status.SCHEDULED)
			.build();

		reservationService.editReservation(id, updatedRequest);

		ReservationEntity updatedEntity = reservationRepository.findById(id).orElseThrow();
		assertThat(updatedEntity.getMemberCount()).isEqualTo(5);
		assertThat(updatedEntity.getReservationDate()).isEqualTo(updatedRequest.getReservationDate());
		assertThat(updatedEntity.getStatus()).isEqualTo(updatedRequest.getStatus());
	}
}
