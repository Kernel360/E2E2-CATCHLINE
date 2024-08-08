package org.example.catch_line.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.mapper.RestaurantPreviewMapper;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.example.catch_line.review.service.ReviewService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantPreviewService {

    private final RestaurantRepository restaurantRepository;
    private final ReviewService reviewService;

    // 식당 프리뷰 리스트 조회
    public List<RestaurantPreviewResponse> getRestaurantPreviewList() {
        List<RestaurantEntity> restaurantList = restaurantRepository.findAll();

        return restaurantList.stream()
                .map(restaurantEntity -> {
                    Long restaurantId = restaurantEntity.getRestaurantId();
                    BigDecimal averageRating = reviewService.getAverageRating(restaurantId);
                    Long reviewCount = reviewService.getReviewCount(restaurantId);
                    return RestaurantPreviewMapper.entityToResponse(restaurantEntity, averageRating, reviewCount);
                })
                .collect(Collectors.toList());
    }


}
