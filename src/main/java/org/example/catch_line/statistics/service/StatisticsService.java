package org.example.catch_line.statistics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;
    private final StatisticsMapper statisticsMapper;
    private final RestaurantValidator restaurantValidator;


    public List<StatisticsResponse> getStatisticsList() {
        List<RestaurantEntity> statisticsList = statisticsRepository.findDistinctRestaurants();
        return statisticsList.stream()
                .map(statisticsMapper::entityToResponse)
                .toList();
    }

    public StatisticsGraphResponse getStatisticsByRestaurantId(Long restaurantId) {
        RestaurantEntity restaurantEntity = restaurantValidator.checkIfRestaurantPresent(restaurantId);
        List<StatisticsEntity> statisticsList = statisticsRepository.findByRestaurantRestaurantId(restaurantId);

        List<String> dates = statisticsList.stream()
                .map(date -> date.getStatisticsDate().toString())
                .toList();

        List<Integer> reservationCounts = statisticsList.stream()
                .map(StatisticsEntity::getReservationCount)
                .toList();

        List<Integer> waitingCounts = statisticsList.stream()
                .map(StatisticsEntity::getWaitingCount)
                .toList();

        return statisticsMapper.entityToGraphResponse(dates, reservationCounts, waitingCounts, restaurantEntity.getName(), restaurantEntity.getServiceType().name());
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateScheduledReservation() {
        List<ReservationEntity> reservationEntities = reservationRepository.findAllByStatus(Status.SCHEDULED);

        LocalDateTime now = LocalDateTime.now();
        reservationEntities.stream()
                .filter(reservationEntity -> now.isAfter(reservationEntity.getReservationDate()))
                .forEach(ReservationEntity::canceled);
        reservationRepository.saveAll(reservationEntities);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateScheduledWaiting() {
        List<WaitingEntity> waitingEntities = waitingRepository.findAllByStatus(Status.SCHEDULED);
        waitingEntities.forEach(WaitingEntity::canceled);
        waitingRepository.saveAll(waitingEntities);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void recordDailyStatistics() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startOfDay = yesterday.atStartOfDay();
        LocalDateTime endOfDay = yesterday.plusDays(1).atStartOfDay();

        List<RestaurantEntity> restaurants = restaurantRepository.findAll();
        for (RestaurantEntity restaurant : restaurants) {
            int reservationCount = 0;
            int waitingCount = 0;

            if (restaurant.getServiceType() == ServiceType.RESERVATION) {
                reservationCount = reservationRepository.countByRestaurantAndReservationDateBetween(restaurant, startOfDay, endOfDay);
            }
            if (restaurant.getServiceType() == ServiceType.WAITING) {
                waitingCount = waitingRepository.countByRestaurantAndModifiedAtBetween(restaurant, startOfDay, endOfDay);
            }

            StatisticsEntity statistics = new StatisticsEntity(waitingCount, reservationCount, yesterday, restaurant);
            statisticsRepository.save(statistics);
        }
    }

}
