package org.example.catch_line.review.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.validation.MemberValidator;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.validation.RestaurantValidator;
import org.example.catch_line.review.model.dto.ReviewCreateRequest;
import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.model.dto.ReviewUpdateRequest;
import org.example.catch_line.review.model.entity.ReviewEntity;
import org.example.catch_line.review.model.mapper.ReviewMapper;
import org.example.catch_line.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        return reviewList.stream()
                .map(ReviewMapper::entityToResponse)
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

    // 식당 평점 구하기
    public Rating getAverageRating(Long restaurantId) {
        List<Integer> reviewRatingList = reviewRepository.findRatingsByRestaurantId(restaurantId);
        return new Rating(reviewRatingList);
    }

    // 리뷰 전체 수 조회
    public Long getReviewCount(Long restaurantId) {
        return reviewRepository.countByRestaurantRestaurantId(restaurantId);
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
