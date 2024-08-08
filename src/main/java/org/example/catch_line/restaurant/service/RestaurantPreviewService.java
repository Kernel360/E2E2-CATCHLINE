package org.example.catch_line.restaurant.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.example.catch_line.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.mapper.RestaurantPreviewMapper;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.example.catch_line.review.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

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

	public Page<RestaurantPreviewResponse> restaurantPreviewPaging(Pageable pageable, String criteria) {
		// int page = pageable.getPageNumber();
		int pageLimit = pageable.getPageSize();

		//식당 엔티티들을 리포지토리에서 Page 속성들과 정렬 방식을 입력해서 restaurants 에 저장
		Page<RestaurantEntity> restaurants = restaurantRepository.findAll(
			PageRequest.of(pageable.getPageNumber(), pageLimit,
				Sort.by(Sort.Direction.DESC, criteria)));

		// restaurants 를 매핑해 restaurantResponse 로 변환했다
		Page<RestaurantPreviewResponse> restaurantPreviewPage = restaurants.map(
			entity -> RestaurantPreviewMapper.entityToResponse(entity,
				reviewService.getAverageRating(entity.getRestaurantId()),
				reviewService.getReviewCount(entity.getRestaurantId())));

		return restaurantPreviewPage;

	}

}
