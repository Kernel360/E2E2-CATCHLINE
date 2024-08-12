package org.example.catch_line.menu.model.mapper;

import org.example.catch_line.menu.model.dto.MenuRequest;
import org.example.catch_line.menu.model.dto.MenuResponse;
import org.example.catch_line.menu.model.entity.MenuEntity;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;

public class MenuMapper {

    private static final NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);

    public static MenuResponse entityToResponse(MenuEntity menuEntity) {
        return MenuResponse.builder()
                .menuId(menuEntity.getMenuId())
                .name(menuEntity.getName())
                .price(formatter.format(menuEntity.getPrice()))
                .build();
    }

    public static MenuEntity requestToEntity(MenuRequest menuRequest, RestaurantEntity restaurantEntity) {
        return MenuEntity.builder()
                .name(menuRequest.getName())
                .price(menuRequest.getPrice())
                .restaurant(restaurantEntity)
                .build();
    }
}
