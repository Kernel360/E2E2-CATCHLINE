package org.example.catch_line.dining.menu.model.validation;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.dining.menu.repository.MenuRepository;
import org.example.catch_line.dining.menu.model.entity.MenuEntity;
import org.example.catch_line.exception.dining.MenuNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuValidator {

    private final MenuRepository menuRepository;

    public MenuEntity checkIfMenuPresent(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException(menuId));
    }
}
