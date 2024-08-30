package org.example.catch_line.dining.restaurant.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.dining.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.dining.restaurant.model.mapper.RestaurantPreviewMapper;
import org.example.catch_line.dining.restaurant.repository.RestaurantRepository;
import org.example.catch_line.review.service.ReviewService;
import org.example.catch_line.scrap.service.ScrapService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantPreviewService {

	private final RestaurantRepository restaurantRepository;
	private final RestaurantPreviewMapper restaurantPreviewMapper;
	private final ReviewService reviewService;
	private final ScrapService scrapService;

	public List<RestaurantPreviewResponse> getRestaurantPreviewList() {
		List<RestaurantEntity> restaurantList = restaurantRepository.findAll();

		return restaurantList.stream()
			.map(restaurantPreviewMapper::entityToResponse)
			.collect(Collectors.toList());
	}

	public Page<RestaurantPreviewResponse> restaurantPreviewSearchAndPaging(Pageable pageable, String criteria, String type, String keyword) {
		Page<RestaurantEntity> restaurants = restaurantRepository.findRestaurantsByCriteria(pageable, criteria, type, keyword);
		return restaurants.map(restaurantPreviewMapper::entityToResponse);
	}

	public Page<RestaurantPreviewResponse> restaurantPreviewPaging(Pageable pageable, String criteria) {
		int pageLimit = pageable.getPageSize();
		Sort sort = Sort.by(Sort.Direction.DESC, criteria).and(Sort.by(Sort.Direction.DESC, "restaurantId"));
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageLimit, sort);

		Page<RestaurantEntity> restaurants = restaurantRepository.findAll(pageRequest);

		return restaurants.map(restaurantPreviewMapper::entityToResponse);

	}

	@Scheduled(cron = "0 0 0/1 * * *")
//	@Scheduled(cron = "0 39 13 * * ?")
	public void scheduleUpdateRestaurantPreview() {
		List<RestaurantEntity> restaurantList = restaurantRepository.findAll();
		restaurantList.forEach(restaurant -> {
					Long restaurantId = restaurant.getRestaurantId();
					BigDecimal averageRating = reviewService.getAverageRating(restaurantId).getRating();
					Long reviewCount = reviewService.getReviewCount(restaurantId);
					Long scrapCount = scrapService.getRestaurantScraps(restaurantId);

					restaurant.updateReview(new Rating(averageRating), reviewCount);
					restaurant.updateScrap(scrapCount);
					restaurantRepository.save(restaurant);
				});
	}

}
