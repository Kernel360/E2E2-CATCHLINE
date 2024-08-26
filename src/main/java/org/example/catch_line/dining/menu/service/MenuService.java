package org.example.catch_line.dining.menu.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.dining.menu.repository.MenuRepository;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.menu.model.dto.MenuRequest;
import org.example.catch_line.dining.menu.model.dto.MenuResponse;
import org.example.catch_line.dining.menu.model.entity.MenuEntity;
import org.example.catch_line.dining.menu.model.mapper.MenuMapper;
import org.example.catch_line.dining.menu.model.validation.MenuValidator;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantValidator restaurantValidator;
    private final MenuValidator menuValidator;
    private final MenuMapper menuMapper;

    public List<MenuResponse> getRestaurantMenuList(Long restaurantId) {
        List<MenuEntity> menuList = menuRepository.findAllByRestaurantRestaurantId(restaurantId);

        return menuList.stream()
                .map(menuMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public MenuResponse createRestaurantMenu(Long restaurantId, MenuRequest menuRequest) {
        RestaurantEntity restaurantEntity = restaurantValidator.checkIfRestaurantPresent(restaurantId);
        MenuEntity menuEntity = menuMapper.requestToEntity(menuRequest, restaurantEntity);
        MenuEntity savedEntity = menuRepository.save(menuEntity);
        return menuMapper.entityToResponse(savedEntity);
    }

    @Transactional
    public void updateRestaurantMenu(Long restaurantId, Long menuId, MenuRequest menuRequest) {
        restaurantValidator.checkIfRestaurantPresent(restaurantId);
        MenuEntity menuEntity = menuValidator.checkIfMenuPresent(menuId);
        menuEntity.updateMenu(menuRequest.getName(), menuRequest.getPrice());
    }

    public void deleteRestaurantMenu(Long menuId) {
        menuRepository.deleteById(menuId);
    }

}
