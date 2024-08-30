package org.example.catch_line.statistics.service;

import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.booking.reservation.repository.ReservationRepository;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.booking.waiting.repository.WaitingRepository;
import org.example.catch_line.common.constant.ServiceType;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.repository.RestaurantRepository;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.example.catch_line.statistics.model.dto.StatisticsGraphResponse;
import org.example.catch_line.statistics.model.dto.StatisticsResponse;
import org.example.catch_line.statistics.model.entity.StatisticsEntity;
import org.example.catch_line.statistics.model.mapper.StatisticsMapper;
import org.example.catch_line.statistics.repository.StatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatisticsServiceTest {

    @Mock
    private StatisticsRepository statisticsRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private WaitingRepository waitingRepository;

    @Mock
    private StatisticsMapper statisticsMapper;

    @Mock
    private RestaurantValidator restaurantValidator;

    @InjectMocks
    private StatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStatisticsList() {
        RestaurantEntity restaurant = mock(RestaurantEntity.class);
        StatisticsResponse response = mock(StatisticsResponse.class);

        when(statisticsRepository.findDistinctRestaurants()).thenReturn(List.of(restaurant));
        when(statisticsMapper.entityToResponse(restaurant)).thenReturn(response);

        List<StatisticsResponse> result = statisticsService.getStatisticsList();

        assertEquals(1, result.size());
        verify(statisticsMapper).entityToResponse(restaurant);
    }

    @Test
    void testGetStatisticsByRestaurantId() {
        Long restaurantId = 1L;
        RestaurantEntity restaurant = mock(RestaurantEntity.class);
        StatisticsEntity statistics = mock(StatisticsEntity.class);
        StatisticsGraphResponse graphResponse = mock(StatisticsGraphResponse.class);

        // Mocking necessary methods
        when(restaurantValidator.checkIfRestaurantPresent(restaurantId)).thenReturn(restaurant);
        when(statisticsRepository.findByRestaurantRestaurantId(restaurantId)).thenReturn(List.of(statistics));
        when(statisticsMapper.entityToGraphResponse(any(), any(), any(), any(), any())).thenReturn(graphResponse);

        // Setting up the mock for statistics
        when(statistics.getStatisticsDate()).thenReturn(LocalDate.now());
        when(statistics.getReservationCount()).thenReturn(10);
        when(statistics.getWaitingCount()).thenReturn(5);

        // Setting up the mock for restaurant entity
        when(restaurant.getName()).thenReturn("Test Restaurant");
        when(restaurant.getServiceType()).thenReturn(ServiceType.RESERVATION);

        StatisticsGraphResponse result = statisticsService.getStatisticsByRestaurantId(restaurantId);

        assertEquals(graphResponse, result);
        verify(statisticsMapper).entityToGraphResponse(any(), any(), any(), any(), any());
    }

    @Test
    void testUpdateScheduledReservation() {
        ReservationEntity reservation = mock(ReservationEntity.class);
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

        when(reservationRepository.findAllByStatus(Status.SCHEDULED)).thenReturn(List.of(reservation));
        when(reservation.getReservationDate()).thenReturn(pastDate);

        statisticsService.updateScheduledReservation();

        verify(reservation).canceled();
        verify(reservationRepository).saveAll(any());
    }

    @Test
    void testUpdateScheduledWaiting() {
        WaitingEntity waiting = mock(WaitingEntity.class);

        when(waitingRepository.findAllByStatus(Status.SCHEDULED)).thenReturn(List.of(waiting));

        statisticsService.updateScheduledWaiting();

        verify(waiting).canceled();
        verify(waitingRepository).saveAll(any());
    }

    @Test
    void testRecordDailyStatistics() {
        RestaurantEntity restaurant = mock(RestaurantEntity.class);
        StatisticsEntity statistics = mock(StatisticsEntity.class);

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startOfDay = yesterday.atStartOfDay();
        LocalDateTime endOfDay = yesterday.plusDays(1).atStartOfDay();

        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));
        when(restaurant.getServiceType()).thenReturn(ServiceType.RESERVATION);
        when(reservationRepository.countByRestaurantAndReservationDateBetween(restaurant, startOfDay, endOfDay)).thenReturn(5);

        statisticsService.recordDailyStatistics();

        verify(statisticsRepository).save(any(StatisticsEntity.class));
    }
}
