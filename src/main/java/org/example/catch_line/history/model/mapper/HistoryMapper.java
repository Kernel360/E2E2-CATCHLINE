package org.example.catch_line.history.model.mapper;

import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.springframework.stereotype.Component;

@Component
public class HistoryMapper {

    //WaitingEntity -> HistoryResponse
    public HistoryResponse entityToHistoryResponse(WaitingEntity waiting, int waitingRegistrationId, int myWaitingPosition) {
        return HistoryResponse.builder()
                .restaurantId(waiting.getRestaurant().getRestaurantId())
                .waitingId(waiting.getWaitingId())
                .memberCount(waiting.getMemberCount())
                .restaurantName(waiting.getRestaurant().getName())
                .status(waiting.getStatus())
                .waitingType(waiting.getWaitingType())
                .serviceType(waiting.getRestaurant().getServiceType())
                .createdAt(waiting.getCreatedAt())
                .modifiedAt(waiting.getModifiedAt())
                .waitingRegistrationId(waitingRegistrationId)
                .myWaitingPosition(myWaitingPosition)
                .build();
    }

    //ReservationEntity -> HistoryResponse
    public HistoryResponse reservationToHistoryResponse(ReservationEntity entity) {
        return HistoryResponse.builder()
                .restaurantId(entity.getRestaurant().getRestaurantId())
                .reservationId(entity.getReservationId())
                .memberCount(entity.getMemberCount())
                .restaurantName(entity.getRestaurant().getName())
                .status(entity.getStatus())
                .reservationDate(entity.getReservationDate())
                .serviceType(entity.getRestaurant().getServiceType())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .waitingRegistrationId(1) // 항상 1로 설정
                .myWaitingPosition(1) // 항상 1로 설정
                .build();
    }
}
