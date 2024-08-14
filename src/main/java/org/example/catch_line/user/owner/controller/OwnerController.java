package org.example.catch_line.user.owner.controller;

import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.common.constant.Role;
import org.example.catch_line.common.constant.SessionConst;
import org.example.catch_line.exception.phone.InvalidPhoneNumberException;
import org.example.catch_line.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.dto.RestaurantUpdateRequest;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.restaurant.service.RestaurantService;
import org.example.catch_line.user.owner.service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {

	private final RestaurantService restaurantService;

	private final OwnerService ownerService;

	@GetMapping
	public String viewOwnerPage(
		HttpSession httpSession,
		Model model
	) {

		boolean isLoggedIn = httpSession.getAttribute(SessionConst.OWNER_ID) != null; // "user" 세션 속성으로 로그인 상태 확인
		model.addAttribute("isLoggedIn", isLoggedIn);

		return "owner/owner";
	}

	@GetMapping("/restaurants")
	public String createRestaurantForm(Model model) {
		model.addAttribute("request",
			new RestaurantCreateRequest("", "", "", "", FoodType.KOREAN, ServiceType.WAITING));
		return "owner/createRestaurant";
	}

	@PostMapping("/restaurants")
	public String createRestaurant(@ModelAttribute("request") RestaurantCreateRequest request,
		BindingResult bindingResult, HttpSession session) {

		Long ownerId = SessionUtils.getOwnerId(session);
		if (bindingResult.hasErrors()) {
			log.info("error = {}", bindingResult);
			return "owner/createRestaurant";
		}

		try {
			ownerService.createRestaurant(request, ownerId);
		} catch (InvalidPhoneNumberException e) {
			return invalidPhoneNumberException(e, bindingResult);
		}

		return "redirect:/owner";
	}

	@GetMapping("/restaurants/list")
	public String showRestaurantListPage(HttpSession session) {
		Long memberId = SessionUtils.getMemberId(session);
		Role role = SessionUtils.getRole(session);

		return "owner/restaurantList";
	}

	@GetMapping("/restaurants/{restaurantId}")
	public String updateRestaurantForm(@PathVariable Long restaurantId, Model model) {
		RestaurantResponse restaurant = restaurantService.findRestaurant(restaurantId);
		model.addAttribute("restaurant", restaurant);
		return "owner/updateRestaurant";
	}

	@PostMapping("/restaurants/{restaurantId}")
	public String updateRestaurant(@PathVariable Long restaurantId,
		@ModelAttribute("restaurant") RestaurantUpdateRequest request, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "owner/updateRestaurant";
		}

		restaurantService.updateRestaurant(restaurantId, request);

		return "redirect:/restaurants/" + restaurantId;
	}

	private String invalidPhoneNumberException(Exception e, BindingResult bindingResult) {
		bindingResult.rejectValue("phoneNumber", null, e.getMessage());
		return "owner/createRestaurant";
	}
}
