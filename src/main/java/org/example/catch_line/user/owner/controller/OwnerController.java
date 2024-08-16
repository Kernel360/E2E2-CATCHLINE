package org.example.catch_line.user.owner.controller;

import java.math.BigDecimal;
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
import org.example.catch_line.menu.service.MenuService;
import org.example.catch_line.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.restaurant.model.dto.RestaurantHourResponse;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.dto.RestaurantUpdateRequest;
import org.example.catch_line.restaurant.model.entity.RestaurantImageEntity;
import org.example.catch_line.restaurant.model.entity.constant.DayOfWeeks;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.restaurant.service.RestaurantHourService;
import org.example.catch_line.restaurant.service.RestaurantImageService;
import org.example.catch_line.restaurant.service.RestaurantService;
import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.service.ReviewService;
import org.example.catch_line.user.owner.service.OwnerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	private final ReviewService reviewService;

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

	// @GetMapping("/restaurants/history")
	// public String showHistory(HttpSession session, Model model) {
	// 	RestaurantResponse restaurant = getRestaurantResponse(session);
	//
	// 	List<HistoryResponse> historyResponses = ownerService.findHistoryByRestaurantId(restaurant.getRestaurantId());
	// 	model.addAttribute("history",historyResponses);
	//
	// 	return "owner/history";
	//
	// }

	@GetMapping("/restaurants/list/{restaurantId}/reviews")
	public String getReviews(@PathVariable Long restaurantId, Model model) {

		List<ReviewResponse> reviewList = reviewService.getRestaurantReviewList(restaurantId);
		BigDecimal averageRating = reviewService.getAverageRating(restaurantId).getRating();
		model.addAttribute("averageRating",averageRating);
		model.addAttribute("reviewList",reviewList);
		return "owner/reviews";
	}

	@GetMapping("/restaurants/list")
	public String showRestaurantListPage(HttpSession session, Model model) {
		List<RestaurantResponse> restaurantResponseList = getRestaurantResponseList(session);
		model.addAttribute("restaurantList", restaurantResponseList);

		return "owner/restaurantList";
	}

	@GetMapping("/restaurants/list/{restaurantId}")
	public String showRestaurant(@PathVariable Long restaurantId, Model model) {
		DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();
		DayOfWeeks dayOfWeek = DayOfWeeks.from(currentDayOfWeek);

		RestaurantResponse restaurant = restaurantService.findRestaurant(restaurantId);
		List<RestaurantHourResponse> restaurantHours = restaurantHourService.getAllRestaurantHours(restaurantId);
		RestaurantHourResponse hourResponse = restaurantHourService.getRestaurantHour(restaurantId, dayOfWeek);

		String x = String.valueOf(restaurant.getLongitude()); // 경도 == x 좌표
		String y = String.valueOf(restaurant.getLatitude()); // 위도 == y 좌표

		KakaoAddressResponse kakaoAddressResponse = kakaoAddressService.coordinateToAddress(x, y);
		KakaoAddressResponse.Document document = kakaoAddressResponse.getDocuments().get(0);

		List<RestaurantImageEntity> imageList = restaurantImageService.getImageList(restaurantId);

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("restaurantHours", restaurantHours);
		model.addAttribute("hourResponse", hourResponse);
		model.addAttribute("imageList", imageList);
		model.addAttribute("document", document);
		model.addAttribute("jsKey", jsKey);
		model.addAttribute("dayOfWeek", dayOfWeek.getDescription());

		return "owner/restaurant";
	}

	@GetMapping("restaurants/list/{restaurantId}/menus")
	public String getMenus(@PathVariable Long restaurantId,Model model) {

		List<MenuResponse> restaurantMenuList = menuService.getRestaurantMenuList(restaurantId);

		model.addAttribute("jsKey",jsKey);
		model.addAttribute("restaurantMenuList",restaurantMenuList);

		return "owner/menus";
	}

	@PutMapping("/restaurants/list/{restaurantId}/menus/menu")
	public String updateMenu(@PathVariable Long restaurantId, @RequestParam Long menuId, @ModelAttribute MenuRequest menuRequest, Model model) {

		menuService.updateRestaurantMenu(restaurantId,menuId,menuRequest);

		List<MenuResponse> menuResponseList = menuService.getRestaurantMenuList(restaurantId);
		model.addAttribute("restaurantMenuList",menuResponseList);

		return "redirect:/owner/restaurants/list/" + restaurantId + "/menus";
	}

	@DeleteMapping("/restaurants/list/{restaurantId}/menus/menu")
	public String deleteMenu(@PathVariable Long restaurantId,@RequestParam Long menuId) {

		menuService.deleteRestaurantMenu(menuId);
		return "redirect:/owner/restaurants/list/" + restaurantId + "/menus";
	}



	@PostMapping("/restaurants/list/{restaurantId}/menus/menu")
	public String addMenu(@PathVariable Long restaurantId, @ModelAttribute MenuRequest menuRequest, Model model) {


		menuService.createRestaurantMenu(restaurantId,menuRequest);

		List<MenuResponse> restaurantMenuList = menuService.getRestaurantMenuList(restaurantId);
		model.addAttribute("restaurantMenuList", restaurantMenuList);
		return "redirect:/owner/restaurants/list/menus";
	}



	private List<RestaurantResponse> getRestaurantResponseList(HttpSession session) {
		Long ownerId = SessionUtils.getOwnerId(session);

		List<RestaurantResponse> restaurantList = ownerService.findAllRestaurantByOwnerId(ownerId);
		return restaurantList;
	}



	private String invalidPhoneNumberException(Exception e, BindingResult bindingResult) {
		bindingResult.rejectValue("phoneNumber", null, e.getMessage());
		return "owner/createRestaurant";
	}




}
