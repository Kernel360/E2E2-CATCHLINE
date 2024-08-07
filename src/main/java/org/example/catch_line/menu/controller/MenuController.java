package org.example.catch_line.menu.controller;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.menu.model.dto.MenuResponse;
import org.example.catch_line.menu.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/restaurants/{restaurantId}/menus")
    public String getRestaurantMenus(Model model, @PathVariable Long restaurantId) {
        List<MenuResponse> menuList = menuService.getRestaurantMenu(restaurantId);
        model.addAttribute("menuList", menuList);
        model.addAttribute("restaurantId", restaurantId);
        return "menu/menu";
    }
}
