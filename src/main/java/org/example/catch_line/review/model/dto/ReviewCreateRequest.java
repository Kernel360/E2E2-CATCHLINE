package org.example.catch_line.review.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReviewCreateRequest {

    private Integer rating;
    private String content;
}
