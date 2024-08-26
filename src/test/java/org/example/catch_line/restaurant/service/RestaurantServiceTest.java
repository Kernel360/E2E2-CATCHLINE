package org.example.catch_line.restaurant.service;

import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.dining.restaurant.service.RestaurantService;
import org.example.catch_line.scrap.service.ScrapService;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantUpdateRequest;
import org.example.catch_line.dining.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.mapper.RestaurantMapper;
import org.example.catch_line.dining.restaurant.repository.RestaurantRepository;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.example.catch_line.review.service.ReviewService;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
// 해당 어노테이션을 붙여야 @Mock으로 선언된 객체들이 초기화된다.
// 넣지 않으면 객체들이 모두 null로 들어가서 NullPointerException 발생
@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    // RestaurantService에서 사용할 가짜 객체 생성
    @Mock RestaurantRepository restaurantRepository;
    @Mock ReviewService reviewService;
    @Mock ScrapService scrapService;
    @Mock RestaurantValidator restaurantValidator;
    @Mock RestaurantMapper restaurantMapper;

    // RestaurantService에 위에서 만든 가짜 객체 넣어주기
    @InjectMocks
    RestaurantService restaurantService;

    RestaurantEntity restaurantEntity;

    @BeforeEach
    void setUp() {
        OwnerEntity ownerEntity = new OwnerEntity();
        restaurantEntity = new RestaurantEntity("새마을식당", "백종원", new Rating(BigDecimal.ZERO),
                new PhoneNumber("02-1234-1234"), FoodType.KOREAN, ServiceType.WAITING, ownerEntity, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    @Test
    @DisplayName("식당 상세 조회 테스트")
    void restaurant_find() {
        // given
        Long restaurantId = 1L;
        Long memberId = 1L;

        when(restaurantValidator.checkIfRestaurantPresent(restaurantId)).thenReturn(restaurantEntity);
        when(reviewService.getAverageRating(restaurantId)).thenReturn(new Rating(BigDecimal.ZERO));
        when(reviewService.getReviewCount(restaurantId)).thenReturn(0L);
        when(scrapService.getRestaurantScraps(restaurantId)).thenReturn(0L);

        // when
        RestaurantResponse response = restaurantService.findRestaurant(memberId, restaurantId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(restaurantEntity.getName());
        assertThat(response.getDescription()).isEqualTo(restaurantEntity.getDescription());
    }

    @Test
    @DisplayName("식당 상세 조회 테스트 - 리뷰 3개, 평균 평점 4.5")
    void restaurant_find_with_reviews_3() {
        // given
        Long restaurantId = 1L;
        Long memberId = 1L;
        Long reviewCount = 3L;
        double averageRating = 4.5;

        // 앞에 넣은 메서드가 호출되면 뒤에 적은 객체, 값을 반환한다.
        when(restaurantValidator.checkIfRestaurantPresent(restaurantId)).thenReturn(restaurantEntity);
        when(reviewService.getAverageRating(restaurantId)).thenReturn(new Rating(BigDecimal.valueOf(averageRating)));
        when(reviewService.getReviewCount(restaurantId)).thenReturn(reviewCount);

        // when
        // when().thenReturn() 스터빙을 통해,
        // restaurantService.findRestaurant() 에서 필요한 모든 외부 의존성을 미리 정의된 값으로 대체하여 사용함
        // 그렇기 때문에 'RestaurantResponse'에 예상된 값들이 포함되게 된다.
        RestaurantResponse response = restaurantService.findRestaurant(memberId, restaurantId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(restaurantEntity.getName());
        assertThat(response.getDescription()).isEqualTo(restaurantEntity.getDescription());
        assertThat(response.getAverageRating()).isEqualTo(BigDecimal.valueOf(averageRating)); // 4.5
        assertThat(response.getReviewCount()).isEqualTo(3L);
    }

    @Test
    @DisplayName("식당 수정 테스트")
    void restaurant_update() {
        // given
        Long ownerId = 1L;
        Long restaurantId = 1L;
        RestaurantUpdateRequest request = getRestaurantUpdateRequest();

        when(restaurantValidator.checkIfRestaurantPresent(restaurantId)).thenReturn(restaurantEntity);

        // when
        restaurantService.updateRestaurant(ownerId, restaurantId, request);

        // then
        assertThat(request.getName()).isEqualTo(restaurantEntity.getName());
        assertThat(request.getDescription()).isEqualTo(restaurantEntity.getDescription());
        assertThat(request.getPhoneNumber()).isEqualTo(restaurantEntity.getPhoneNumber().getPhoneNumberValue());
        assertThat(request.getFoodType()).isEqualTo(restaurantEntity.getFoodType());
        assertThat(request.getServiceType()).isEqualTo(restaurantEntity.getServiceType());
    }


    // 실제 DB에서 삭제하는 것이 아닌 단순히 메서드 호출된 것만 확인하는 테스트가 과연 의미 있을까...?
    @Test
    @DisplayName("식당 삭제 테스트")
    void restaurant_delete() {
        // given
        Long restaurantId = 1L;

        // when
        restaurantService.deleteRestaurant(restaurantId);

        // then
        verify(restaurantRepository, times(1)).deleteById(restaurantId);
    }

    private RestaurantUpdateRequest getRestaurantUpdateRequest() {
        return RestaurantUpdateRequest.builder()
                .name("식당")
                .description("내용")
                .phoneNumber("02-4321-4321")
                .serviceType(ServiceType.RESERVATION)
                .foodType(FoodType.CHINESE)
                .build();
    }

    private RestaurantCreateRequest getRestaurantCreateRequest() {
        return RestaurantCreateRequest.builder()
                .name("새마을식당")
                .description("식당 소개")
                .address("서울특별시")
                .phoneNumber("02-1234-1234")
                .foodType(FoodType.KOREAN)
                .serviceType(ServiceType.WAITING)
                .build();
    }
}