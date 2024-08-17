package org.example.catch_line.review.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.example.catch_line.review.model.entity.ReviewEntity;

@Getter
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long reviewId;
    private Long memberId;
    private Integer rating;
    private String content;
    private String createdAt;

}
