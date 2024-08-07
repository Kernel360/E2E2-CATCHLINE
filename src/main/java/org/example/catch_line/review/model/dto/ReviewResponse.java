package org.example.catch_line.review.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long reviewId;
    private Integer rating;
    private BigDecimal averageRating;
    private String content;
    private LocalDateTime createdAt;
}
