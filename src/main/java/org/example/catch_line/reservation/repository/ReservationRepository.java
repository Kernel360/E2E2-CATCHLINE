package org.example.catch_line.reservation.repository;

import java.util.List;

import org.example.catch_line.reservation.model.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

	List<ReservationEntity> findByMemberMemberId(Long memberId);
}
