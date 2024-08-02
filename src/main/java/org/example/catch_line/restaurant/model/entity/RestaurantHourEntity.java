package org.example.catch_line.restaurant.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "restaurant_hour")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantHourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantHourId;

    private DayOfWeek dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private OpenStatus openStatus;

    @Builder
    public RestaurantHourEntity(DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime, OpenStatus openStatus) {
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.openStatus = openStatus;
    }
}
