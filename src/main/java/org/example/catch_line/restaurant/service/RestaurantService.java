package org.example.catch_line.restaurant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.scrap.service.ScrapService;
import org.example.catch_line.user.member.model.vo.PhoneNumber;
import org.example.catch_line.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.dto.RestaurantUpdateRequest;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.mapper.RestaurantMapper;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.example.catch_line.restaurant.validation.RestaurantValidator;
import org.example.catch_line.review.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantValidator restaurantValidator;
    private final ReviewService reviewService;
    private final ScrapService scrapService;

    public RestaurantResponse createRestaurant(RestaurantCreateRequest request) {
        if(restaurantRepository.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("중복된 식당 이름입니다.");
        }

        RestaurantEntity entity = RestaurantMapper.requestToEntity(request);
        RestaurantEntity savedEntity = restaurantRepository.save(entity);
        return RestaurantMapper.entityToResponse(savedEntity);
    }

    // TODO: 현재 식당 상세 정보 조회 시 리뷰 수, 평균 평점을 구하기 위해 리뷰 테이블에서 실시간으로 가져오는 중
    // TODO: 추후 DB에서 실시간으로 데이터 가져오지 않고, 특정 시간마다(10초?) update하는 식으로 변경 필요
    // TODO: 더 나은 성능과 최신성을 위해 배치 처리? Redis?
    @Transactional
    public RestaurantResponse findRestaurant(Long restaurantId) {
        RestaurantEntity entity = restaurantValidator.checkIfRestaurantPresent(restaurantId);
        BigDecimal averageRating = reviewService.getAverageRating(restaurantId).getRating();
        Long reviewCount = reviewService.getReviewCount(restaurantId);
        Long scrapCount = scrapService.getRestaurantScraps(restaurantId);

        entity.updateReview(new Rating(averageRating), reviewCount);
        entity.updateScrap(scrapCount);
        return RestaurantMapper.entityToResponse(entity);
    }

//    public List<RestaurantResponse> findAllRestaurantByOwner(Long memberId, Role role) {
//
//    }

    // TODO: 사장님 ID 넣어줘야 함.
    public void updateRestaurant(Long restaurantId, RestaurantUpdateRequest request) {
        RestaurantEntity entity = restaurantValidator.checkIfRestaurantPresent(restaurantId);
        entity.updateReservation(request.getName(), request.getDescription(), new PhoneNumber(request.getPhoneNumber()), request.getFoodType(), request.getServiceType());
    }

    public void deleteRestaurant(Long restaurantId) {
        restaurantRepository.deleteById(restaurantId);
    }



}
