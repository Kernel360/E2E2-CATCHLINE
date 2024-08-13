package org.example.catch_line.restaurant.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.kakao.model.dto.KakaoAddressResponse;
import org.example.catch_line.common.kakao.service.KakaoAddressService;
import org.example.catch_line.restaurant.model.dto.RestaurantHourResponse;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.entity.constant.DayOfWeeks;
import org.example.catch_line.restaurant.service.RestaurantHourService;
import org.example.catch_line.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantHourService restaurantHourService;
    private final KakaoAddressService kakaoAddressService;

    @Value("${kakao.maps.js-key}")
    private String jsKey;

    // TODO: 리뷰 개수는 실제 DB 조회해서 count하고, 평점은 DB에서 리뷰 점수 다 가져와서 평균 계산해야함.
    // TODO: 현재 날짜 기준으로 보여줘야 함.
    @GetMapping("/{restaurantId}")
    public String viewRestaurant(
            @PathVariable Long restaurantId,
            Model model
    ) {
        DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();
        DayOfWeeks dayOfWeek = DayOfWeeks.from(currentDayOfWeek);

        RestaurantResponse restaurant = restaurantService.findRestaurant(restaurantId);
        List<RestaurantHourResponse> restaurantHours = restaurantHourService.getAllRestaurantHours(restaurantId);
        RestaurantHourResponse hourResponse = restaurantHourService.getRestaurantHour(restaurantId, dayOfWeek);

        String x = String.valueOf(restaurant.getLatitude());
        String y = String.valueOf(restaurant.getLongitude());

        KakaoAddressResponse kakaoAddressResponse = kakaoAddressService.coordinateToAddress(x, y);
        KakaoAddressResponse.Document document = kakaoAddressResponse.getDocuments().get(0);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("restaurantHours", restaurantHours);
        model.addAttribute("hourResponse", hourResponse);
        model.addAttribute("document", document);
        model.addAttribute("jsKey", jsKey);
        model.addAttribute("dayOfWeek", dayOfWeek.getDescription());
        return "restaurant/restaurant";
    }

}
