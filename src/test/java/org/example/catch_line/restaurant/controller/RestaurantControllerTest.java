package org.example.catch_line.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.common.kakao.model.dto.KakaoAddressResponse;
import org.example.catch_line.common.kakao.service.KakaoAddressService;
import org.example.catch_line.dining.restaurant.controller.RestaurantController;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantHourResponse;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.common.constant.DayOfWeeks;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantImageEntity;
import org.example.catch_line.dining.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.dining.restaurant.model.entity.constant.OpenStatus;
import org.example.catch_line.dining.restaurant.service.RestaurantHourService;
import org.example.catch_line.dining.restaurant.service.RestaurantImageService;
import org.example.catch_line.dining.restaurant.service.RestaurantService;
import org.example.catch_line.user.auth.details.MemberUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@WebMvcTest(RestaurantController.class) // controller 단위 테스트
@MockBean(JpaMetamodelMappingContext.class) // JPA metamodel empty 문제 해결 방법
@AutoConfigureMockMvc(addFilters = false) // 테스트 시 Security 비활성화
class RestaurantControllerTest {

    @Autowired MockMvc mockMvc;

    @MockBean RestaurantService restaurantService;
    @MockBean RestaurantHourService restaurantHourService;
    @MockBean RestaurantImageService restaurantImageService;
    @MockBean KakaoAddressService kakaoAddressService;
    @Autowired
    private RestaurantController restaurantController;

    @Test
    @DisplayName("식당 상세 조회 테스트")
    void viewRestaurantTest() throws Exception {
        // given
        final Long restaurantId = 1L;
        final RestaurantResponse restaurant = getRestaurantResponse();
        final List<RestaurantHourResponse> restaurantHours = getRestaurantHourResponseList();
        final RestaurantHourResponse hourResponse = getRestaurantHourResponse();
        final DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();
        final DayOfWeeks dayOfWeek = DayOfWeeks.from(currentDayOfWeek);
        final Long memberId = 1L;

        Model model = mock(Model.class);
        MemberUserDetails memberUserDetails = mock(MemberUserDetails.class);

        when(restaurantService.findRestaurant(memberId, restaurantId)).thenReturn(restaurant);
        when(restaurantHourService.getAllRestaurantHours(restaurantId)).thenReturn(restaurantHours);
        when(restaurantHourService.getRestaurantHour(restaurantId, dayOfWeek)).thenReturn(hourResponse);

        // 수정된 부분: KakaoAddressService의 모킹 설정
        KakaoAddressResponse kakaoAddressResponse = new KakaoAddressResponse();
        KakaoAddressResponse.Document document = new KakaoAddressResponse.Document();
        KakaoAddressResponse.Address address = new KakaoAddressResponse.Address();
        address.setAddressName("서울특별시 종로구"); // Address 객체의 addressName 설정
        document.setAddress(address);
        kakaoAddressResponse.setDocuments(List.of(document));
        when(kakaoAddressService.coordinateToAddress(anyString(), anyString())).thenReturn(kakaoAddressResponse);

        // 추가: RestaurantImageService의 모킹 설정
        List<RestaurantImageEntity> imageList = new ArrayList<>();
        when(restaurantImageService.getImageList(restaurantId)).thenReturn(imageList);

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