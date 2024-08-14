package org.example.catch_line.restaurant.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.catch_line.restaurant.model.entity.constant.DayOfWeeks;
import org.example.catch_line.restaurant.model.entity.constant.OpenStatus;

import java.time.LocalTime;

@Entity
@Table(name = "restaurant_hour")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantHourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantHourId;

    @Enumerated(EnumType.STRING)
    private DayOfWeeks dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpenStatus openStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;

    // @Builder
    // public RestaurantHourEntity(DayOfWeeks dayOfWeek, LocalTime openTime, LocalTime closeTime, OpenStatus openStatus) {
    //     this.dayOfWeek = dayOfWeek;
    //     this.openTime = openTime;
    //     this.closeTime = closeTime;
    //     this.openStatus = openStatus;
    // }
    @Builder
    public RestaurantHourEntity(DayOfWeeks dayOfWeek, LocalTime openTime, LocalTime closeTime, OpenStatus openStatus,RestaurantEntity restaurant) {
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.openStatus = openStatus;
        this.restaurant = restaurant;
    }


    public void closeBusiness() {
        this.openStatus = OpenStatus.CLOSE;
    }
}
