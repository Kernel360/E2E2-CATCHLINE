package org.example.catch_line.user.owner.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.example.catch_line.common.constant.Status;
import org.example.catch_line.common.kakao.model.dto.KakaoCoordinateResponse;
import org.example.catch_line.common.kakao.service.KakaoAddressService;
import org.example.catch_line.history.model.dto.HistoryResponse;
import org.example.catch_line.history.service.HistoryService;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.mapper.RestaurantMapper;
import org.example.catch_line.dining.restaurant.repository.RestaurantRepository;
import org.example.catch_line.dining.restaurant.service.RestaurantHourService;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.example.catch_line.user.owner.validation.OwnerValidator;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OwnerService {

	private final KakaoAddressService kakaoAddressService;
	private final RestaurantRepository restaurantRepository;
	private final RestaurantHourService restaurantHourService;
	private final RestaurantMapper restaurantMapper;
	private final OwnerValidator ownerValidator;
	private final HistoryService historyService;

	public RestaurantResponse createRestaurant(RestaurantCreateRequest request, Long ownerId) {
		OwnerEntity owner = ownerValidator.checkIfOwnerIdExist(ownerId);
		String address = request.getAddress();
		KakaoCoordinateResponse kakaoCoordinateResponse = kakaoAddressService.addressToCoordinate(address);
		BigDecimal longitude = new BigDecimal(kakaoCoordinateResponse.getDocuments().get(0).getRoadAddress().getX());
		BigDecimal latitude = new BigDecimal(kakaoCoordinateResponse.getDocuments().get(0).getRoadAddress().getY());

		RestaurantEntity restaurant = restaurantMapper.requestToEntity(request, owner, latitude, longitude);
		RestaurantEntity savedEntity = restaurantRepository.save(restaurant);
		restaurantHourService.createRestaurantHour(savedEntity);

		return restaurantMapper.entityToResponse(savedEntity);
	}

	public List<RestaurantResponse> findAllRestaurantByOwnerId(Long ownerId) {
		List<RestaurantEntity> restaurantEntityList = restaurantRepository.findAllByOwnerOwnerId(ownerId);
		return restaurantEntityList.stream().map(restaurantMapper::entityToResponse).collect(Collectors.toList());
	}

	public List<HistoryResponse> findHistoryByRestaurantIdAndStatus(Long restaurantId, Status status) {
        return historyService.findByRestaurantId(restaurantId,status);
	}

}
