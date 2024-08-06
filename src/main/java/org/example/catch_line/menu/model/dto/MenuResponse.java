package org.example.catch_line.menu.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MenuResponse {

    private Long menuId;
    private String name;
    private Long price;
}