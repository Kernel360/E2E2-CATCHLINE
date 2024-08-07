package org.example.catch_line.menu.controller;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.menu.model.dto.MenuResponse;
import org.example.catch_line.menu.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurants/{restaurantId}/menus")
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public String getRestaurantMenus(Model model, @PathVariable Long restaurantId) {
        List<MenuResponse> menuList = menuService.getRestaurantMenuList(restaurantId);
        model.addAttribute("menuList", menuList);
        model.addAttribute("restaurantId", restaurantId);
        return "menu/menus";
    }
}
