package org.example.catch_line.user.owner.service;

import java.math.BigDecimal;

import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.common.kakao.model.dto.KakaoCoordinateResponse;
import org.example.catch_line.common.kakao.service.KakaoAddressService;
import org.example.catch_line.restaurant.model.dto.RestaurantCreateRequest;
import org.example.catch_line.restaurant.model.dto.RestaurantResponse;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.restaurant.model.mapper.RestaurantMapper;
import org.example.catch_line.restaurant.repository.RestaurantRepository;
import org.example.catch_line.restaurant.service.RestaurantHourService;
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
	
	public RestaurantResponse createRestaurant(RestaurantCreateRequest request, Long ownerId) {

		OwnerEntity owner = ownerRepository.findByOwnerId(ownerId).orElseThrow(() -> new IllegalArgumentException("사장님이 존재하지 않습니다"));
		String address = request.getAddress();
		KakaoCoordinateResponse kakaoCoordinateResponse = kakaoAddressService.addressToCoordinate(address);
		BigDecimal longitude = new BigDecimal(kakaoCoordinateResponse.getDocuments().get(0).getRoadAddress().getX());
		BigDecimal latitude = new BigDecimal(kakaoCoordinateResponse.getDocuments().get(0).getRoadAddress().getY());

		RestaurantEntity restaurant = RestaurantMapper.requestToEntity(request, latitude, longitude,owner);

		RestaurantEntity savedEntity = restaurantRepository.save(restaurant);

		restaurantHourService.createRestaurantHour(savedEntity);

		return RestaurantMapper.entityToResponse(savedEntity);

	}


}
