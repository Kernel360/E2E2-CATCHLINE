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

	// 식당 프리뷰(홈 화면)를 조회할 때마다 DB에서 리뷰, 스크랩 데이터를 조회하고, 업데이트를 하면 서버의 부하가 클 것이라 예상하였습니다.
	// 그렇기 때문에 조회할 때마다 업데이트가 아닌 스케줄러를 이용해서 1시간 마다 업데이트하는 방식으로 진행하고 있습니다.
	// 이 방식보다 더 효율적이고 좋은 방안이 있을까요?
	// 또한 홈 화면의 식당 프리뷰 조회 시마다 DB의 데이터를 계속 조회하는 것이 아닌 다른 좋은 방안이 있을까요? 떠오르는 것은 캐시를 활용하면 될 것 같습니다.
	@Scheduled(cron = "0 0 0/1 * * *")
//	@Scheduled(cron = "0 0 0 * * ?")
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
