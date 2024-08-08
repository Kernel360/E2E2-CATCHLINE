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
