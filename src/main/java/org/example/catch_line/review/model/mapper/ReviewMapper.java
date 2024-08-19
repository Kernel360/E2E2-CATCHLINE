package org.example.catch_line.review.model.mapper;

import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.model.entity.ReviewEntity;
import org.example.catch_line.user.member.model.dto.MyReviewResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Component
public class ReviewMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ReviewResponse entityToResponse(ReviewEntity reviewEntity) {
        return ReviewResponse.builder()
                .reviewId(reviewEntity.getReviewId())
                .restaurantId(reviewEntity.getRestaurant().getRestaurantId())
                .memberId(reviewEntity.getMember().getMemberId())
                .rating(reviewEntity.getRating())
                .content(reviewEntity.getContent())
                .createdAt(reviewEntity.getCreatedAt().format(formatter))
                .build();
    }

    public MyReviewResponse entityToMyResponse(ReviewEntity reviewEntity) {
        return MyReviewResponse.builder()
                .reviewId(reviewEntity.getReviewId())
                .restaurantId(reviewEntity.getRestaurant().getRestaurantId())
                .restaurantName(reviewEntity.getRestaurant().getName())
                .memberId(reviewEntity.getMember().getMemberId())
                .rating(reviewEntity.getRating())
                .content(reviewEntity.getContent())
                .createdAt(reviewEntity.getCreatedAt().format(formatter))
                .build();
    }
}
