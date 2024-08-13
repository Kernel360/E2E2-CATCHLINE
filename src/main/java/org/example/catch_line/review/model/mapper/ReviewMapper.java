package org.example.catch_line.review.model.mapper;

import org.example.catch_line.review.model.dto.ReviewResponse;
import org.example.catch_line.review.model.entity.ReviewEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
public class ReviewMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ReviewResponse entityToResponse(ReviewEntity reviewEntity) {
        return ReviewResponse.builder()
                .reviewId(reviewEntity.getReviewId())
                .rating(reviewEntity.getRating())
                .content(reviewEntity.getContent())
                .createdAt(reviewEntity.getCreatedAt().format(formatter))
                .build();
    }
}
