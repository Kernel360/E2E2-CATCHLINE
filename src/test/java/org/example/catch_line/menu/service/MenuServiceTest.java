package org.example.catch_line.dining.menu.service;

import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.dining.menu.model.dto.MenuRequest;
import org.example.catch_line.dining.menu.model.dto.MenuResponse;
import org.example.catch_line.dining.menu.model.entity.MenuEntity;
import org.example.catch_line.dining.menu.model.mapper.MenuMapper;
import org.example.catch_line.dining.menu.model.validation.MenuValidator;
import org.example.catch_line.dining.menu.repository.MenuRepository;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RestaurantValidator restaurantValidator;

    @Mock
    private MenuValidator menuValidator;

    @Mock
    private MenuMapper menuMapper;

    @InjectMocks
    private MenuService menuService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private RestaurantEntity restaurantEntity;
    private MenuEntity menuEntity;

    @BeforeEach
    void setUp() {
        OwnerEntity owner = new OwnerEntity("qwer1111", "철수", new Password(passwordEncoder.encode("123qwe!@Q")), new PhoneNumber("010-1111-1111"));

        restaurantEntity = new RestaurantEntity("새마을식당", "백종원의 새마을식당", new Rating(BigDecimal.ZERO), new PhoneNumber("010-2111-1111"),
                FoodType.KOREAN, ServiceType.RESERVATION, owner, new BigDecimal("37.50828251273050000000"), new BigDecimal("127.06548046585200000000"));

        menuEntity = mock(MenuEntity.class);  // MenuEntity 객체를 Mock으로 생성
    }

    @Test
    @DisplayName("레스토랑 메뉴 목록 조회 테스트")
    void getRestaurantMenuList_success() {
        when(menuRepository.findAllByRestaurantRestaurantId(anyLong()))
                .thenReturn(Arrays.asList(menuEntity));

        MenuResponse menuResponse = new MenuResponse(1L, "Menu Name", "10000");
        when(menuMapper.entityToResponse(any(MenuEntity.class))).thenReturn(menuResponse);

        List<MenuResponse> result = menuService.getRestaurantMenuList(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Menu Name", result.get(0).getName());
        verify(menuRepository, times(1)).findAllByRestaurantRestaurantId(anyLong());
    }

    @Test
    @DisplayName("레스토랑 메뉴 생성 테스트")
    void createRestaurantMenu_success() {
        when(restaurantValidator.checkIfRestaurantPresent(anyLong())).thenReturn(restaurantEntity);
        when(menuMapper.requestToEntity(any(MenuRequest.class), any(RestaurantEntity.class))).thenReturn(menuEntity);
        when(menuRepository.save(any(MenuEntity.class))).thenReturn(menuEntity);

        MenuResponse menuResponse = new MenuResponse(1L, "Menu Name", "10000");
        when(menuMapper.entityToResponse(any(MenuEntity.class))).thenReturn(menuResponse);

        MenuRequest menuRequest = new MenuRequest("Menu Name", 10000L);
        MenuResponse result = menuService.createRestaurantMenu(1L, menuRequest);

        assertNotNull(result);
        assertEquals("Menu Name", result.getName());
        verify(menuRepository, times(1)).save(any(MenuEntity.class));
    }

    @Test
    @DisplayName("레스토랑 메뉴 수정 테스트")
    void updateRestaurantMenu_success() {
        when(restaurantValidator.checkIfRestaurantPresent(anyLong())).thenReturn(restaurantEntity);
        when(menuValidator.checkIfMenuPresent(anyLong())).thenReturn(menuEntity);

        MenuRequest menuRequest = new MenuRequest("Updated Menu", 15000L);
        menuService.updateRestaurantMenu(1L, 1L, menuRequest);

        verify(menuEntity, times(1)).updateMenu("Updated Menu", 15000L);
    }

    @Test
    @DisplayName("레스토랑 메뉴 삭제 테스트")
    void deleteRestaurantMenu_success() {
        doNothing().when(menuRepository).deleteById(anyLong());

        menuService.deleteRestaurantMenu(1L);

        verify(menuRepository, times(1)).deleteById(anyLong());
    }
}
