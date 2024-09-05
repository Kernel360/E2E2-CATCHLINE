package org.example.catch_line.user.owner.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.constant.DayOfWeeks;
import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.kakao.model.dto.KakaoAddressResponse;
import org.example.catch_line.kakao.service.KakaoAddressService;
import org.example.catch_line.dining.restaurant.model.dto.*;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantImageEntity;
import org.example.catch_line.dining.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.dining.restaurant.service.RestaurantHourService;
import org.example.catch_line.dining.restaurant.service.RestaurantImageService;
import org.example.catch_line.dining.restaurant.service.RestaurantService;
import org.example.catch_line.exception.dining.RestaurantHourNotFoundException;
import org.example.catch_line.exception.phone.InvalidPhoneNumberException;
import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.service.ReviewService;
import org.example.catch_line.user.auth.details.OwnerUserDetails;
import org.example.catch_line.user.owner.service.OwnerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/owner")
@RequiredArgsConstructor
@Slf4j
public class OwnerRestaurantController {

    private final RestaurantService restaurantService;
    private final OwnerService ownerService;
    private final RestaurantImageService restaurantImageService;
    private final KakaoAddressService kakaoAddressService;
    private final RestaurantHourService restaurantHourService;
    private final ReviewService reviewService;

    @Value("${kakao.maps.js-key}")
    private String jsKey;

    @GetMapping("/restaurants")
    public String createRestaurantForm(Model model) {
        model.addAttribute("request",
                new RestaurantCreateRequest("", "", "", "", FoodType.KOREAN, ServiceType.WAITING));
        return "owner/createRestaurant";
    }

    @PostMapping("/restaurants")
    public String createRestaurant(@Valid @ModelAttribute("request") RestaurantCreateRequest request,
                                   BindingResult bindingResult, @AuthenticationPrincipal OwnerUserDetails ownerUserDetails) {
        Long ownerId = ownerUserDetails.getOwner().getOwnerId();
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

    @GetMapping("/restaurants/{restaurantId}/edit")
    public String updateRestaurantForm(@PathVariable Long restaurantId, Model model) {
        RestaurantResponse restaurant = restaurantService.findRestaurant(null, restaurantId);
        model.addAttribute("restaurant", restaurant);
        return "owner/updateRestaurant";
    }

    @PostMapping("/restaurants/{restaurantId}")
    public String updateRestaurant(@PathVariable Long restaurantId, @AuthenticationPrincipal OwnerUserDetails ownerUserDetails,
                                   @Valid @ModelAttribute("restaurant") RestaurantUpdateRequest request, BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "owner/updateRestaurant";
        }
        Long ownerId = ownerUserDetails.getOwner().getOwnerId();
        try {
            restaurantService.updateRestaurant(ownerId, restaurantId, request);
        } catch (InvalidPhoneNumberException e) {
            log.info("번호 에러 = {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/owner/restaurants/{restaurantId}";
        }

        return "redirect:/owner/restaurants/{restaurantId}";
    }

    @PatchMapping("/restaurants/{restaurantId}/{hourId}")
    private String updateRestaurantHour(@PathVariable Long restaurantId, @PathVariable Long hourId,
                                        @Valid @ModelAttribute RestaurantHourRequest request, BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "영업 시간 업데이트 중 오류가 발생했습니다.");
            return "redirect:/owner/restaurants/{restaurantId}";
        }

        try {
            restaurantHourService.updateRestaurantHour(hourId, request);
            redirectAttributes.addFlashAttribute("errorMessage", "영업 시간이 성공적으로 업데이트");
        } catch (RestaurantHourNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "영업 시간 업데이트 중 오류가 발생했습니다.");
        }
        return "redirect:/owner/restaurants/{restaurantId}";
    }

    @GetMapping("/restaurants/{restaurantId}")
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

    @GetMapping("/restaurants/{restaurantId}/reviews")
    public String getReviews(@PathVariable Long restaurantId, Model model) {
        List<ReviewResponse> reviewList = reviewService.getRestaurantReviewList(restaurantId);
        BigDecimal averageRating = reviewService.getAverageRating(restaurantId).getRating();
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("reviewList", reviewList);
        return "owner/reviews";
    }

    private String invalidPhoneNumberException(Exception e, BindingResult bindingResult) {
        bindingResult.rejectValue("phoneNumber", null, e.getMessage());
        return "owner/createRestaurant";
    }
}
