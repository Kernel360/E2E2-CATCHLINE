package org.example.catch_line.waiting.repository;

import java.util.List;

import org.example.catch_line.common.constant.Status;
import org.example.catch_line.waiting.model.entity.WaitingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingRepository extends JpaRepository<WaitingEntity, Long> {
	List<WaitingEntity> findByMemberMemberId(Long memberId);

	List<WaitingEntity> findByMemberMemberIdAndStatus(Long memberId, Status status);
}
