package org.example.catch_line.statistics.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;

import java.time.LocalDate;

@Entity
@Table(name = "statistics")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StatisticsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statisticsId;

    private int waitingCount;
    private int reservationCount;

    private LocalDate statisticsDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;

    public StatisticsEntity(int waitingCount, int reservationCount, LocalDate statisticsDate, RestaurantEntity restaurant) {
        this.waitingCount = waitingCount;
        this.reservationCount = reservationCount;
        this.statisticsDate = statisticsDate;
        this.restaurant = restaurant;
    }
}
