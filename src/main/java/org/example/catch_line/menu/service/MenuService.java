package org.example.catch_line.menu.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.menu.model.dto.MenuRequest;
import org.example.catch_line.menu.model.dto.MenuResponse;
import org.example.catch_line.menu.model.entity.MenuEntity;
import org.example.catch_line.menu.model.mapper.MenuMapper;
import org.example.catch_line.menu.repository.MenuRepository;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.validation.RestaurantValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantValidator restaurantValidator;

    // 식당에 대한 메뉴 조회
    public List<MenuResponse> getRestaurantMenuList(Long restaurantId) {
        List<MenuEntity> menuList = menuRepository.findAllByRestaurantRestaurantId(restaurantId);

        return menuList.stream()
                .map(entity -> MenuMapper.entityToResponse(entity)) // .map(menuMapper::entityToResponse)
                .collect(Collectors.toList());
    }


    // TODO: 이 부분은 아마 사장님 구현할 때 사용
    // 메뉴 상세 조회
    // 메뉴 추가
    public MenuResponse createRestaurantMenu(Long restaurantId, MenuRequest menuRequest) {
        RestaurantEntity restaurantEntity = restaurantValidator.checkIfRestaurantPresent(restaurantId);
        MenuEntity menuEntity = requestToEntity(menuRequest, restaurantEntity);
        MenuEntity savedEntity = menuRepository.save(menuEntity);
        return MenuMapper.entityToResponse(savedEntity);
    }

    // 메뉴 수정
    public void updateRestaurantMenu(Long restaurantId, MenuRequest menuRequest) {
        RestaurantEntity restaurantEntity = restaurantValidator.checkIfRestaurantPresent(restaurantId);
        MenuEntity menuEntity = requestToEntity(menuRequest, restaurantEntity);
        menuEntity.updateMenu(menuRequest.getName(), menuRequest.getPrice());
        // menuRepository.save(menuEntity);
    }

    // 메뉴 삭제
    public void deleteRestaurantMenu(Long restaurantId) {
        menuRepository.deleteById(restaurantId);
    }

    private MenuEntity requestToEntity(MenuRequest menuRequest, RestaurantEntity restaurantEntity) {
        return MenuEntity.builder()
                .name(menuRequest.getName())
                .price(menuRequest.getPrice())
                .restaurant(restaurantEntity)
                .build();
    }
}
