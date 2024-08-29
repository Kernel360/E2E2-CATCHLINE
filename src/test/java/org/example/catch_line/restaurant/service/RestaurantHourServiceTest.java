package org.example.catch_line.restaurant.service;

import org.example.catch_line.common.constant.DayOfWeeks;
import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantHourRequest;
import org.example.catch_line.dining.restaurant.model.dto.RestaurantHourResponse;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantHourEntity;
import org.example.catch_line.dining.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.dining.restaurant.model.entity.constant.OpenStatus;
import org.example.catch_line.dining.restaurant.model.mapper.RestaurantHourMapper;
import org.example.catch_line.dining.restaurant.repository.RestaurantHourRepository;
import org.example.catch_line.dining.restaurant.service.RestaurantHourService;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantHourServiceTest {

    @Mock
    private RestaurantHourRepository restaurantHourRepository;

    @Mock
    private RestaurantValidator restaurantValidator;

    @Mock
    private RestaurantHourMapper restaurantHourMapper;

    @InjectMocks
    private RestaurantHourService restaurantHourService;

    private RestaurantEntity restaurantEntity;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        OwnerEntity owner = new OwnerEntity("qwer1111", "철수", new Password(passwordEncoder.encode("123qwe!@Q")), new PhoneNumber("010-1111-1111"));

        restaurantEntity = new RestaurantEntity("새마을식당", "백종원의 새마을식당", new Rating(BigDecimal.ZERO), new PhoneNumber("010-2111-1111"),
                FoodType.KOREAN, ServiceType.RESERVATION, owner, new BigDecimal("37.50828251273050000000"), new BigDecimal("127.06548046585200000000"));
    }

    @Test
    @DisplayName("식당 영업 시간 전체 조회 테스트")
    void getAllRestaurantHours_success() {
        when(restaurantHourRepository.findAllByRestaurantRestaurantId(anyLong()))
                .thenReturn(getMockRestaurantHours());

        when(restaurantHourMapper.entityToResponse(any(RestaurantHourEntity.class)))
                .thenReturn(new RestaurantHourResponse(1L, "월요일", LocalTime.of(9, 0), LocalTime.of(18, 0), "OPEN"));

        List<RestaurantHourResponse> result = restaurantHourService.getAllRestaurantHours(1L);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getDayOfWeek()).isEqualTo(DayOfWeeks.MONDAY.getDescription());
        verify(restaurantHourRepository, times(1)).findAllByRestaurantRestaurantId(anyLong());
    }

    @Test
    @DisplayName("오늘 영업 시간 조회 및 상태 업데이트 테스트")
    void getRestaurantHour_success() {
        DayOfWeeks dayOfWeek = DayOfWeeks.MONDAY;
        RestaurantHourEntity mockRestaurantHourEntity = mock(RestaurantHourEntity.class);

        // openTime과 closeTime을 명시적으로 설정
        when(mockRestaurantHourEntity.getOpenTime()).thenReturn(LocalTime.of(9, 0));
        when(mockRestaurantHourEntity.getCloseTime()).thenReturn(LocalTime.of(18, 0));

        when(restaurantHourRepository.findByRestaurant_RestaurantIdAndDayOfWeek(anyLong(), eq(dayOfWeek)))
                .thenReturn(mockRestaurantHourEntity);

        when(restaurantHourMapper.entityToResponse(any(RestaurantHourEntity.class)))
                .thenReturn(new RestaurantHourResponse(1L, "월요일", LocalTime.of(9, 0), LocalTime.of(18, 0), "OPEN"));

        RestaurantHourResponse response = restaurantHourService.getRestaurantHour(1L, dayOfWeek);

        assertThat(response).isNotNull();
        assertThat(response.getDayOfWeek()).isEqualTo(dayOfWeek.getDescription());
        verify(restaurantHourRepository, times(1)).findByRestaurant_RestaurantIdAndDayOfWeek(anyLong(), eq(dayOfWeek));
    }

    @Test
    @DisplayName("식당 영업 시간 생성 테스트")
    void createRestaurantHour_success() {
        restaurantHourService.createRestaurantHour(restaurantEntity);

        verify(restaurantHourRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("식당 영업 시간 업데이트 테스트")
    void updateRestaurantHour_success() {
        RestaurantHourEntity mockRestaurantHourEntity = mock(RestaurantHourEntity.class);

        when(restaurantValidator.checkIfRestaurantHourPresent(anyLong())).thenReturn(mockRestaurantHourEntity);

        RestaurantHourRequest request = new RestaurantHourRequest("월요일", LocalTime.of(9, 0), LocalTime.of(18, 0), "CLOSE");
        restaurantHourService.updateRestaurantHour(1L, request);

        verify(mockRestaurantHourEntity, times(1)).updateRestaurantHourEntity("월요일", LocalTime.of(9, 0), LocalTime.of(18, 0), "CLOSE");
    }

    private List<RestaurantHourEntity> getMockRestaurantHours() {
        List<RestaurantHourEntity> hours = new ArrayList<>();
        hours.add(new RestaurantHourEntity(DayOfWeeks.MONDAY, LocalTime.of(9, 0), LocalTime.of(18, 0), OpenStatus.OPEN, restaurantEntity));
        hours.add(new RestaurantHourEntity(DayOfWeeks.TUESDAY, LocalTime.of(9, 0), LocalTime.of(18, 0), OpenStatus.OPEN, restaurantEntity));
        hours.add(new RestaurantHourEntity(DayOfWeeks.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(18, 0), OpenStatus.OPEN, restaurantEntity));
        return hours;
    }
}
