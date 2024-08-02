package org.example.catch_line.restaurant.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.catch_line.common.BaseTimeEntity;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.menu.model.entity.MenuEntity;
import org.example.catch_line.reservation.model.entity.ReservationEntity;
import org.example.catch_line.review.model.entity.ReviewEntity;
import org.example.catch_line.waiting.model.entity.WaitingEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    @Column(nullable = false)
    private String name;
    private String description;

    @Column(nullable = false)
    private BigDecimal rating;

    private String phoneNumber;

    @Column(nullable = false)
    private BigDecimal latitude;

    @Column(nullable = false)
    private BigDecimal longitude;

    @Column(nullable = false)
    private Long scrapCount;

    @Column(nullable = false)
    private Long reviewCount;

    @Enumerated(EnumType.STRING)
    private FoodType foodType;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @OneToMany(mappedBy = "restaurant")
    private List<ReviewEntity> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<ReservationEntity> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<WaitingEntity> waitings = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<MenuEntity> menus = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantHourEntity> restaurantHours = new ArrayList<>();

    @ManyToMany(mappedBy = "restaurantScraps")
    private List<MemberEntity> scraps = new ArrayList<>();


    @Builder
    public RestaurantEntity(String name, String description, BigDecimal rating, String phoneNumber, BigDecimal latitude, BigDecimal longitude, FoodType foodType, ServiceType serviceType) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.scrapCount = 0L;
        this.reviewCount = 0L;
        this.foodType = foodType;
        this.serviceType = serviceType;
    }
}
