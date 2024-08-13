package org.example.catch_line.restaurant.service;


import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.user.member.model.vo.PhoneNumber;
import org.example.catch_line.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantPreviewServiceTest {

    @Mock RestaurantRepository restaurantRepository;
    @InjectMocks RestaurantPreviewService restaurantPreviewService;

    @Test
    @DisplayName("식당 프리뷰 리스트 조회 테스트")
    void restaurant_preview_list_test() {
        // given
        when(restaurantRepository.findAll()).thenReturn(getRestaurantList());

        // when
        List<RestaurantPreviewResponse> previewList = restaurantPreviewService.getRestaurantPreviewList();

        // then
        assertThat(previewList).isNotNull();
        assertThat(previewList.size()).isGreaterThan(0);
        assertThat(previewList.get(0).getName()).isEqualTo("새마을식당");
        assertThat(previewList.get(1).getName()).isEqualTo("연돈");
        assertThat(previewList.get(2).getName()).isEqualTo("숙성도");
    }

    @Test
    @DisplayName("식당 프리뷰 페이지네이션 테스트")
    void restaurant_preview_pagination_list_test() {
        // given
        Pageable pageable = PageRequest.of(0, 2);
        String criteria = "reviewCount";

        List<RestaurantEntity> restaurantList = getRestaurantList();
        Page<RestaurantEntity> restaurantPage = new PageImpl<>(restaurantList, pageable, 2);

        when(restaurantRepository.findAll(any(Pageable.class))).thenReturn(restaurantPage);

        // when
        Page<RestaurantPreviewResponse> result = restaurantPreviewService.restaurantPreviewPaging(pageable, criteria);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getName()).isEqualTo("새마을식당");
        assertThat(result.getContent().get(1).getName()).isEqualTo("연돈");

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(restaurantRepository, times(1)).findAll(pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        Sort sort = capturedPageable.getSort();

        // Sort 객체가 올바르게 설정되었는지 검증합니다.
        assertThat(sort.getOrderFor("reviewCount").getDirection()).isEqualTo(Sort.Direction.DESC);
        assertThat(sort.getOrderFor("restaurantId").getDirection()).isEqualTo(Sort.Direction.DESC);
    }


    private List<RestaurantEntity> getRestaurantList() {
        List<RestaurantEntity> list = new ArrayList<>();

        RestaurantEntity restaurant1 = RestaurantEntity.builder()
                .name("새마을식당")
                .description("백종원의 새마을식당")
                .phoneNumber(new PhoneNumber("02-1234-1234"))
                .rating(new Rating(BigDecimal.ZERO))
                .serviceType(ServiceType.WAITING)
                .foodType(FoodType.KOREAN)
                .build();

        RestaurantEntity restaurant2 = RestaurantEntity.builder()
                .name("연돈")
                .description("돈까스")
                .phoneNumber(new PhoneNumber("063-1234-1234"))
                .rating(new Rating(BigDecimal.ZERO))
                .serviceType(ServiceType.RESERVATION)
                .foodType(FoodType.KOREAN)
                .build();

        RestaurantEntity restaurant3 = RestaurantEntity.builder()
                .name("숙성도")
                .description("삼겹살")
                .phoneNumber(new PhoneNumber("063-4321-1234"))
                .rating(new Rating(BigDecimal.ZERO))
                .serviceType(ServiceType.WAITING)
                .foodType(FoodType.KOREAN)
                .build();

        list.add(restaurant1); list.add(restaurant2); list.add(restaurant3);
        return list;
    }
}