package org.example.catch_line.user.owner.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.common.constant.SessionConst;
import org.example.catch_line.common.kakao.model.dto.KakaoAddressResponse;
import org.example.catch_line.common.kakao.service.KakaoAddressService;
import org.example.catch_line.exception.phone.InvalidPhoneNumberException;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.menu.model.dto.MenuRequest;
import org.example.catch_line.menu.model.dto.MenuResponse;
import org.example.catch_line.menu.model.entity.MenuEntity;
import org.example.catch_line.menu.model.mapper.MenuMapper;
import org.example.catch_line.menu.repository.MenuRepository;
import org.example.catch_line.menu.service.MenuService;
import org.example.catch_line.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.restaurant.model.dto.RestaurantHourResponse;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.dto.RestaurantUpdateRequest;
import org.example.catch_line.restaurant.model.entity.RestaurantImageEntity;
import org.example.catch_line.restaurant.model.entity.constant.DayOfWeeks;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.restaurant.model.mapper.RestaurantMapper;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.example.catch_line.restaurant.service.RestaurantHourService;
import org.example.catch_line.restaurant.service.RestaurantImageService;
import org.example.catch_line.restaurant.service.RestaurantService;
import org.example.catch_line.user.owner.service.OwnerService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {

	private final RestaurantService restaurantService;
	private final MenuService menuService;
	private final OwnerService ownerService;
	private final RestaurantImageService restaurantImageService;
	private final KakaoAddressService kakaoAddressService;
	private final RestaurantHourService restaurantHourService;
	private final MenuRepository menuRepository;

	@Value("${kakao.maps.js-key}")
	private String jsKey;

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

	@GetMapping("/restaurants/history")
	public String showHistory(HttpSession session, Model model) {
		RestaurantResponse restaurant = getRestaurantResponse(session);

		List<HistoryResponse> historyResponses = ownerService.findHistoryByRestaurantId(restaurant.getRestaurantId());
		model.addAttribute("history",historyResponses);

		return "owner/history";

	}

	@GetMapping("/restaurants/list")
	public String showRestaurantListPage(HttpSession session, Model model) {
		RestaurantResponse restaurant = getRestaurantResponse(session);
		List<RestaurantHourResponse> restaurantHours = ownerService.findRestaurantHourByRestaurantId(
			restaurant.getRestaurantId());
		DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();
		DayOfWeeks dayOfWeek = DayOfWeeks.from(currentDayOfWeek);
		RestaurantHourResponse hourResponse = restaurantHourService.getRestaurantHour(restaurant.getRestaurantId(),
			dayOfWeek);

		String x = String.valueOf(restaurant.getLongitude()); // 경도 == x 좌표
		String y = String.valueOf(restaurant.getLatitude()); // 위도 == y 좌표

		KakaoAddressResponse kakaoAddressResponse = kakaoAddressService.coordinateToAddress(x, y);
		KakaoAddressResponse.Document document = kakaoAddressResponse.getDocuments().get(0);

		List<RestaurantImageEntity> imageList = restaurantImageService.getImageList(restaurant.getRestaurantId());

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("restaurantHours", restaurantHours);
		model.addAttribute("imageList", imageList);
		model.addAttribute("document", document);
		model.addAttribute("jsKey", jsKey);
		model.addAttribute("hourResponse", hourResponse);
		model.addAttribute("dayOfWeek", dayOfWeek.getDescription());

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

	@PutMapping("/restaurants/list/menus/menu")
	public String updateMenu(HttpSession session, @RequestParam Long menuId, @ModelAttribute MenuRequest menuRequest, Model model) {

		RestaurantResponse restaurant = getRestaurantResponse(session);

		menuService.updateRestaurantMenu(restaurant.getRestaurantId(),menuId,menuRequest);

		List<MenuResponse> menuResponseList = menuService.getRestaurantMenuList(restaurant.getRestaurantId());
		model.addAttribute("restaurantMenuList",menuResponseList);

		return "redirect:/owner/restaurants/list/menus";


	}

	@GetMapping("restaurants/list/menus")
	public String getMenus(Model model, HttpSession session) {
		RestaurantResponse restaurant = getRestaurantResponse(session);

		List<MenuResponse> restaurantMenuList = menuService.getRestaurantMenuList(restaurant.getRestaurantId());

		model.addAttribute("jsKey",jsKey);
		model.addAttribute("restaurantMenuList",restaurantMenuList);
		model.addAttribute("restaurantId",restaurant.getRestaurantId());
		return "owner/menus";
	}

	@PostMapping("/restaurants/list/menus/menu")
	public String addMenu(HttpSession session, @ModelAttribute MenuRequest menuRequest, Model model) {

		RestaurantResponse restaurant = getRestaurantResponse(session);
		menuService.createRestaurantMenu(restaurant.getRestaurantId(),menuRequest);

		List<MenuResponse> menuResponseList = menuService.getRestaurantMenuList(restaurant.getRestaurantId());
		List<MenuResponse> restaurantMenuList = menuService.getRestaurantMenuList(restaurant.getRestaurantId());
		model.addAttribute("menuList",menuResponseList);
		model.addAttribute("restaurantMenuList", restaurantMenuList);
		return "redirect:/owner/restaurants/list/menus";
	}



	private RestaurantResponse getRestaurantResponse(HttpSession session) {
		Long ownerId = SessionUtils.getOwnerId(session);

		RestaurantResponse restaurant = ownerService.findRestaurantByOwnerId(ownerId);
		return restaurant;
	}



	private String invalidPhoneNumberException(Exception e, BindingResult bindingResult) {
		bindingResult.rejectValue("phoneNumber", null, e.getMessage());
		return "owner/createRestaurant";
	}




}
