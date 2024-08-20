package org.example.catch_line.user.owner.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.example.catch_line.common.constant.Status;
import org.example.catch_line.common.kakao.model.dto.KakaoCoordinateResponse;
import org.example.catch_line.common.kakao.service.KakaoAddressService;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.history.service.HistoryService;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantHourResponse;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantHourEntity;
import org.example.catch_line.dining.restaurant.model.mapper.RestaurantHourMapper;
import org.example.catch_line.dining.restaurant.model.mapper.RestaurantMapper;
import org.example.catch_line.dining.restaurant.repository.RestaurantHourRepository;
import org.example.catch_line.dining.restaurant.repository.RestaurantRepository;
import org.example.catch_line.dining.restaurant.service.RestaurantHourService;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.example.catch_line.user.owner.repository.OwnerRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OwnerService {

	private final OwnerRepository ownerRepository;
	private final KakaoAddressService kakaoAddressService;
	private final RestaurantRepository restaurantRepository;
	private final RestaurantHourService restaurantHourService;
	private final RestaurantHourRepository restaurantHourRepository;
	private final HistoryService historyService;
	
	public RestaurantResponse createRestaurant(RestaurantCreateRequest request, Long ownerId) {

		OwnerEntity owner = ownerRepository.findByOwnerId(ownerId).orElseThrow(() -> new CatchLineException("사장님이 존재하지 않습니다"));
		String address = request.getAddress();
		KakaoCoordinateResponse kakaoCoordinateResponse = kakaoAddressService.addressToCoordinate(address);
		BigDecimal longitude = new BigDecimal(kakaoCoordinateResponse.getDocuments().get(0).getRoadAddress().getX());
		BigDecimal latitude = new BigDecimal(kakaoCoordinateResponse.getDocuments().get(0).getRoadAddress().getY());

		RestaurantEntity restaurant = RestaurantMapper.requestToEntity(request, latitude, longitude,owner);

		RestaurantEntity savedEntity = restaurantRepository.save(restaurant);

		restaurantHourService.createRestaurantHour(savedEntity);

		return RestaurantMapper.entityToResponse(savedEntity);

	}

	public List<RestaurantResponse> findAllRestaurantByOwnerId(Long ownerId) {

		List<RestaurantEntity> restaurantEntityList = restaurantRepository.findAllByOwnerOwnerId(ownerId);


		return restaurantEntityList.stream().map(RestaurantMapper::entityToResponse).collect(Collectors.toList());

	}

	public List<RestaurantHourResponse> findRestaurantHourByRestaurantId(Long restaurantId) {
		List<RestaurantHourEntity> restaurantHourEntities = restaurantHourRepository.findAllByRestaurantRestaurantId(
			restaurantId);

		return restaurantHourEntities.stream()
			.map(RestaurantHourMapper::entityToResponse)
			.collect(Collectors.toList());
	}

	public List<HistoryResponse> findHistoryByRestaurantIdAndStatus(Long restaurantId, Status status) {
		List<HistoryResponse> historyResponses = historyService.findByRestaurantId(restaurantId,status);

		return historyResponses;

	}

}
