package org.example.catch_line.restaurant.service;

import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.dining.restaurant.model.mapper.RestaurantPreviewMapper;
import org.example.catch_line.dining.restaurant.repository.RestaurantRepository;
import org.example.catch_line.dining.restaurant.service.RestaurantPreviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantPreviewServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantPreviewMapper restaurantPreviewMapper;

    @InjectMocks
    private RestaurantPreviewService restaurantPreviewService;

    private List<RestaurantEntity> restaurantList;

    @BeforeEach
    void setUp() {
        restaurantList = getMockRestaurants();
    }

    @Test
    @DisplayName("식당 프리뷰 리스트 조회 테스트")
    void shouldReturnRestaurantPreviewList() {
        // given
        when(restaurantRepository.findAll()).thenReturn(restaurantList);
        for (RestaurantEntity entity : restaurantList) {
            when(restaurantPreviewMapper.entityToResponse(entity))
                    .thenReturn(RestaurantPreviewResponse.builder()
                            .restaurantId(entity.getRestaurantId())
                            .name(entity.getName())
                            .averageRating(entity.getAverageRating())
                            .reviewCount(entity.getReviewCount())
                            .scrapCount(entity.getScrapCount())
                            .foodType(entity.getFoodType().name())
                            .serviceType(entity.getServiceType().name())
                            .build());
        }

        // when
        List<RestaurantPreviewResponse> previewList = restaurantPreviewService.getRestaurantPreviewList();

        // then
        assertThat(previewList).isNotNull();
        assertThat(previewList.size()).isEqualTo(restaurantList.size());
        assertThat(previewList.get(0).getName()).isEqualTo(restaurantList.get(0).getName());
        verify(restaurantRepository, times(1)).findAll();
        verify(restaurantPreviewMapper, times(restaurantList.size())).entityToResponse(any(RestaurantEntity.class));
    }

    @Test
    @DisplayName("식당 프리뷰 검색 및 페이지네이션 테스트")
    void shouldReturnPagedRestaurantPreviewsBasedOnCriteriaAndSearch() {
        // given
        Pageable pageable = PageRequest.of(0, 2);
        Page<RestaurantEntity> restaurantPage = new PageImpl<>(restaurantList, pageable, restaurantList.size());

        when(restaurantRepository.findRestaurantsByCriteria(any(Pageable.class), anyString(), anyString(), anyString())).thenReturn(restaurantPage);
        for (RestaurantEntity entity : restaurantList) {
            when(restaurantPreviewMapper.entityToResponse(entity))
                    .thenReturn(RestaurantPreviewResponse.builder()
                            .restaurantId(entity.getRestaurantId())
                            .name(entity.getName())
                            .averageRating(entity.getAverageRating())
                            .reviewCount(entity.getReviewCount())
                            .scrapCount(entity.getScrapCount())
                            .foodType(entity.getFoodType().name())
                            .serviceType(entity.getServiceType().name())
                            .build());
        }

        // when
        Page<RestaurantPreviewResponse> result = restaurantPreviewService.restaurantPreviewSearchAndPaging(pageable, "reviewCount", "name", "식당");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(restaurantList.size());
        verify(restaurantRepository, times(1)).findRestaurantsByCriteria(any(Pageable.class), anyString(), anyString(), anyString());
        verify(restaurantPreviewMapper, times(restaurantList.size())).entityToResponse(any(RestaurantEntity.class));
    }

    @Test
    @DisplayName("식당 프리뷰 페이지네이션 테스트")
    void shouldReturnPagedRestaurantPreviewsBasedOnPaging() {
        // given
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "reviewCount").and(Sort.by(Sort.Direction.DESC, "restaurantId")));
        Page<RestaurantEntity> restaurantPage = new PageImpl<>(restaurantList, pageable, restaurantList.size());

        when(restaurantRepository.findAll(any(Pageable.class))).thenReturn(restaurantPage);
        for (RestaurantEntity entity : restaurantList) {
            when(restaurantPreviewMapper.entityToResponse(entity))
                    .thenReturn(RestaurantPreviewResponse.builder()
                            .restaurantId(entity.getRestaurantId())
                            .name(entity.getName())
                            .averageRating(entity.getAverageRating())
                            .reviewCount(entity.getReviewCount())
                            .scrapCount(entity.getScrapCount())
                            .foodType(entity.getFoodType().name())
                            .serviceType(entity.getServiceType().name())
                            .build());
        }

        // when
        Page<RestaurantPreviewResponse> result = restaurantPreviewService.restaurantPreviewPaging(pageable, "reviewCount");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(restaurantList.size());
        verify(restaurantRepository, times(1)).findAll(any(Pageable.class));
        verify(restaurantPreviewMapper, times(restaurantList.size())).entityToResponse(any(RestaurantEntity.class));
    }

    private List<RestaurantEntity> getMockRestaurants() {
        List<RestaurantEntity> list = new ArrayList<>();

        RestaurantEntity restaurant1 = new RestaurantEntity(
                "새마을식당", "백종원의 새마을식당", new Rating(BigDecimal.ZERO),
                new PhoneNumber("02-1234-1234"), FoodType.KOREAN, ServiceType.WAITING, null,
                new BigDecimal("37.123456"), new BigDecimal("127.123456")
        );

        RestaurantEntity restaurant2 = new RestaurantEntity(
                "연돈", "연돈 돈까스", new Rating(BigDecimal.ZERO),
                new PhoneNumber("063-1234-1234"), FoodType.KOREAN, ServiceType.RESERVATION, null,
                new BigDecimal("35.123456"), new BigDecimal("129.123456")
        );

        RestaurantEntity restaurant3 = new RestaurantEntity(
                "숙성도", "숙성도 삼겹살", new Rating(BigDecimal.ZERO),
                new PhoneNumber("02-4321-4321"), FoodType.KOREAN, ServiceType.WAITING, null,
                new BigDecimal("37.654321"), new BigDecimal("127.654321")
        );

        list.add(restaurant1);
        list.add(restaurant2);
        list.add(restaurant3);

        return list;
    }
}
