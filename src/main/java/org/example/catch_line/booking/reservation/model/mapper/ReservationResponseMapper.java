package org.example.catch_line.booking.reservation.model.mapper;

import org.example.catch_line.booking.reservation.model.dto.ReservationResponse;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.springframework.stereotype.Service;

@Service
public class ReservationResponseMapper {

	public ReservationResponse convertToResponse(ReservationEntity entity) {
		return ReservationResponse.builder()
			.reservationId(entity.getReservationId())
			.memberCount(entity.getMemberCount())
			.status(entity.getStatus())
			.reservationDate(entity.getReservationDate())
			.createdAt(entity.getCreatedAt())
			.modifiedAt(entity.getModifiedAt())
			.build();
	}

}
