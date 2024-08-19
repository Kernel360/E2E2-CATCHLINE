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
import org.example.catch_line.exception.CatchLineException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantHourService {

	private final RestaurantHourRepository restaurantHourRepository;

	// 영업 시간 전체 조회
	public List<RestaurantHourResponse> getAllRestaurantHours(Long restaurantId) {
		List<RestaurantHourEntity> restaurantHourList = restaurantHourRepository.findAllByRestaurantRestaurantId(
			restaurantId);

		log.info("size : {}", restaurantHourList.size());

		return restaurantHourList.stream()
			.map(RestaurantHourMapper::entityToResponse)
			.collect(Collectors.toList());
	}

	// 오늘 영업 시간 조회
	public RestaurantHourResponse getRestaurantHour(Long restaurantId, DayOfWeeks dayOfWeek) {
		RestaurantHourEntity entity = restaurantHourRepository.findByRestaurant_RestaurantIdAndDayOfWeek(restaurantId,
			dayOfWeek);
		return RestaurantHourMapper.entityToResponse(entity);
	}

	// 영업 재개

	// 영업 종료
	public void closeBusiness(Long restaurantHourId) {
		RestaurantHourEntity entity = restaurantHourRepository.findById(restaurantHourId).orElseThrow(() -> {
			throw new IllegalArgumentException("restaurantHourId가 없습니다 : " + restaurantHourId);
		});

		entity.closeBusiness();
	}

	public List<RestaurantHourEntity> createRestaurantHour(RestaurantEntity restaurant) {
		List<RestaurantHourEntity> list = new ArrayList<>();

		DayOfWeeks[] dayOfWeeks = DayOfWeeks.values();

		for (DayOfWeeks dayOfWeek : dayOfWeeks) {
			RestaurantHourEntity restaurantHourEntity = RestaurantHourEntity.builder()
				.dayOfWeek(dayOfWeek)
				.openTime(LocalTime.MIN)
				.closeTime(LocalTime.MAX)
				.openStatus(OpenStatus.OPEN)
				.restaurant(restaurant)
				.build();
			list.add(restaurantHourEntity);
		}

		restaurantHourRepository.saveAll(list);

		return list;

	}

	public void updateRestaurantHour(Long restaurantHourId, RestaurantHourRequest request) {
		RestaurantHourEntity restaurantHourEntity = restaurantHourRepository.findById(restaurantHourId)
			.orElseThrow(() -> new CatchLineException("영업 시간이 존재하지 않습니다"));
		restaurantHourEntity.updateRestaurantHourEntity(request.getDayOfWeek(), request.getOpenTime(),
			request.getCloseTime(), request.getOpenStatus());
		restaurantHourRepository.save(restaurantHourEntity);

	}

}
