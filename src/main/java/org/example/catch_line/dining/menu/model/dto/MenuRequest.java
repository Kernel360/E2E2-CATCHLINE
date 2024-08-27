package org.example.catch_line.dining.menu.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MenuRequest {

    @NotBlank(message = "메뉴의 이름은 비어있을 수 없습니다.")
    private String name;

    @NotNull(message = "메뉴 가격은 비어있을 수 없습니다.")
    @Positive(message = "가격은 양수여야 합니다.")
    private Long price;
}
