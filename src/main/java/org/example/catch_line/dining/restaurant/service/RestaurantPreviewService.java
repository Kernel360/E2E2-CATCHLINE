package org.example.catch_line.dining.restaurant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantPreviewResponse;
import org.example.catch_line.dining.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.dining.restaurant.model.mapper.RestaurantPreviewMapper;
import org.example.catch_line.dining.restaurant.repository.RestaurantRepository;
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
	private final RestaurantPreviewMapper restaurantPreviewMapper;

	public List<RestaurantPreviewResponse> getRestaurantPreviewList() {
		List<RestaurantEntity> restaurantList = restaurantRepository.findAll();

		return restaurantList.stream()
			.map(restaurantPreviewMapper::entityToResponse)
			.collect(Collectors.toList());
	}

	public Page<RestaurantPreviewResponse> restaurantPreviewPaging(Pageable pageable, String criteria) {
		int pageLimit = pageable.getPageSize();
		Sort sort = Sort.by(Sort.Direction.DESC, criteria).and(Sort.by(Sort.Direction.DESC, "restaurantId"));
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageLimit, sort);

		Page<RestaurantEntity> restaurants = restaurantRepository.findAll(pageRequest);

		return restaurants.map(restaurantPreviewMapper::entityToResponse);

	}

	public Page<RestaurantPreviewResponse> restaurantPreviewSearchAndPaging(Pageable pageable, String criteria, String type, String keyword) {
		int pageLimit = pageable.getPageSize();
		Sort sort = Sort.by(Sort.Direction.DESC, criteria).and(Sort.by(Sort.Direction.DESC, "restaurantId"));
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageLimit, sort);

		Page<RestaurantEntity> restaurants = switch (type) {
            case "name" -> restaurantRepository.findAllByNameContaining(keyword, pageRequest);
            case "foodType" -> restaurantRepository.findAllByFoodType(FoodType.fromKoreanName(keyword), pageRequest);
            default -> restaurantRepository.findAll(pageRequest);
        };

		return restaurants.map(restaurantPreviewMapper::entityToResponse);
	}

}
