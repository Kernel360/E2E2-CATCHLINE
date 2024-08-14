package org.example.catch_line.restaurant.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.catch_line.common.BaseTimeEntity;
import org.example.catch_line.common.model.mapper.RatingConverter;
import org.example.catch_line.common.model.vo.Rating;
import org.example.catch_line.scrap.model.entity.ScrapEntity;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.mapper.converter.PhoneNumberConverter;
import org.example.catch_line.user.member.model.vo.PhoneNumber;
import org.example.catch_line.menu.model.entity.MenuEntity;
import org.example.catch_line.booking.reservation.model.entity.ReservationEntity;
import org.example.catch_line.restaurant.model.entity.constant.FoodType;
import org.example.catch_line.restaurant.model.entity.constant.ServiceType;
import org.example.catch_line.review.model.entity.ReviewEntity;
import org.example.catch_line.booking.waiting.model.entity.WaitingEntity;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;

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
    @Convert(converter = RatingConverter.class)
    private Rating rating;

    @Column(nullable = false)
    @Convert(converter = PhoneNumberConverter.class)
    private PhoneNumber phoneNumber;

    @Column(nullable = false, precision = 30, scale = 20)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 30, scale = 20)
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

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantImageEntity> restaurantImages = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<ScrapEntity> scraps = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private OwnerEntity owner;


    @Builder
    public RestaurantEntity(String name, String description, Rating rating, PhoneNumber phoneNumber, FoodType foodType, ServiceType serviceType,OwnerEntity owner, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
        this.scrapCount = 0L;
        this.reviewCount = 0L;
        this.foodType = foodType;
        this.serviceType = serviceType;
        this.owner = owner;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updateReview(Rating rating, Long reviewCount) {
        this.rating = rating;
        this.reviewCount = reviewCount;
    }

    public void updateScrap(Long scrapCount) {
        this.scrapCount = scrapCount;
    }

    // TODO: 위도, 경도 값도 수정할 수 있도록 변경
    public void updateReservation(String name, String description, PhoneNumber phoneNumber, FoodType foodType, ServiceType serviceType) {
        this.name = name;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.foodType = foodType;
        this.serviceType = serviceType;
    }

    // 사용자가 식당 스크랩 시 `scrapCount` 1 증가
    public void addScrapCountByUser() {
        this.scrapCount++;

    }

    // 사용자가 식당 스크랩 취소 시 `scrapCount` 1 감소
    public void reduceScrapCountByUser() {
        this.scrapCount--;
    }
}
