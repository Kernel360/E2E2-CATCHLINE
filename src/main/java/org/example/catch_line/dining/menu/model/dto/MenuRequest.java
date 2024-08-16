package org.example.catch_line.dining.menu.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MenuRequest {

    private String name;
    private Long price;
}
