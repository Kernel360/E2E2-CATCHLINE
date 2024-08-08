package org.example.catch_line.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.mapper.RestaurantPreviewMapper;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantPreviewService {

    private final RestaurantRepository restaurantRepository;

    // 식당 프리뷰 리스트 조회
    // 변경 사항 : 프리뷰 조회 시 리뷰 수, 평점을 항상 DB에서 조회했음 -> 식당 상세 정보 조회시에만 리뷰 수, 평점 DB에서 조회
    public List<RestaurantPreviewResponse> getRestaurantPreviewList() {
        List<RestaurantEntity> restaurantList = restaurantRepository.findAll();

        return restaurantList.stream()
                .map(RestaurantPreviewMapper::entityToResponse)
                .collect(Collectors.toList());
    }


}
