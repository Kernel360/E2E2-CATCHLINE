package org.example.catch_line.review.service;

import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.common.model.entity.BaseTimeEntity;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.user.member.model.provider.validation.MemberValidator;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.example.catch_line.review.model.dto.ReviewCreateRequest;
import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.model.dto.ReviewUpdateRequest;
import org.example.catch_line.review.model.entity.ReviewEntity;
import org.example.catch_line.review.repository.ReviewRepository;
import org.example.catch_line.review.validation.ReviewValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock ReviewRepository reviewRepository;
    @Mock MemberValidator memberValidator;
    @Mock RestaurantValidator restaurantValidator;
    @Mock ReviewValidator reviewValidator;
    @InjectMocks ReviewService reviewService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("식당 별 리뷰 전체 조회 테스트")
    void restaurant_review_list_test() {
        // given
        Long restaurantId = 1L;

        when(reviewRepository.findAllByRestaurantRestaurantIdOrderByCreatedAtDesc(restaurantId)).thenReturn(getReviewList());

        // when
        List<ReviewResponse> reviewList = reviewService.getRestaurantReviewList(restaurantId);

        // then
        assertThat(reviewList).isNotNull();
        assertThat(reviewList).hasSize(3);
        assertThat(reviewList.get(0).getContent()).isEqualTo("맛있어요");
        assertThat(reviewList.get(0).getRating()).isEqualTo(5);
    }

    @Test
    @DisplayName("리뷰 작성 테스트")
    void review_create_test() {
        // given
        Long memberId = 1L;
        Long restaurantId = 1L;
        ReviewCreateRequest request = getCreateRequest();

        when(memberValidator.checkIfMemberPresent(memberId)).thenReturn(getMemberEntity());
        when(restaurantValidator.checkIfRestaurantPresent(restaurantId)).thenReturn(getRestaurantEntity());

        // Mockito의 doAnswer를 사용하여 생성된 ReviewEntity의 createdAt 필드를 설정합니다.
        doAnswer(invocation -> {
            ReviewEntity reviewEntity = invocation.getArgument(0);
            setCreatedAt(reviewEntity, LocalDateTime.now());
            return reviewEntity;
        }).when(reviewRepository).save(any(ReviewEntity.class));

        // when
        ReviewResponse review = reviewService.createReview(memberId, restaurantId, request);

        // then
        assertThat(review).isNotNull();
        assertThat(review.getRating()).isEqualTo(5);
        assertThat(review.getContent()).isEqualTo("맛있어요");
    }

    @Test
    @DisplayName("리뷰 수정 테스트")
    void review_update_test() {
        // given
        Long memberId = 1L;
        Long restaurantId = 1L;
        Long reviewId = 1L;
        ReviewUpdateRequest request = getUpdateRequest();
        ReviewEntity review = getReviewList().get(0);

        when(memberValidator.checkIfMemberPresent(memberId)).thenReturn(getMemberEntity());
        when(restaurantValidator.checkIfRestaurantPresent(restaurantId)).thenReturn(getRestaurantEntity());
        when(reviewValidator.checkIfReviewPresent(restaurantId)).thenReturn(review);

        // when
//        reviewService.updateReview(memberId, request.getRating(), request.getContent());

        // then
        assertThat(request.getContent()).isEqualTo(review.getContent());
    }

    @Test
    @DisplayName("리뷰 삭제 테스트")
    void review_delete_test() {
        // given
        Long reviewId = 1L;

        // when
//        reviewService.deleteReview(reviewId);

        // then
        verify(reviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    @DisplayName("식당 평점 구하기 테스트")
    void review_average_rating_test() {
        // given
        Long restaurantId = 1L;

        when(reviewRepository.findRatingsByRestaurantId(restaurantId)).thenReturn(getSumRating());

        // when
        Rating rating = reviewService.getAverageRating(restaurantId);

        // then
        assertThat(rating.getRating()).isEqualTo(BigDecimal.valueOf(4.7));
    }

    @Test
    @DisplayName("리뷰 전체 수 조회 테스트")
    void review_count_test() {
        // given
        Long restaurantId = 1L;

        // when
        reviewService.getReviewCount(restaurantId);

        // then
        verify(reviewRepository, times(1)).countByRestaurantRestaurantId(restaurantId);
    }

    private void setCreatedAt(ReviewEntity reviewEntity, LocalDateTime createdAt) {
        try {
            Field createdAtField = BaseTimeEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(reviewEntity, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private MemberEntity getMemberEntity() {
        return MemberEntity.builder()
                .email(new Email("abc@gmail.com"))
                .name("홍길동")
                .nickname("hong")
                .password(new Password(passwordEncoder.encode("123qwe!@Q")))
                .phoneNumber(new PhoneNumber("010-1234-1234"))
                .build();
    }

    private RestaurantEntity getRestaurantEntity() {
        return RestaurantEntity.builder()
                .name("새마을식당")
                .description("백종원의 새마을식당")
                .phoneNumber(new PhoneNumber("02-1234-1234"))
                .rating(new Rating(BigDecimal.ZERO))
                .serviceType(ServiceType.WAITING)
                .foodType(FoodType.KOREAN)
                .build();
    }

    private ReviewCreateRequest getCreateRequest() {
        return ReviewCreateRequest.builder()
                .rating(5)
                .content("맛있어요")
                .build();
    }

    private ReviewUpdateRequest getUpdateRequest() {
        return ReviewUpdateRequest.builder()
                .content("수정")
                .build();
    }

    private List<ReviewEntity> getReviewList() {
        List<ReviewEntity> reviewList = new ArrayList<>();

        MemberEntity memberEntity = getMemberEntity();

        LocalDateTime now = LocalDateTime.now();
        RestaurantEntity restaurantEntity = getRestaurantEntity();

        ReviewEntity review1 = ReviewEntity.builder()
                .rating(5)
                .content("맛있어요")
                .member(memberEntity)
                .restaurant(restaurantEntity)
                .build();

        setCreatedAt(review1, now);

        ReviewEntity review2 = ReviewEntity.builder()
                .rating(4)
                .content("맛있어요")
                .member(memberEntity)
                .restaurant(restaurantEntity)
                .build();

        setCreatedAt(review2, now);

        ReviewEntity review3 = ReviewEntity.builder()
                .rating(5)
                .content("맛있어요")
                .member(memberEntity)
                .restaurant(restaurantEntity)
                .build();

        setCreatedAt(review3, now);

        reviewList.add(review1); reviewList.add(review2); reviewList.add(review3);
        return reviewList;
    }

    private List<Integer> getSumRating() {
        List<Integer> sum = new ArrayList<>();
        List<ReviewEntity> reviewList = getReviewList();
        for (ReviewEntity reviewEntity : reviewList) {
            sum.add(reviewEntity.getRating());
        }
        return sum;
    }
}