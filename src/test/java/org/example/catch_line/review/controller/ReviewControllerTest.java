package org.example.catch_line.review.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(ReviewController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest {

    @Autowired MockMvc mockMvc;

    @MockBean ReviewService reviewService;

    @Test
    @DisplayName("식당 리뷰 리스트 조회 테스트")
    void restaurant_reviews_test() throws Exception {
        // given
        Long restaurantId = 1L;
        List<ReviewResponse> reviewList = getReviewList();

        BigDecimal averageRating = BigDecimal.valueOf(4.0);
        Rating rating = new Rating(averageRating);

        when(reviewService.getRestaurantReviewList(restaurantId)).thenReturn(reviewList);
        when(reviewService.getAverageRating(restaurantId)).thenReturn(rating);

        // when
        // then
        mockMvc.perform(get("/restaurants/{restaurantId}/reviews", restaurantId))
                .andExpect(status().isOk())
                .andExpect(view().name("review/reviews"))
                .andExpect(model().attribute("averageRating", averageRating))
                .andExpect(model().attribute("reviewList", reviewList));
    }

    private List<ReviewResponse> getReviewList() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<ReviewResponse> list = new ArrayList<>();

        ReviewResponse review1 = ReviewResponse.builder()
                .rating(5)
                .content("맛있어요")
                .createdAt(LocalDateTime.now().format(formatter))
                .build();

        ReviewResponse review2 = ReviewResponse.builder()
                .rating(4)
                .content("보통이에요")
                .createdAt(LocalDateTime.now().format(formatter))
                .build();

        ReviewResponse review3 = ReviewResponse.builder()
                .rating(1)
                .content("맛없어요")
                .createdAt(LocalDateTime.now().format(formatter))
                .build();

        list.add(review1); list.add(review2); list.add(review3);
        return list;
    }


}