package org.example.catch_line.menu.model.validation;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.menu.model.entity.MenuEntity;
import org.example.catch_line.menu.repository.MenuRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuValidator {

    private final MenuRepository menuRepository;

    public MenuEntity checkIfMenuPresent(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 메뉴가 없습니다."));
    }
}
