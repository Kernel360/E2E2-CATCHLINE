package org.example.catch_line.menu.controller;

import org.example.catch_line.menu.model.dto.MenuResponse;
import org.example.catch_line.menu.service.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class MenuControllerTest {

    @Autowired MockMvc mockMvc;

    @MockBean MenuService menuService;

    @Test
    @DisplayName("식당 메뉴 전체 조회 테스트")
    void restaurant_menus_test() throws Exception {
        // given
        Long restaurantId = 1L;
        List<MenuResponse> menuList = getMenuList();

        when(menuService.getRestaurantMenuList(restaurantId)).thenReturn(menuList);

        // when
        // then
        mockMvc.perform(get("/restaurants/{restaurantId}/menus", restaurantId))
                .andExpect(status().isOk())
                .andExpect(view().name("menu/menus"))
                .andExpect(model().attribute("menuList", menuList))
                .andExpect(model().attribute("restaurantId", restaurantId));
    }

    private List<MenuResponse> getMenuList() {
        List<MenuResponse> list = new ArrayList<>();

        MenuResponse menu1 = MenuResponse.builder()
                .menuId(1L)
                .name("돈까스")
                .price("15000")
                .build();

        MenuResponse menu2 = MenuResponse.builder()
                .menuId(2L)
                .name("우동")
                .price("8000")
                .build();

        MenuResponse menu3 = MenuResponse.builder()
                .menuId(3L)
                .name("알밥")
                .price("9000")
                .build();

        list.add(menu1); list.add(menu2); list.add(menu3);
        return list;
    }

}