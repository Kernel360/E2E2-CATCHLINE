package org.example.catch_line.restaurant.service;

import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantUpdateRequest;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.dining.restaurant.model.mapper.RestaurantMapper;
import org.example.catch_line.dining.restaurant.repository.RestaurantRepository;
import org.example.catch_line.dining.restaurant.service.RestaurantService;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.example.catch_line.review.service.ReviewService;
import org.example.catch_line.scrap.service.ScrapService;
import org.example.catch_line.user.owner.validation.OwnerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ReviewService reviewService;

    @Mock
    private ScrapService scrapService;

    @Mock
    private RestaurantMapper restaurantMapper;

    @Mock
    private RestaurantValidator restaurantValidator;

    @Mock
    private OwnerValidator ownerValidator;

    @InjectMocks
    private RestaurantService restaurantService;

    private RestaurantEntity restaurantEntity;

    @BeforeEach
    void setUp() {
        restaurantEntity = new RestaurantEntity(
                "새마을식당", "백종원의 새마을식당", new Rating(BigDecimal.valueOf(4.5)),
                new PhoneNumber("010-1234-5678"), FoodType.KOREAN, ServiceType.RESERVATION, null,
                new BigDecimal("37.123456"), new BigDecimal("127.123456")
        );
    }

    @Test
    @DisplayName("식당 상세 정보 조회 테스트")
    void findRestaurant_success() {
        // given
        Long memberId = 1L;
        Long restaurantId = 1L;

        when(restaurantValidator.checkIfRestaurantPresent(restaurantId)).thenReturn(restaurantEntity);
        when(reviewService.getAverageRating(restaurantId)).thenReturn(new Rating(BigDecimal.valueOf(4.5)));
        when(reviewService.getReviewCount(restaurantId)).thenReturn(100L);
        when(scrapService.getRestaurantScraps(restaurantId)).thenReturn(50L);
        when(scrapService.hasMemberScrapRestaurant(memberId, restaurantId)).thenReturn(true);

        RestaurantResponse mockResponse = RestaurantResponse.builder()
                .restaurantId(restaurantId)
                .name(restaurantEntity.getName())
                .description(restaurantEntity.getDescription())
                .averageRating(restaurantEntity.getAverageRating())
                .phoneNumber(restaurantEntity.getPhoneNumber().toString())
                .latitude(restaurantEntity.getLatitude())
                .longitude(restaurantEntity.getLongitude())
                .scrapCount(50L)
                .reviewCount(100L)
                .foodType(restaurantEntity.getFoodType())
                .serviceType(restaurantEntity.getServiceType())
                .hasScrapped(true)
                .build();

        when(restaurantMapper.entityToResponse(restaurantEntity, true)).thenReturn(mockResponse);

        // when
        RestaurantResponse response = restaurantService.findRestaurant(memberId, restaurantId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("새마을식당");


        verify(restaurantValidator, times(1)).checkIfRestaurantPresent(restaurantId);
        verify(reviewService, times(1)).getAverageRating(restaurantId);
        verify(reviewService, times(1)).getReviewCount(restaurantId);
        verify(scrapService, times(1)).getRestaurantScraps(restaurantId);
        verify(scrapService, times(1)).hasMemberScrapRestaurant(memberId, restaurantId);
        verify(restaurantMapper, times(1)).entityToResponse(restaurantEntity, true);
    }

    @Test
    @DisplayName("식당 정보 업데이트 테스트")
    void updateRestaurant_success() {
        // given
        Long ownerId = 1L;
        Long restaurantId = 1L;
        RestaurantUpdateRequest request = RestaurantUpdateRequest.builder()
                .name("새로운 식당 이름")
                .description("새로운 설명")
                .phoneNumber("010-9876-5432")
                .foodType(FoodType.JAPANESE)
                .serviceType(ServiceType.WAITING)
                .build();

        RestaurantEntity spyRestaurantEntity = spy(restaurantEntity);

        when(restaurantValidator.checkIfRestaurantPresent(restaurantId)).thenReturn(spyRestaurantEntity);

        // when
        restaurantService.updateRestaurant(ownerId, restaurantId, request);

        // then
        verify(ownerValidator, times(1)).checkIfOwnerIdExist(ownerId);
        verify(restaurantValidator, times(1)).checkIfRestaurantPresent(restaurantId);
        verify(spyRestaurantEntity, times(1)).updateReservation(
                request.getName(),
                request.getDescription(),
                new PhoneNumber(request.getPhoneNumber()),
                request.getFoodType(),
                request.getServiceType()
        );
    }


    @Test
    @DisplayName("식당 삭제 테스트")
    void deleteRestaurant_success() {
        // given
        Long restaurantId = 1L;

        // when
        restaurantService.deleteRestaurant(restaurantId);

        // then
        verify(restaurantRepository, times(1)).deleteById(restaurantId);
    }
}
