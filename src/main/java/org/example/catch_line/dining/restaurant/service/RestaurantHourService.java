package org.example.catch_line.dining.restaurant.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.example.catch_line.common.constant.DayOfWeeks;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantHourRequest;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantHourResponse;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantHourEntity;
import org.example.catch_line.dining.restaurant.model.entity.constant.OpenStatus;
import org.example.catch_line.dining.restaurant.model.mapper.RestaurantHourMapper;
import org.example.catch_line.dining.restaurant.repository.RestaurantHourRepository;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantHourService {

	private final RestaurantHourRepository restaurantHourRepository;
	private final RestaurantValidator restaurantValidator;
	private final RestaurantHourMapper restaurantHourMapper;

	public List<RestaurantHourResponse> getAllRestaurantHours(Long restaurantId) {
		List<RestaurantHourEntity> restaurantHourList = restaurantHourRepository.findAllByRestaurantRestaurantId(restaurantId);

		return restaurantHourList.stream()
			.map(restaurantHourMapper::entityToResponse)
			.collect(Collectors.toList());
	}

	@Transactional
	public RestaurantHourResponse getRestaurantHour(Long restaurantId, DayOfWeeks dayOfWeek) {
		RestaurantHourEntity entity = restaurantHourRepository.findByRestaurant_RestaurantIdAndDayOfWeek(restaurantId, dayOfWeek);

		if (LocalTime.now().isBefore(entity.getCloseTime()) && LocalTime.now().isAfter(entity.getOpenTime())) {
			entity.updateOpenStatus(OpenStatus.OPEN);
		} else {
			entity.updateOpenStatus(OpenStatus.CLOSE);
		}

		return restaurantHourMapper.entityToResponse(entity);
	}

	public void createRestaurantHour(RestaurantEntity restaurant) {
		List<RestaurantHourEntity> list = new ArrayList<>();
		DayOfWeeks[] dayOfWeeks = DayOfWeeks.values();

		for (DayOfWeeks dayOfWeek : dayOfWeeks) {
			RestaurantHourEntity restaurantHourEntity = createRestaurantHourEntity(restaurant, dayOfWeek);
			list.add(restaurantHourEntity);
		}
		restaurantHourRepository.saveAll(list);
	}

	@Transactional
	public void updateRestaurantHour(Long restaurantHourId, RestaurantHourRequest request) {
		RestaurantHourEntity restaurantHourEntity = restaurantValidator.checkIfRestaurantHourPresent(restaurantHourId);
		restaurantHourEntity.updateRestaurantHourEntity(request.getDayOfWeek(), request.getOpenTime(),
				request.getCloseTime(), request.getOpenStatus());
	}

	private RestaurantHourEntity createRestaurantHourEntity(RestaurantEntity restaurant, DayOfWeeks dayOfWeek) {
		return new RestaurantHourEntity(dayOfWeek, LocalTime.MIN, LocalTime.MAX, OpenStatus.OPEN, restaurant);
	}

}
