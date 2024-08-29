package org.example.catch_line.review.service;

import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.example.catch_line.exception.authorizaion.UnauthorizedException;
import org.example.catch_line.review.model.dto.ReviewCreateRequest;
import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.model.entity.ReviewEntity;
import org.example.catch_line.review.model.mapper.ReviewMapper;
import org.example.catch_line.review.repository.ReviewRepository;
import org.example.catch_line.review.validation.ReviewValidator;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.provider.validation.MemberValidator;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MemberValidator memberValidator;

    @Mock
    private RestaurantValidator restaurantValidator;

    @Mock
    private ReviewValidator reviewValidator;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private MemberEntity memberEntity;
    private RestaurantEntity restaurantEntity;
    private ReviewEntity reviewEntity;

    @BeforeEach
    void setUp() {
        OwnerEntity owner = new OwnerEntity("qwer1111", "철수", new Password(passwordEncoder.encode("123qwe!@Q")), new PhoneNumber("010-1111-1111"));

        restaurantEntity = new RestaurantEntity("새마을식당", "백종원의 새마을식당", new Rating(BigDecimal.ZERO), new PhoneNumber("010-2111-1111"),
                FoodType.KOREAN, ServiceType.RESERVATION, owner, new BigDecimal("37.50828251273050000000"), new BigDecimal("127.06548046585200000000"));

        memberEntity = new MemberEntity(new Email("abc@gmail.com"), "홍길동", "hong", new Password(passwordEncoder.encode("123qwe!@Q")),
                new PhoneNumber("010-1234-1234"));
        reviewEntity = new ReviewEntity(5, "Great food!", memberEntity, restaurantEntity);

    }

    @Test
    @DisplayName("식당 리뷰 목록 조회 테스트")
    void getRestaurantReviewList_success() {
        // given
        when(reviewRepository.findAllByRestaurantRestaurantIdOrderByCreatedAtDesc(anyLong())).thenReturn(Arrays.asList(reviewEntity));
        when(reviewMapper.entityToResponse(any(ReviewEntity.class))).thenReturn(
                ReviewResponse.builder()
                        .reviewId(reviewEntity.getReviewId())
                        .memberId(memberEntity.getMemberId())
                        .restaurantId(restaurantEntity.getRestaurantId())
                        .rating(reviewEntity.getRating())
                        .content(reviewEntity.getContent())
                        .build()
        );

        // when
        List<ReviewResponse> reviewList = reviewService.getRestaurantReviewList(1L);

        // then
        assertThat(reviewList).isNotNull();
        assertThat(reviewList.size()).isEqualTo(1);
        verify(reviewRepository, times(1)).findAllByRestaurantRestaurantIdOrderByCreatedAtDesc(anyLong());
    }

    @Test
    @DisplayName("리뷰 ID로 리뷰 조회 테스트")
    void getReviewById_success() {
        // given
        when(reviewValidator.checkIfReviewPresent(anyLong())).thenReturn(reviewEntity);
        when(reviewMapper.entityToResponse(any(ReviewEntity.class))).thenReturn(
                ReviewResponse.builder()
                        .reviewId(reviewEntity.getReviewId())
                        .memberId(memberEntity.getMemberId())
                        .restaurantId(restaurantEntity.getRestaurantId())
                        .rating(reviewEntity.getRating())
                        .content(reviewEntity.getContent())
                        .build()
        );

        // when
        ReviewResponse reviewResponse = reviewService.getReviewById(1L, memberEntity.getMemberId());

        // then
        assertThat(reviewResponse).isNotNull();
        assertThat(reviewResponse.getReviewId()).isEqualTo(reviewEntity.getReviewId());
        assertThat(reviewResponse.getRating()).isEqualTo(reviewEntity.getRating());
        verify(reviewValidator, times(1)).checkIfReviewPresent(anyLong());
        verify(reviewMapper, times(1)).entityToResponse(any(ReviewEntity.class));
    }

    @Test
    @DisplayName("리뷰 생성 테스트")
    void createReview_success() {
        // given
        ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(5, "Awesome place!");
        when(memberValidator.checkIfMemberPresent(anyLong())).thenReturn(memberEntity);
        when(restaurantValidator.checkIfRestaurantPresent(anyLong())).thenReturn(restaurantEntity);
        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(reviewEntity);
        when(reviewMapper.entityToResponse(any(ReviewEntity.class))).thenReturn(
                ReviewResponse.builder()
                        .reviewId(reviewEntity.getReviewId())
                        .memberId(memberEntity.getMemberId())
                        .restaurantId(restaurantEntity.getRestaurantId())
                        .rating(reviewEntity.getRating())
                        .content(reviewEntity.getContent())
                        .build()
        );

        // when
        ReviewResponse reviewResponse = reviewService.createReview(1L, 1L, reviewCreateRequest);

        // then
        assertThat(reviewResponse).isNotNull();
        verify(reviewRepository, times(1)).save(any(ReviewEntity.class));
        verify(reviewMapper, times(1)).entityToResponse(any(ReviewEntity.class));
    }

    @Test
    @DisplayName("리뷰 수정 테스트")
    void updateReview_success() {
        // given
        when(reviewValidator.checkIfReviewPresent(anyLong())).thenReturn(reviewEntity);
        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(reviewEntity);
        when(reviewMapper.entityToResponse(any(ReviewEntity.class))).thenReturn(
                ReviewResponse.builder()
                        .reviewId(reviewEntity.getReviewId())
                        .memberId(memberEntity.getMemberId())
                        .restaurantId(restaurantEntity.getRestaurantId())
                        .rating(reviewEntity.getRating())
                        .content("Good food!")
                        .build()
        );

        // when
        ReviewResponse reviewResponse = reviewService.updateReview(1L, memberEntity.getMemberId(), 4, "Good food!");

        // then
        assertThat(reviewResponse).isNotNull();
        assertThat(reviewResponse.getContent()).isEqualTo("Good food!");
        verify(reviewRepository, times(1)).save(any(ReviewEntity.class));
        verify(reviewMapper, times(1)).entityToResponse(any(ReviewEntity.class));
    }

    @Test
    @DisplayName("리뷰 삭제 테스트")
    void deleteReview_success() {
        // given
        when(reviewValidator.checkIfReviewPresent(anyLong())).thenReturn(reviewEntity);

        // when
        reviewService.deleteReview(1L, memberEntity.getMemberId());

        // then
        verify(reviewRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("식당 평균 평점 조회 테스트")
    void getAverageRating_success() {
        // given
        when(reviewRepository.findRatingsByRestaurantId(anyLong())).thenReturn(Arrays.asList(5, 4, 3));

        // when
        Rating rating = reviewService.getAverageRating(1L);

        // then
        assertThat(rating).isNotNull();
        assertThat(rating.getRating()).isEqualTo(BigDecimal.valueOf(4.0));
        verify(reviewRepository, times(1)).findRatingsByRestaurantId(anyLong());
    }

    @Test
    @DisplayName("식당 리뷰 개수 조회 테스트")
    void getReviewCount_success() {
        // given
        when(reviewRepository.countByRestaurantRestaurantId(anyLong())).thenReturn(10L);

        // when
        Long reviewCount = reviewService.getReviewCount(1L);

        // then
        assertThat(reviewCount).isEqualTo(10L);
        verify(reviewRepository, times(1)).countByRestaurantRestaurantId(anyLong());
    }


}
