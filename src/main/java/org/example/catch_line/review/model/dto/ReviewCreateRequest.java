package org.example.catch_line.review.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReviewCreateRequest {

    private Integer rating;

    @NotBlank
    @Size(min = 10, message = "최소 10자 이상 작성해야 합니다.")
    private String content;
}
