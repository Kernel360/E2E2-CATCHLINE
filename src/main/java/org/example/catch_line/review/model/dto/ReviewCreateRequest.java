package org.example.catch_line.review.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Builder
public class ReviewCreateRequest {

    private Integer rating;
    private String content;
}
