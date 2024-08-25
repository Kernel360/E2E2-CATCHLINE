package org.example.catch_line.restaurant.service;

import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantHourResponse;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantHourEntity;
import org.example.catch_line.common.constant.DayOfWeeks;
import org.example.catch_line.dining.restaurant.model.entity.constant.OpenStatus;
import org.example.catch_line.dining.restaurant.repository.RestaurantHourRepository;
import org.example.catch_line.dining.restaurant.service.RestaurantHourService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RestaurantHourServiceTest {

    @Mock RestaurantHourRepository restaurantHourRepository;
    @InjectMocks
    RestaurantHourService restaurantHourService;

    @Test
    @DisplayName("식당 영업 시간 전체 조회 테스트")
    void restaurant_hours_test() {
        // given
        Long restaurantId = 1L;

        when(restaurantHourRepository.findAllByRestaurantRestaurantId(restaurantId)).thenReturn(getRestaurantHourResponseList());

        // when
        List<RestaurantHourResponse> hourList = restaurantHourService.getAllRestaurantHours(restaurantId);

        // then
        assertThat(hourList).isNotNull();
        assertThat(hourList.size()).isGreaterThan(0);

        assertThat(hourList.get(0).getDayOfWeek()).isEqualTo(DayOfWeeks.MONDAY.getDescription());
        assertThat(hourList.get(1).getDayOfWeek()).isEqualTo(DayOfWeeks.TUESDAY.getDescription());
        assertThat(hourList.get(2).getDayOfWeek()).isEqualTo(DayOfWeeks.WEDNESDAY.getDescription());
    }

    @Test
    @DisplayName("오늘 영업 시간 조회 테스트")
    void restaurant_hour_test() {
        // given
        Long restaurantId = 1L;
        DayOfWeeks dayOfWeek = DayOfWeeks.MONDAY;
        RestaurantHourEntity hourEntity = getRestaurantHourResponseList().get(0);

        when(restaurantHourRepository.findByRestaurant_RestaurantIdAndDayOfWeek(restaurantId, dayOfWeek)).thenReturn(hourEntity);

        // when
        RestaurantHourResponse response = restaurantHourService.getRestaurantHour(restaurantId, dayOfWeek);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getDayOfWeek()).isEqualTo(dayOfWeek.getDescription());
    }

    @Test
    @DisplayName("영업 종료 테스트")
    void restaurant_close_test() {
        // given
        Long restaurantHourId = 1L;
        RestaurantHourEntity hourEntity = getRestaurantHourResponseList().get(0);

        when(restaurantHourRepository.findById(restaurantHourId)).thenReturn(Optional.of(hourEntity));

        // when

        // then
        assertThat(hourEntity.getOpenStatus()).isEqualTo(OpenStatus.CLOSE);
        verify(restaurantHourRepository, times(1)).findById(restaurantHourId);  // findById가 한 번 호출되었는지 확인
    }

    private List<RestaurantHourEntity> getRestaurantHourResponseList() {
        List<RestaurantHourEntity> list = new ArrayList<>();

        RestaurantHourEntity hour1 = new RestaurantHourEntity(DayOfWeeks.MONDAY, LocalTime.now(), LocalTime.now(), OpenStatus.OPEN);


        RestaurantHourEntity hour2 = RestaurantHourEntity.builder()
                .dayOfWeek(DayOfWeeks.TUESDAY)
                .openTime(LocalTime.now())
                .closeTime(LocalTime.now())
                .openStatus(OpenStatus.OPEN)
                .build();

        RestaurantHourEntity hour3 = RestaurantHourEntity.builder()
                .dayOfWeek(DayOfWeeks.WEDNESDAY)
                .openTime(LocalTime.now())
                .closeTime(LocalTime.now())
                .openStatus(OpenStatus.OPEN)
                .build();

        list.add(hour1); list.add(hour2); list.add(hour3);
        return list;
    }

}
