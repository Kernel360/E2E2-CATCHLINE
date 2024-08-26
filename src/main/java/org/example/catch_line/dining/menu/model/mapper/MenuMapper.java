package org.example.catch_line.dining.menu.model.mapper;

import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.menu.model.dto.MenuRequest;
import org.example.catch_line.dining.menu.model.dto.MenuResponse;
import org.example.catch_line.dining.menu.model.entity.MenuEntity;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Locale;

@Component
public class MenuMapper {

    private static final NumberFormat FORMATTER = NumberFormat.getNumberInstance(Locale.KOREA);

    public MenuResponse entityToResponse(MenuEntity menuEntity) {
        return MenuResponse.builder()
                .menuId(menuEntity.getMenuId())
                .name(menuEntity.getName())
                .price(FORMATTER.format(menuEntity.getPrice()))
                .build();
    }

    public MenuEntity requestToEntity(MenuRequest menuRequest, RestaurantEntity restaurantEntity) {
        return new MenuEntity(menuRequest.getName(), menuRequest.getPrice(), restaurantEntity);
    }
}
