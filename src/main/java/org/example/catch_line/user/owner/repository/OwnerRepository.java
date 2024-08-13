package org.example.catch_line.user.owner.repository;

import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {

    Optional<OwnerEntity> findByLoginId(String loginId);
}
