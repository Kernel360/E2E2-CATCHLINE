package org.example.catch_line.restaurant.controller;

import org.example.catch_line.common.constant.SessionConst;
import org.example.catch_line.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.restaurant.service.RestaurantPreviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantPreviewController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class RestaurantPreviewControllerTest {

    @Autowired MockMvc mockMvc;

    @MockBean RestaurantPreviewService restaurantPreviewService;

    @Test
    @DisplayName("식당 프리뷰 테스트")
    void restaurant_preview_list_test() throws Exception {
        // given
        String criteria = "reviewCount";
        Pageable pageable = PageRequest.of(0, 2);
        List<RestaurantPreviewResponse> restaurantList = getRestaurantPreviewResponseList();
        PageImpl<RestaurantPreviewResponse> restaurantPage = new PageImpl<>(restaurantList, pageable, 10);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.MEMBER_ID, 1L);

        when(restaurantPreviewService.restaurantPreviewPaging(pageable, criteria)).thenReturn(restaurantPage);

        int blockLimit = 5;
        int startPage = (((int) Math.ceil(((double) (pageable.getPageNumber() +1) / blockLimit))) -1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), restaurantPage.getTotalPages());

        // when
        // then
        mockMvc.perform(get("/restaurants")
                        .session(session)
                        .param("criteria", criteria))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurant/restaurantPreview"))
                .andExpect(model().attribute("isLoggedIn", true))
                .andExpect(model().attribute("restaurantPreviewPage", restaurantPage))
                .andExpect(model().attribute("startPage", startPage))
                .andExpect(model().attribute("endPage", endPage))
                .andExpect(model().attribute("criteria", criteria));
    }

    private List<RestaurantPreviewResponse> getRestaurantPreviewResponseList() {
        RestaurantPreviewResponse restaurant1 = RestaurantPreviewResponse.builder()
                .restaurantId(1L)
                .name("식당1")
                .averageRating(BigDecimal.valueOf(4.5))
                .reviewCount(100L)
                .serviceType(ServiceType.RESERVATION.getDescription())
                .foodType(FoodType.KOREAN.getDescription())
                .build();

        RestaurantPreviewResponse restaurant2 = RestaurantPreviewResponse.builder()
                .restaurantId(2L)
                .name("식당1")
                .averageRating(BigDecimal.valueOf(4.0))
                .reviewCount(50L)
                .serviceType(ServiceType.WAITING.getDescription())
                .foodType(FoodType.CHINESE.getDescription())
                .build();

        return Arrays.asList(restaurant1, restaurant2);
    }
}