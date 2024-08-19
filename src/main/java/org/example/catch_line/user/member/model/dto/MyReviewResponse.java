package org.example.catch_line.user.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyReviewResponse {

    private Long reviewId;
    private Long memberId;
    private Long restaurantId;
    private String restaurantName;
    private Integer rating;
    private String content;
    private String createdAt;
}
