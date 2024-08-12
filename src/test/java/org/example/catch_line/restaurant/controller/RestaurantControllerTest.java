package org.example.catch_line.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.restaurant.model.dto.RestaurantHourResponse;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.entity.constant.DayOfWeeks;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.model.entity.constant.OpenStatus;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.restaurant.repository.RestaurantHourRepository;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.example.catch_line.restaurant.service.RestaurantHourService;
import org.example.catch_line.restaurant.service.RestaurantService;
import org.example.catch_line.restaurant.validation.RestaurantValidator;
import org.example.catch_line.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(RestaurantController.class) // controller 단위 테스트
@MockBean(JpaMetamodelMappingContext.class) // JPA metamodel empty 문제 해결 방법
@AutoConfigureMockMvc(addFilters = false) // 테스트 시 Security 비활성화
class RestaurantControllerTest {

    @Autowired MockMvc mockMvc;

    @MockBean RestaurantService restaurantService;
    @MockBean RestaurantHourService restaurantHourService;

    @Test
    @DisplayName("식당 상세 조회 테스트")
    void view_restaurant_test() throws Exception {
        // given
        Long restaurantId = 1L;
        RestaurantResponse restaurant = getRestaurantResponse();
        List<RestaurantHourResponse> restaurantHours = getRestaurantHourResponseList();
        RestaurantHourResponse hourResponse = getRestaurantHourResponse();

        DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();
        DayOfWeeks dayOfWeek = DayOfWeeks.from(currentDayOfWeek);

        when(restaurantService.findRestaurant(restaurantId)).thenReturn(restaurant);
        when(restaurantHourService.getAllRestaurantHours(restaurantId)).thenReturn(restaurantHours);
        when(restaurantHourService.getRestaurantHour(restaurantId, dayOfWeek)).thenReturn(hourResponse);

        // when
        // then
        mockMvc.perform(get("/restaurants/{restaurantId}", restaurantId))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurant/restaurant"))
                .andExpect(model().attribute("restaurant", restaurant))
                .andExpect(model().attribute("restaurantHours", restaurantHours))
                .andExpect(model().attribute("hourResponse", hourResponse))
                .andExpect(model().attribute("dayOfWeek", dayOfWeek.getDescription()));
    }

    private RestaurantResponse getRestaurantResponse() {
        return RestaurantResponse.builder()
                .restaurantId(1L)
                .name("연돈")
                .description("소개")
                .averageRating(BigDecimal.valueOf(4.7))
                .phoneNumber("064-1234-1234")
                .scrapCount(0L)
                .reviewCount(10L)
                .longitude(BigDecimal.ZERO)
                .latitude(BigDecimal.ZERO)
                .foodType(FoodType.KOREAN)
                .serviceType(ServiceType.RESERVATION)
                .build();
    }

    private RestaurantHourResponse getRestaurantHourResponse() {
        return RestaurantHourResponse.builder()
                .restaurantHourId(1L)
                .dayOfWeek(DayOfWeeks.MONDAY.getDescription())
                .openTime(LocalTime.of(9, 0))
                .closeTime(LocalTime.of(21, 0))
                .openStatus(OpenStatus.OPEN.getDescription())
                .build();
    }

    private List<RestaurantHourResponse> getRestaurantHourResponseList() {
        List<RestaurantHourResponse> list = new ArrayList<>();

        list.add(getRestaurantHourResponse());
        list.add(getRestaurantHourResponse());
        return list;
    }
}