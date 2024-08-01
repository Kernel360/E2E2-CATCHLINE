package org.example.catch_line.menu.repository;

import org.example.catch_line.menu.model.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
}
