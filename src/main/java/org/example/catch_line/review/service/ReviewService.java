package org.example.catch_line.review.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.validate.MemberValidator;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.validate.RestaurantValidator;
import org.example.catch_line.review.model.dto.ReviewCreateRequest;
import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.model.dto.ReviewUpdateRequest;
import org.example.catch_line.review.model.entity.ReviewEntity;
import org.example.catch_line.review.model.mapper.ReviewMapper;
import org.example.catch_line.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberValidator memberValidator;
    private final RestaurantValidator restaurantValidator;

    // 식당 별 리뷰 전체 조회
    public List<ReviewResponse> getRestaurantReviewList(Long restaurantId) {
        List<ReviewEntity> reviewList = reviewRepository.findAllByRestaurantRestaurantIdOrderByCreatedAtDesc(restaurantId);

        BigDecimal totalRating = BigDecimal.ZERO;
        for (ReviewEntity reviewEntity : reviewList) {
            totalRating = totalRating.add(BigDecimal.valueOf(reviewEntity.getRating()));
        }

        BigDecimal averageRating = reviewList.isEmpty() ? BigDecimal.ZERO :
                totalRating.divide(BigDecimal.valueOf(reviewList.size()), 1, BigDecimal.ROUND_HALF_UP);

        BigDecimal rating = averageRating.setScale(1, BigDecimal.ROUND_HALF_UP);

        return reviewList.stream()
                .map(reviewEntity -> ReviewMapper.entityToResponseWithAverageRating(reviewEntity, rating))
                .collect(Collectors.toList());
    }

    // 리뷰 작성
    public ReviewResponse createReview(Long memberId, Long restaurantId, ReviewCreateRequest reviewCreateRequest) {
        MemberEntity memberEntity = memberValidator.checkIfMemberPresent(memberId);
        RestaurantEntity restaurantEntity = restaurantValidator.checkIfRestaurantPresent(restaurantId);

        ReviewEntity reviewEntity = createRequestToEntity(reviewCreateRequest, memberEntity, restaurantEntity);
        return ReviewMapper.entityToResponse(reviewEntity);
    }

    // 리뷰 수정
    public void updateReview(Long memberId, Long restaurantId, ReviewUpdateRequest reviewUpdateRequest) {
        MemberEntity memberEntity = memberValidator.checkIfMemberPresent(memberId);
        RestaurantEntity restaurantEntity = restaurantValidator.checkIfRestaurantPresent(restaurantId);

        ReviewEntity reviewEntity = updateRequestToEntity(reviewUpdateRequest, memberEntity, restaurantEntity);
        reviewEntity.updateContent(reviewUpdateRequest.getContent());
//        reviewRepository.save(reviewEntity);
    }

    // 리뷰 삭제
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    private ReviewEntity createRequestToEntity(ReviewCreateRequest reviewCreateRequest, MemberEntity memberEntity, RestaurantEntity restaurantEntity) {
        return ReviewEntity.builder()
                .rating(reviewCreateRequest.getRating())
                .content(reviewCreateRequest.getContent())
                .member(memberEntity)
                .restaurant(restaurantEntity)
                .build();
    }

    private ReviewEntity updateRequestToEntity(ReviewUpdateRequest reviewUpdateRequest, MemberEntity memberEntity, RestaurantEntity restaurantEntity) {
        return ReviewEntity.builder()
                .content(reviewUpdateRequest.getContent())
                .member(memberEntity)
                .restaurant(restaurantEntity)
                .build();
    }
}
