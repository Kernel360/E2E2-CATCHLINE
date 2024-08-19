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

	// 식당 프리뷰 리스트 조회
	// 변경 사항 : 프리뷰 조회 시 리뷰 수, 평점을 항상 DB에서 조회했음 -> 식당 상세 정보 조회시에만 리뷰 수, 평점 DB에서 조회
	public List<RestaurantPreviewResponse> getRestaurantPreviewList() {
		List<RestaurantEntity> restaurantList = restaurantRepository.findAll();

		return restaurantList.stream()
			.map(RestaurantPreviewMapper::entityToResponse)
			.collect(Collectors.toList());
	}

	public Page<RestaurantPreviewResponse> restaurantPreviewPaging(Pageable pageable, String criteria) {
		int pageLimit = pageable.getPageSize();
		Sort sort = Sort.by(Sort.Direction.DESC, criteria).and(Sort.by(Sort.Direction.DESC, "restaurantId"));
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageLimit, sort);

		//식당 엔티티들을 리포지토리에서 Page 속성들과 정렬 방식을 입력해서 restaurants 에 저장
		Page<RestaurantEntity> restaurants = restaurantRepository.findAll(pageRequest);

		// restaurants 를 매핑해 restaurantResponse 로 변환했다
		return restaurants.map(RestaurantPreviewMapper::entityToResponse);

	}

	public Page<RestaurantPreviewResponse> restaurantPreviewSearchAndPaging(Pageable pageable, String criteria, String type, String keyword) {
		int pageLimit = pageable.getPageSize();
		Sort sort = Sort.by(Sort.Direction.DESC, criteria).and(Sort.by(Sort.Direction.DESC, "restaurantId"));
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageLimit, sort);

		//식당 엔티티들을 리포지토리에서 Page 속성들과 정렬 방식을 입력해서 restaurants 에 저장
		Page<RestaurantEntity> restaurants = switch (type) {
            case "name" -> restaurantRepository.findAllByNameContaining(keyword, pageRequest);
            case "foodType" -> restaurantRepository.findAllByFoodType(FoodType.fromKoreanName(keyword), pageRequest);
            default -> restaurantRepository.findAll(pageRequest);
        };

        // restaurants 를 매핑해 restaurantResponse 로 변환했다
		return restaurants.map(RestaurantPreviewMapper::entityToResponse);
	}

}
