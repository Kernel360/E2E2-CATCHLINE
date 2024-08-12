package org.example.catch_line.menu.service;


import org.assertj.core.api.Assertions;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.member.model.vo.PhoneNumber;
import org.example.catch_line.menu.model.dto.MenuRequest;
import org.example.catch_line.menu.model.dto.MenuResponse;
import org.example.catch_line.menu.model.entity.MenuEntity;
import org.example.catch_line.menu.model.mapper.MenuMapper;
import org.example.catch_line.menu.model.validation.MenuValidator;
import org.example.catch_line.menu.repository.MenuRepository;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.restaurant.validation.RestaurantValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock MenuRepository menuRepository;
    @Mock RestaurantValidator restaurantValidator;
    @Mock MenuValidator menuValidator;
    @InjectMocks MenuService menuService;

    @Test
    @DisplayName("식당 메뉴 전체 조회 테스트")
    void restaurant_menu_list_test() {
        // given
        Long restaurantId = 1L;

        when(menuRepository.findAllByRestaurantRestaurantId(restaurantId)).thenReturn(getMenuList());

        // when
        List<MenuResponse> menuList = menuService.getRestaurantMenuList(restaurantId);

        // then
        assertThat(menuList).isNotNull();
        assertThat(menuList.size()).isGreaterThan(0);
        assertThat(menuList.size()).isEqualTo(3);
        assertThat(menuList.get(0).getName()).isEqualTo("삼겹살");
    }

    @Test
    @DisplayName("메뉴 추가 테스트")
    void restaurant_create_menu_test() {
        // given
        Long restaurantId = 1L;
        MenuRequest request = getMenuRequest();
        RestaurantEntity restaurantEntity = getRestaurantEntity();
        MenuEntity menuEntity = MenuMapper.requestToEntity(request, restaurantEntity);

        when(restaurantValidator.checkIfRestaurantPresent(restaurantId)).thenReturn(restaurantEntity);
        when(menuRepository.save(any(MenuEntity.class))).thenReturn(menuEntity);

        // when
        MenuResponse response = menuService.createRestaurantMenu(restaurantId, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(menuEntity.getName());
        assertThat(response.getPrice()).isEqualTo("10,000");
    }

    @Test
    @DisplayName("메뉴 수정 테스트")
    void restaurant_update_menu_test() {
        // given
        Long restaurantId = 1L;
        Long menuId = 1L;
        MenuRequest request = getMenuUpdateRequest();
        RestaurantEntity restaurantEntity = getRestaurantEntity();
        MenuEntity menuEntity = MenuMapper.requestToEntity(request, restaurantEntity);

        when(restaurantValidator.checkIfRestaurantPresent(restaurantId)).thenReturn(restaurantEntity);
        when(menuValidator.checkIfMenuPresent(menuId)).thenReturn(menuEntity);

        // when
        menuService.updateRestaurantMenu(restaurantId, menuId, request);

        // then
        assertThat(request.getName()).isEqualTo(menuEntity.getName());
        assertThat(request.getPrice()).isEqualTo(menuEntity.getPrice());
        assertThat(menuEntity.getPrice()).isEqualTo(20000L);
    }

    @Test
    @DisplayName("메뉴 삭제 테스트")
    void restaurant_delete_menu_test() {
        // given
        Long menuId = 1L;

        // when
        menuService.deleteRestaurantMenu(menuId);

        // then
        verify(menuRepository, times(1)).deleteById(menuId);
    }

    private RestaurantEntity getRestaurantEntity() {
        return RestaurantEntity.builder()
                .name("새마을식당")
                .description("백종원의 새마을식당")
                .phoneNumber(new PhoneNumber("02-1234-1234"))
                .rating(new Rating(BigDecimal.ZERO))
                .serviceType(ServiceType.WAITING)
                .foodType(FoodType.KOREAN)
                .build();
    }

    private MenuRequest getMenuRequest() {
        return MenuRequest.builder()
                .name("불고기")
                .price(10000L)
                .build();
    }

    private MenuRequest getMenuUpdateRequest() {
        return MenuRequest.builder()
                .name("불고기")
                .price(20000L)
                .build();
    }

    private List<MenuEntity> getMenuList() {
        List<MenuEntity> menuList = new ArrayList<>();

        MenuEntity menu1 = MenuEntity.builder()
                .name("삼겹살")
                .price(15000L)
                .restaurant(getRestaurantEntity())
                .build();

        MenuEntity menu2 = MenuEntity.builder()
                .name("항정살")
                .price(16000L)
                .restaurant(getRestaurantEntity())
                .build();

        MenuEntity menu3 = MenuEntity.builder()
                .name("갈비")
                .price(18000L)
                .restaurant(getRestaurantEntity())
                .build();

        menuList.add(menu1); menuList.add(menu2); menuList.add(menu3);
        return menuList;
    }

}