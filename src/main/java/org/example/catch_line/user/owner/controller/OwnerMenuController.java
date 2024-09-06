package org.example.catch_line.user.owner.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.dining.menu.model.dto.MenuRequest;
import org.example.catch_line.dining.menu.model.dto.MenuResponse;
import org.example.catch_line.dining.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerMenuController {

    private final MenuService menuService;

    @Value("${kakao.maps.js-key}")
    private String jsKey;

    @GetMapping("/restaurants/{restaurantId}/menus")
    public String getMenus(@PathVariable Long restaurantId, Model model) {
        List<MenuResponse> restaurantMenuList = menuService.getRestaurantMenuList(restaurantId);

        model.addAttribute("jsKey", jsKey);
        model.addAttribute("restaurantMenuList", restaurantMenuList);
        return "owner/menus";
    }

    @PostMapping("/restaurants/{restaurantId}/menus")
    public String addMenu(@PathVariable Long restaurantId, @Valid @ModelAttribute MenuRequest menuRequest, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage","입력한 값이 유효하지 않습니다");
            return "redirect:/owner/restaurants/{restaurantId}/menus";
        }

        menuService.createRestaurantMenu(restaurantId, menuRequest);
        List<MenuResponse> restaurantMenuList = menuService.getRestaurantMenuList(restaurantId);

        model.addAttribute("restaurantMenuList", restaurantMenuList);
        return "redirect:/owner/restaurants/{restaurantId}/menus";
    }

    @PutMapping("/restaurants/{restaurantId}/menus")
    public String updateMenu(@PathVariable Long restaurantId, @RequestParam Long menuId,
                             @Valid @ModelAttribute MenuRequest menuRequest, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "입력한 값이 유효하지 않습니다");
            return "redirect:/owner/restaurants/{restaurantId}/menus";
        }

        menuService.updateRestaurantMenu(restaurantId, menuId, menuRequest);
        List<MenuResponse> menuResponseList = menuService.getRestaurantMenuList(restaurantId);

        model.addAttribute("restaurantMenuList", menuResponseList);
        return "redirect:/owner/restaurants/{restaurantId}/menus";
    }

    @DeleteMapping("/restaurants/{restaurantId}/menus")
    public String deleteMenu(@PathVariable Long restaurantId, @RequestParam Long menuId) {
        menuService.deleteRestaurantMenu(menuId);
        return "redirect:/owner/restaurants/{restaurantId}/menus";
    }

}
