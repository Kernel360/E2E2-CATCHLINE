package org.example.catch_line.review.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.exception.authorizaion.UnauthorizedException;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.provider.validation.MemberValidator;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.example.catch_line.review.model.dto.ReviewCreateRequest;
import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.model.entity.ReviewEntity;
import org.example.catch_line.review.model.mapper.ReviewMapper;
import org.example.catch_line.review.repository.ReviewRepository;
import org.example.catch_line.review.validation.ReviewValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final MemberValidator memberValidator;
	private final RestaurantValidator restaurantValidator;
	private final ReviewValidator reviewValidator;
	private final ReviewMapper reviewMapper;

	public List<ReviewResponse> getRestaurantReviewList(Long restaurantId) {
		List<ReviewEntity> reviewList = reviewRepository.findAllByRestaurantRestaurantIdOrderByCreatedAtDesc(restaurantId);

		return reviewList.stream()
			.map(reviewMapper::entityToResponse)
			.collect(Collectors.toList());
	}

	public ReviewResponse getReviewById(Long reviewId, Long memberId) {
		ReviewEntity reviewEntity = reviewValidator.checkIfReviewPresent(reviewId);
		isMemberOfReview(reviewEntity, memberId);

		return reviewMapper.entityToResponse(reviewEntity);
	}

	public ReviewResponse createReview(Long memberId, Long restaurantId, ReviewCreateRequest reviewCreateRequest) {
		MemberEntity memberEntity = memberValidator.checkIfMemberPresent(memberId);
		RestaurantEntity restaurantEntity = restaurantValidator.checkIfRestaurantPresent(restaurantId);

		ReviewEntity reviewEntity = createRequestToEntity(reviewCreateRequest, memberEntity, restaurantEntity);
		ReviewEntity savedEntity = reviewRepository.save(reviewEntity);
		return reviewMapper.entityToResponse(savedEntity);
	}

	public ReviewResponse updateReview(Long reviewId, Long memberId, Integer rating, String content) {
		ReviewEntity reviewEntity = reviewValidator.checkIfReviewPresent(reviewId);
		isMemberOfReview(reviewEntity, memberId);

		reviewEntity.updateContent(rating, content);
		ReviewEntity updatedReview = reviewRepository.save(reviewEntity);
		return reviewMapper.entityToResponse(updatedReview);
	}

	public void deleteReview(Long reviewId, Long memberId) {
		ReviewEntity reviewEntity = reviewValidator.checkIfReviewPresent(reviewId);
		isMemberOfReview(reviewEntity, memberId);
		reviewRepository.deleteById(reviewId);
	}

	public Rating getAverageRating(Long restaurantId) {
		List<Integer> reviewRatingList = reviewRepository.findRatingsByRestaurantId(restaurantId);
		return new Rating(reviewRatingList);
	}

	public Long getReviewCount(Long restaurantId) {
		return reviewRepository.countByRestaurantRestaurantId(restaurantId);
	}

	private ReviewEntity createRequestToEntity(ReviewCreateRequest reviewCreateRequest, MemberEntity memberEntity, RestaurantEntity restaurantEntity) {
		return new ReviewEntity(reviewCreateRequest.getRating(), reviewCreateRequest.getContent(), memberEntity, restaurantEntity);
	}

	private void isMemberOfReview(ReviewEntity reviewEntity, Long memberId) {
		if (!reviewEntity.getMember().getMemberId().equals(memberId)) {
			throw new UnauthorizedException();
		}
	}

}
