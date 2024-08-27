package org.example.catch_line.user.owner.controller;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.validation.Valid;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.service.ReservationService;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.service.WaitingService;
import org.example.catch_line.common.constant.DayOfWeeks;
import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.common.kakao.model.dto.KakaoAddressResponse;
import org.example.catch_line.common.kakao.service.KakaoAddressService;
import org.example.catch_line.common.session.SessionConst;
import org.example.catch_line.common.session.SessionUtils;
import org.example.catch_line.dining.menu.model.dto.MenuRequest;
import org.example.catch_line.dining.menu.model.dto.MenuResponse;
import org.example.catch_line.dining.menu.service.MenuService;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantHourRequest;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantHourResponse;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantUpdateRequest;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantImageEntity;
import org.example.catch_line.dining.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.dining.restaurant.service.RestaurantHourService;
import org.example.catch_line.dining.restaurant.service.RestaurantImageService;
import org.example.catch_line.dining.restaurant.service.RestaurantService;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.exception.booking.BookingErrorException;
import org.example.catch_line.exception.booking.HistoryException;
import org.example.catch_line.exception.phone.InvalidPhoneNumberException;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.history.service.HistoryService;
import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.service.ReviewService;
import org.example.catch_line.user.owner.service.OwnerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	private final HistoryService historyService;
	private final WaitingService waitingService;
	private final ReservationService reservationService;

	@Value("${kakao.maps.js-key}")
	private String jsKey;

	@GetMapping
	public String viewOwnerPage(HttpSession httpSession, Model model) {
		boolean isLoggedIn = Objects.nonNull(httpSession.getAttribute(SessionConst.OWNER_ID)); // "user" 세션 속성으로 로그인 상태 확인
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
	public String createRestaurant(@Valid @ModelAttribute("request") RestaurantCreateRequest request,
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
		RestaurantResponse restaurant = restaurantService.findRestaurant(null, restaurantId);
		model.addAttribute("restaurant", restaurant);
		return "owner/updateRestaurant";
	}

	@PostMapping("/restaurants/{restaurantId}")
	public String updateRestaurant(@PathVariable Long restaurantId, HttpSession session,
								   @Valid @ModelAttribute("restaurant") RestaurantUpdateRequest request, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "owner/updateRestaurant";
		}
		Long ownerId = SessionUtils.getOwnerId(session);
		restaurantService.updateRestaurant(ownerId, restaurantId, request);

		return "redirect:/owner/restaurants/list/" + restaurantId;
	}

	@GetMapping("/restaurants/listHistory")
	public String showRestaurantListPage2(HttpSession session, Model model) {
		List<RestaurantResponse> restaurantResponseList = getRestaurantResponseList(session);
		model.addAttribute("restaurantList", restaurantResponseList);

		return "owner/restaurantListHistory";
	}

	@GetMapping("/restaurants/{restaurantId}/history")
	public String showHistory(@PathVariable Long restaurantId, @RequestParam(defaultValue = "SCHEDULED") Status status ,Model model, HttpSession session) {
		List<HistoryResponse> historyResponses = ownerService.findHistoryByRestaurantIdAndStatus(restaurantId,status);
		session.setAttribute("historyList",historyResponses);
		model.addAttribute("history",historyResponses);
		model.addAttribute("restaurantId",restaurantId);

		return "owner/history";

	}
	@GetMapping("/restaurants/{restaurantId}/history/waiting/{waitingId}")
	public String getWaitingDetails(@PathVariable Long restaurantId, @PathVariable Long waitingId, HttpSession session, Model model) {
		List<HistoryResponse> historyList = (List<HistoryResponse>)session.getAttribute("historyList");
		model.addAttribute("restaurantId", restaurantId);

		if (Objects.nonNull(historyList)) {
			try {
				HistoryResponse historyResponse = historyService.findWaitingDetailById(historyList, waitingId);
				model.addAttribute("historyResponse", historyResponse);
				model.addAttribute("restaurantId",restaurantId);
				return "owner/waitingDetail";
			} catch (HistoryException e) {
				model.addAttribute("errorMessage", "지금은 상세정보를 조회할 수 없습니다");
				return "error";
			}
		}

		return "redirect:/owner";
	}
	@PostMapping("/restaurants/{restaurantId}/history/reservation/{reservationId}")
	public String deleteReservation(@PathVariable Long restaurantId, @PathVariable Long reservationId, Model model) {
		try {
			ReservationEntity reservation = reservationService.findReservationById(reservationId);
			Long memberId = reservation.getMember().getMemberId();
			reservationService.cancelReservation(memberId, reservationId);
		} catch (BookingErrorException e) {
			model.addAttribute("errorMessage", "예약 삭제 중 오류가 발생했습니다.");
			return "error"; // 오류 페이지로 리다이렉트
		}

		return "redirect:/owner";
	}

	@PostMapping("/restaurants/{restaurantId}/history/reservation/{reservationId}/completed")
	public String completedReservation(@PathVariable Long restaurantId, @PathVariable Long reservationId, Model model) {
		try {
			reservationService.completedReservation(reservationId);
		} catch (BookingErrorException e) {
			model.addAttribute("errorMessage", "예약 삭제 중 오류가 발생했습니다.");
			return "error"; // 오류 페이지로 리다이렉트
		}

		return "redirect:/owner";
	}

	@PostMapping("/restaurants/{restaurantId}/history/waiting/{waitingId}")
	public String deleteWaiting(@PathVariable Long restaurantId, @PathVariable Long waitingId, Model model) {
		try {
			WaitingEntity waiting = waitingService.getWaitingEntity(waitingId);
			Long memberId = waiting.getMember().getMemberId();
			waitingService.cancelWaiting(memberId, waitingId);
		} catch (BookingErrorException e) {
			model.addAttribute("errorMessage", "웨이팅 삭제 중 오류가 발생했습니다.");
			return "error"; // 오류 페이지로 리다이렉트
		}

		return "redirect:/owner";
	}

	@PostMapping("/restaurants/{restaurantId}/history/waiting/{waitingId}/completed")
	public String completeWaiting(@PathVariable Long restaurantId, @PathVariable Long waitingId, Model model) {
		try {
			waitingService.completedWaiting(waitingId);
		} catch (BookingErrorException e) {
			model.addAttribute("errorMessage", "웨이팅 완료 중 오류가 발생했습니다.");
			return "error"; // 오류 페이지로 리다이렉트
		}

		return "redirect:/owner";
	}

	@GetMapping("/restaurants/{restaurantId}/history/reservation/{reservationId}")
	public String getReservationDetails(@PathVariable Long restaurantId, @PathVariable Long reservationId,
										Model model, HttpSession session) {
		List<HistoryResponse> historyList = (List<HistoryResponse>) session.getAttribute("historyList");

		if (Objects.nonNull(historyList)) {
			try {
				HistoryResponse historyResponse = historyService.findReservationDetailById(historyList, reservationId);
				model.addAttribute("historyResponse", historyResponse);
				model.addAttribute("restaurantId", restaurantId);

				return "owner/reservationDetail";
			} catch (HistoryException e) {
				model.addAttribute("errorMessage", "지금은 상세정보를 조회할 수 없습니다");
				return "error";
			}
		}
		return "redirect:/owner";
	}

	@GetMapping("/restaurants/list/{restaurantId}/reviews")
	public String getReviews(@PathVariable Long restaurantId, Model model) {
		List<ReviewResponse> reviewList = reviewService.getRestaurantReviewList(restaurantId);
		BigDecimal averageRating = reviewService.getAverageRating(restaurantId).getRating();
		model.addAttribute("averageRating", averageRating);
		model.addAttribute("reviewList", reviewList);
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

		RestaurantResponse restaurant = restaurantService.findRestaurant(null, restaurantId);
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
	public String getMenus(@PathVariable Long restaurantId, Model model) {
		List<MenuResponse> restaurantMenuList = menuService.getRestaurantMenuList(restaurantId);

		model.addAttribute("jsKey", jsKey);
		model.addAttribute("restaurantMenuList", restaurantMenuList);

		return "owner/menus";
	}

	@PatchMapping("/restaurants/list/{restaurantId}/{hourId}")
	private String updateRestaurantHour(@PathVariable Long restaurantId, @PathVariable Long hourId,
										@Valid @ModelAttribute RestaurantHourRequest request, BindingResult bindingResult, Model model,
										RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "영업 시간 업데이트 중 오류가 발생했습니다.");
			return "redirect:/owner/restaurants/list/" + restaurantId;
		}

		try {
			restaurantHourService.updateRestaurantHour(hourId, request);
			redirectAttributes.addFlashAttribute("message", "영업 시간이 성공적으로 업데이트");
		} catch (CatchLineException e) {
			redirectAttributes.addFlashAttribute("error", "영업 시간 업데이트 중 오류가 발생했습니다.");
		}
		return "redirect:/owner/restaurants/list/" + restaurantId;
	}

	@PutMapping("/restaurants/list/{restaurantId}/menus/menu")
	public String updateMenu(@PathVariable Long restaurantId, @RequestParam Long menuId,
							 @ModelAttribute MenuRequest menuRequest, Model model) {

		menuService.updateRestaurantMenu(restaurantId, menuId, menuRequest);

		List<MenuResponse> menuResponseList = menuService.getRestaurantMenuList(restaurantId);
		model.addAttribute("restaurantMenuList", menuResponseList);

		return "redirect:/owner/restaurants/list/" + restaurantId + "/menus";
	}

	@DeleteMapping("/restaurants/list/{restaurantId}/menus/menu")
	public String deleteMenu(@PathVariable Long restaurantId, @RequestParam Long menuId) {
		menuService.deleteRestaurantMenu(menuId);
		return "redirect:/owner/restaurants/list/" + restaurantId + "/menus";
	}

	@PostMapping("/restaurants/list/{restaurantId}/menus/menu")
	public String addMenu(@PathVariable Long restaurantId, @ModelAttribute MenuRequest menuRequest, Model model) {
		menuService.createRestaurantMenu(restaurantId, menuRequest);

		List<MenuResponse> restaurantMenuList = menuService.getRestaurantMenuList(restaurantId);
		model.addAttribute("restaurantMenuList", restaurantMenuList);
		return "redirect:/owner/restaurants/list/{restaurantId}/menus";
	}

	private List<RestaurantResponse> getRestaurantResponseList(HttpSession session) {
		Long ownerId = SessionUtils.getOwnerId(session);
        return ownerService.findAllRestaurantByOwnerId(ownerId);
	}

	private String invalidPhoneNumberException(Exception e, BindingResult bindingResult) {
		bindingResult.rejectValue("phoneNumber", null, e.getMessage());
		return "owner/createRestaurant";
	}

}
