package org.example.catch_line.review.model.mapper;

import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.model.entity.ReviewEntity;

import java.math.BigDecimal;

public class ReviewMapper {

    public static ReviewResponse entityToResponseWithAverageRating(ReviewEntity reviewEntity, BigDecimal averageRating) {
        return ReviewResponse.builder()
                .reviewId(reviewEntity.getReviewId())
                .rating(reviewEntity.getRating())
                .content(reviewEntity.getContent())
                .averageRating(averageRating)
                .createdAt(reviewEntity.getCreatedAt())
                .build();
    }

    public static ReviewResponse entityToResponse(ReviewEntity reviewEntity) {
        return ReviewResponse.builder()
                .reviewId(reviewEntity.getReviewId())
                .rating(reviewEntity.getRating())
                .content(reviewEntity.getContent())
                .createdAt(reviewEntity.getCreatedAt())
                .build();
    }
}
