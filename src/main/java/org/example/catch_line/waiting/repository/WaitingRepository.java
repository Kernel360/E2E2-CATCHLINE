package org.example.catch_line.waiting.repository;

import org.example.catch_line.waiting.model.entity.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {
}
