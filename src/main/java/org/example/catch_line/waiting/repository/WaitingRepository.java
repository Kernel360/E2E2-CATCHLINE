package org.example.catch_line.waiting.repository;

import org.example.catch_line.waiting.model.entity.WaitingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingRepository extends JpaRepository<WaitingEntity, Long> {
}
