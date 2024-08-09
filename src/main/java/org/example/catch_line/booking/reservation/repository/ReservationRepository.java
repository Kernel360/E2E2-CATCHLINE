package org.example.catch_line.booking.reservation.repository;

import java.util.List;

import org.example.catch_line.common.constant.Status;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

	List<ReservationEntity> findByMemberMemberId(Long memberId);

	List<ReservationEntity> findByMemberMemberIdAndStatus(Long memberId, Status status);


}
