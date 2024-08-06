package org.example.catch_line.menu.model.mapper;

import org.example.catch_line.menu.model.dto.MenuResponse;
import org.example.catch_line.menu.model.entity.MenuEntity;
import org.springframework.stereotype.Service;

@Service
public class MenuMapper {

    public MenuResponse entityToResponse(MenuEntity menuEntity) {
        return MenuResponse.builder()
                .menuId(menuEntity.getMenuId())
                .name(menuEntity.getName())
                .price(menuEntity.getPrice())
                .build();
    }
}
