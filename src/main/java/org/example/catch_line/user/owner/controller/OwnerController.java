package org.example.catch_line.user.owner.controller;

import java.math.BigDecimal;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.common.constant.Role;
import org.example.catch_line.common.constant.SessionConst;
import org.example.catch_line.common.kakao.model.dto.KakaoCoordinateResponse;
import org.example.catch_line.common.kakao.service.KakaoAddressService;
import org.example.catch_line.exception.phone.InvalidPhoneNumberException;
import org.example.catch_line.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.dto.RestaurantUpdateRequest;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.restaurant.service.RestaurantService;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.example.catch_line.user.owner.repository.OwnerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final RestaurantService restaurantService;
    private final OwnerRepository ownerRepository;
    private final KakaoAddressService kakaoAddressService;

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
        model.addAttribute("request", new RestaurantCreateRequest("", "", "", "", FoodType.KOREAN, ServiceType.WAITING,null,null,null));
        return "owner/createRestaurant";
    }

    @PostMapping("/restaurants")
    public String createRestaurant(@ModelAttribute("request") RestaurantCreateRequest request, BindingResult bindingResult,HttpSession session) {

        OwnerEntity owner = ownerRepository.findByOwnerId(SessionUtils.getOwnerId(session)).orElseThrow(() -> new IllegalArgumentException("사장님을 찾을 수 없습니다"));

        String address = request.getAddress();
        KakaoCoordinateResponse kakaoCoordinateResponse = kakaoAddressService.addressToCoordinate(address);
        //TODO :: Setter 제거하기
        request.setLatitude(new BigDecimal(kakaoCoordinateResponse.getDocuments().get(0).getRoadAddress().getX()));
        request.setLongitude(new BigDecimal(kakaoCoordinateResponse.getDocuments().get(0).getRoadAddress().getY()));
        request.setOwner(owner);

        if(bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
            return "owner/createRestaurant";
        }

        try {
            restaurantService.createRestaurant(request);
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
    public String updateRestaurant(@PathVariable Long restaurantId, @ModelAttribute("restaurant") RestaurantUpdateRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
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
